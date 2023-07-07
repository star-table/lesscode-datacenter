package com.polaris.lesscode.dc.internal.dsl;

import org.apache.commons.lang3.StringUtils;

public class Agg {
    public static final String COUNT = "count";
    public static final String COUNT_DISTINCT = "countdistinct";
    public static final String DISTINCT = "distinct";
    public static final String PREFIX_DISTINCT = "distinct_"; // 以这个打头的会变成如下等： sum(distinct column)

    public static String aggJsonColumn(String aggFunc, String tableAlias, String column, String columnAlias,
                                       boolean isArray, boolean isNoLimit) {

        String jsonColumn = SqlUtil.wrapperAliasJsonColumn(tableAlias, column);
        if (StringUtils.isEmpty(aggFunc) || DISTINCT.equalsIgnoreCase(aggFunc)) {
            if (isArray) {
                if (DISTINCT.equalsIgnoreCase(aggFunc)) {
                    if (isNoLimit) {
                        jsonColumn = "agg_distinct_no_limit(" + jsonColumn + ")";
                    } else {
                        jsonColumn = "agg_distinct_limit(" + jsonColumn + ")";
                    }
                } else {
                    if (isNoLimit) {
                        jsonColumn = "agg_no_limit(" + jsonColumn + ")";
                    } else {
                        jsonColumn = "agg_limit(" + jsonColumn + ")";
                    }
                }
                return SqlUtil.wrapperAliasColumn(null,jsonColumn, columnAlias);
            } else if (DISTINCT.equalsIgnoreCase(aggFunc)) {
                jsonColumn = "DISTINCT " + jsonColumn;
            }
        } else if (COUNT.equalsIgnoreCase(aggFunc)) {
            if (isArray) {
               aggFunc = "sum";
               jsonColumn = "jsonb_array_length(" + jsonColumn + ")";
            } else {
                jsonColumn = Conditions.getEmptyCaseWhen(SqlUtil.wrapperAliasJsonTextColumn(tableAlias, column));
            }
        } else if(COUNT_DISTINCT.equalsIgnoreCase(aggFunc)) {
            if (isArray) {
                jsonColumn = "agg_no_limit(" + jsonColumn + ")";
                jsonColumn = "array_length(ARRAY(SELECT DISTINCT jsonb_array_elements_text(" + jsonColumn + ")), 0)";
                return jsonColumn;
            } else {
                jsonColumn = Conditions.getEmptyCaseWhen(SqlUtil.wrapperAliasJsonTextColumn(tableAlias, column));
            }
        } else {
            jsonColumn = SqlUtil.wrapperAliasJsonTextColumn(tableAlias, column);
        }

        return aggColumn(aggFunc, jsonColumn, column, columnAlias, isNoLimit);
    }

    public static String aggColumn(String aggFunc, String column, String originColumn, String columnAlias, boolean isNoLimit) {
        if (SqlUtil.notJsonField.contains(StringUtils.replace(originColumn, "\"", ""))) {
            column = "to_jsonb(" + column +")";
        }
        if (StringUtils.isEmpty(aggFunc) || DISTINCT.equalsIgnoreCase(aggFunc)) {
            aggFunc = "agg_limit";
            if (isNoLimit) {
                aggFunc = "agg_no_limit";
            }
            return SqlUtil.wrapperAliasColumn(null,aggFunc + "("+column +")", columnAlias);
        }  else if (COUNT.equalsIgnoreCase(aggFunc)) {
            return SqlUtil.wrapperAliasColumn(null, "count(" + column + ")", columnAlias);
        } else if (COUNT_DISTINCT.equalsIgnoreCase(aggFunc)) {
            return SqlUtil.wrapperAliasColumn(null, "count(distinct " + column + ")", columnAlias);
        } else {
            column = "(" + column + ")::FLOAT";
            if (aggFunc.startsWith(PREFIX_DISTINCT)) {
                return SqlUtil.wrapperAliasColumn(null, aggFunc + "(distinct " + column + ")", columnAlias);
            }
            return SqlUtil.wrapperAliasColumn(null, aggFunc + "(" + column + ")", columnAlias);
        }
    }

    // 在去重的基础上使用sum等操作，如果已经是去重计算的话，直接忽略
    public static String addDistinctAgg(String aggFunc) {
        if (COUNT_DISTINCT.equalsIgnoreCase(aggFunc)) {
            return aggFunc;
        }
        return  PREFIX_DISTINCT + aggFunc;
    }
}
