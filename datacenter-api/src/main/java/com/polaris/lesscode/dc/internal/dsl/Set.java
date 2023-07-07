package com.polaris.lesscode.dc.internal.dsl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.polaris.lesscode.dc.internal.enums.ResultCode;
import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.util.GsonUtils;
import com.polaris.lesscode.util.JacksonUtils;
import lombok.Data;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.util.CollectionUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * Set条件
 *
 * @author Nico
 * @date 2021/2/1 16:30
 */
@Data
public class Set {

    public static final String PGTYPE_TEXT = "text";
    public static final String PGTYPE_JSONB = "jsonb";

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_JSON = 2;

    public static final int ACTION_SET = 1;
    public static final int ACTION_JSON_ARRAY_ADD_ITEM = 2; // 仅限json类型
    public static final int ACTION_JSON_ARRAY_DEL_ITEM = 3; // 仅限json类型
    public static final int ACTION_JSON_MAP_SET = 4;        // 仅限json类型

    public static final int MAX_JSON_DEPTH_SET = 1;     // json set目前只支持最外层的column
    public static final int MAX_JSON_DEPTH_ARRAY = 2;   // json array add/del目前最多支持最外2层的column

    /**
     * 表别名
     **/
    private String tableAlias;

    /**
     * 字段名
     **/
    private String column;

    /**
     * 字段值
     **/
    private Object value;

    /**
     * 字段类型，1 普通字段，2 jsonb，3 jsonb nest array
     **/
    private int type;

    /**
     * 操作类型，1 SET 直接设置，2 json数组添加元素，3 json数组删除元素
     **/
    private int action;

    /**
     * 是否"无需转义"(限Json类型)
     **/
    private boolean withoutPretreat;

    public Set() {
        this.type = TYPE_NORMAL;
        this.action = ACTION_SET;
    }

    public Set(String tableAlias, String column, Object value, int type, boolean withoutPretreat) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.value = value;
        this.type = type;
        this.action = ACTION_SET;
        this.withoutPretreat = withoutPretreat;
    }

    public Set(String tableAlias, String column, Object value, int type, int action, boolean withoutPretreat) {
        this.tableAlias = tableAlias;
        this.column = column;
        this.value = value;
        this.type = type;
        this.action = action;
        this.withoutPretreat = withoutPretreat;
    }

    public Set(String column, Object value, int type, boolean withoutPretreat) {
        this(null, column, value, type, withoutPretreat);
    }

    public Set(String column, Object value, int type, int action, boolean withoutPretreat) {
        this(null, column, value, type, action, withoutPretreat);
    }

    public String toSqlNormal(List<Object> args) {
        String tempColumn = column;
        if (!tempColumn.contains("\"")) {
            tempColumn = "\"" + column + "\"";
        }
        if (withoutPretreat) {
            return tempColumn + " = " + value;
        } else {
            args.add(value);
            return tempColumn + " = ?";
        }
    }

    public String toSqlJson(List<Object> args) {
        switch (action) {
            case ACTION_SET:
                return toSqlJsonSet(args);

            case ACTION_JSON_ARRAY_ADD_ITEM:
                return toSqlJsonArrayAddItem(args);

            case ACTION_JSON_ARRAY_DEL_ITEM:
                return toSqlJsonArrayDelItem(args);

            case ACTION_JSON_MAP_SET:
                return toSqlJsonMapSet(args);

            default:
                return "";
        }
    }

    public String toSqlJsonSet(List<Object> args) {
        List<String> columns = getColumns();
        String target = getJsonSetTarget(columns);
        String path = getJsonSetPathFull(columns);
        String c = getJsonSetColumnFull(columns);

        Object sqlValue = value;
        if (!withoutPretreat) {
            sqlValue = "?";
            if (value != null) {
                if (value instanceof Map || value instanceof Collection) {
                    args.add(newPgObject(toJson(value)));
                } else {
                    args.add(newPgObject(StringEscapeUtils.escapeJava(String.valueOf(value))));
                }
            }
        }

        if (StringUtils.isBlank(path)) {
            if (value == null) {
                return target + "='{}'";
            } else if (value instanceof Map || value instanceof Collection) {
                return target + "=jsonb_strip_nulls(" + target + "||" + sqlValue + ")";
            } else {
                return target + "=" + sqlValue;
            }
        } else {
            if (value == null) {
                return target + "=jsonb_set(" + target + "," + path + ",'{}')";
//            } else if (value instanceof Map || value instanceof Collection) {
//                return target + "=jsonb_set(" + target + "," + path + "," + c + "||" + sqlValue + ")";
            } else {
                return target + "=jsonb_set(" + target + "," + path + "," + sqlValue + ")";
            }
        }
    }

    public String toSqlJsonMapSet(List<Object> args) {
        List<String> columns = getColumns();
        String target = getJsonSetTarget(columns);
        String path = getJsonSetPathFull(columns);
        String c = getJsonSetColumnFullMap(columns);

        Object sqlValue = value;
        if (!withoutPretreat) {
            sqlValue = "?";
            if (value != null) {
                if (value instanceof Map || value instanceof Collection) {
                    args.add(newPgObject(toJson(value)));
                } else {
                    args.add(newPgObject(StringEscapeUtils.escapeJava(String.valueOf(value))));
                }
            }
        }

        if (StringUtils.isBlank(path)) {
            if (value == null) {
                return target + "='{}'";
            } else if (value instanceof Map || value instanceof Collection) {
                return target + "=" + target + "||" + sqlValue;
            } else {
                return target + "=" + sqlValue;
            }
        } else {
            if (value == null) {
                return target + "=jsonb_set(" + target + "," + path + ",'{}')";
            } else if (value instanceof Map || value instanceof Collection) {
                return target + "=jsonb_set(" + target + "," + path + "," + c + "||" + sqlValue + ")";
            } else {
                return target + "=jsonb_set(" + target + "," + path + "," + sqlValue + ")";
            }
        }
    }

    public String toSqlJsonArrayAddItem(List<Object> args) {
        List<String> columns = getColumns();
        int depth = getJsonDepth(columns);

        if (depth == 1) {
            return toSqlJsonArrayAddItemDepth1(args, columns);
        } else {
            return toSqlJsonArrayAddItemDepth2(args, columns);
        }
    }

    public String toSqlJsonArrayAddItemDepth1(List<Object> args, List<String> columns) {
        String target = getJsonSetTarget(columns);
        String path = getJsonSetPath1(columns);
        String c = getJsonSetColumn1Array(columns);

        Object sqlValue;
        if (!withoutPretreat) {
            sqlValue = "?";
            args.add(newPgObject(toJson(value)));
        } else {
            sqlValue = "'" + toJson(value) + "'";
        }
        String sql = target + " = jsonb_set(" + target + "," + path + "," + c + "||" + sqlValue + ")";
        return sql;
    }

    public String toSqlJsonArrayAddItemDepth2(List<Object> args, List<String> columns) {
        String target = getJsonSetTarget(columns);
        String path = getJsonSetPathFull(columns);
        String c = getJsonSetColumnFullArray(columns);
        String path1 = getJsonSetPath1(columns);
        String c1 = getJsonSetColumn1Map(columns);

        Object sqlValue = value;
        if (!withoutPretreat) {
            sqlValue = "?";
            args.add(newPgObject(toJson(value)));
        } else {
            sqlValue = "'" + toJson(value) + "'";
        }

        String sql = target + " = jsonb_set(" + target + "," + path1 + "," + c1 + "||jsonb_build_object('" + columns.get(2) + "',(" +  c + "||" + sqlValue + ")))";
        return sql;
    }

    public String toSqlJsonArrayDelItem(List<Object> args) {
        List<String> columns = getColumns();
        String target = getJsonSetTarget(columns);
        String path = getJsonSetPathFull(columns);
        String c = getJsonSetColumnFullArray(columns);

        StringBuilder builder = new StringBuilder();
        builder.append(target + " = jsonb_set(" + target + "," + path + "," + c);
        if (!withoutPretreat) {
            if (value instanceof Collection) {
                for (Object o : (Collection) value) {
                    builder.append("-?");
                    args.add(newPgObjectWithType(PGTYPE_TEXT, o.toString()));
                }
            } else {
                builder.append("-?");
                args.add(newPgObjectWithType(PGTYPE_TEXT, value.toString()));
            }
        } else {
            if (value instanceof Collection) {
                for (Object o : (Collection) value) {
                    builder.append("-'" + o + "'");
                }
            } else {
                builder.append("-'" + value + "'");
            }
        }
        builder.append(")");
        String sql = builder.toString();
        return sql;
    }

    public String toSql(List<Object> args) {
        switch (type) {
            case TYPE_NORMAL:
                return toSqlNormal(args);
            case TYPE_JSON:
                return toSqlJson(args);
        }
        return "";
    }

    private List<String> getColumns() {
        // 表名前缀
        String tablePrefix;
        if (StringUtils.isBlank(tableAlias)) {
            tablePrefix = "";
        } else {
            tablePrefix = tableAlias + ".";
        }

        ArrayList<String> columns = new ArrayList<>();
        String[] cs = column.split("\\.");
        columns.add(tablePrefix + cs[0]);

        if (cs.length > 1) {
            for (int i = 1; i < cs.length; i++) {
                columns.add(cs[i]);
            }
        }

        if (!isJsonPathDepthSupport(columns)) {
            throw new BusinessException(ResultCode.JSON_PATH_DEPTH_NOT_SUPPORT);
        }
        return columns;
    }

    private String getJsonSetTarget(List<String> columns) {
        return columns.get(0);
    }

    private int getJsonDepth(List<String> columns) {
        return columns.size() - 1;
    }

    private String getJsonSetPath1(List<String> columns) {
        if (columns.size() <= 1) {
            return "";
        }
        return "'{" + columns.get(1) + "}'";
    }

    private String getJsonSetPathFull(List<String> columns) {
        if (columns.size() <= 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder("'{");
        for (int i = 1; i < columns.size(); i++) {
            if (i > 1) {
                builder.append(",");
            }
            builder.append(columns.get(i));
        }
        builder.append("}'");
        return builder.toString();
    }

    private String getJsonSetColumn1Map(List<String> columns) {
        if (columns.size() <= 1) {
            return "";
        }
        return "coalesce(" + columns.get(0) + "->'" + columns.get(1) + "', '{}')";
    }

    private String getJsonSetColumn1Array(List<String> columns) {
        if (columns.size() <= 1) {
            return "";
        }
        return "coalesce(" + columns.get(0) + "->'" + columns.get(1) + "', '[]')";
    }

    private String getJsonSetColumnFull(List<String> columns) {
        if (columns.size() <= 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(columns.get(0));
        for (int i = 1; i < columns.size(); i++) {
            builder.append("->'" + columns.get(i) + "'");
        }
        return builder.toString();
    }

    private String getJsonSetColumnFullMap(List<String> columns) {
        if (columns.size() <= 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder("coalesce(");
        builder.append(columns.get(0));
        for (int i = 1; i < columns.size(); i++) {
            builder.append("->'" + columns.get(i) + "'");
        }
        builder.append(", '{}')");
        return builder.toString();
    }

    private String getJsonSetColumnFullArray(List<String> columns) {
        if (columns.size() <= 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder("coalesce(");
        builder.append(columns.get(0));
        for (int i = 1; i < columns.size(); i++) {
            builder.append("->'" + columns.get(i) + "'");
        }
        builder.append(", '[]')");
        return builder.toString();
    }

    private boolean isJsonPathDepthSupport(List<String> columns) {
        if (action == ACTION_SET) {
            return getJsonDepth(columns) <= MAX_JSON_DEPTH_SET;
        } else {
            return getJsonDepth(columns) <= MAX_JSON_DEPTH_ARRAY;
        }
    }

    private Object newPgObjectWithType(String type, String value){
        PGobject pGobject = new PGobject();
        pGobject.setType(type);
        try {
            pGobject.setValue(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pGobject;
    }

    private Object newPgObject(String value){
        return newPgObjectWithType(PGTYPE_JSONB, value);
    }

    private String toJson(Object o){
        try {
            return JacksonUtils.Obj2Str(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return GsonUtils.toJson(o);
    }

}
