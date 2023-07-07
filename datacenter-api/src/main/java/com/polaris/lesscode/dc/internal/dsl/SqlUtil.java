package com.polaris.lesscode.dc.internal.dsl;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class SqlUtil {

	public final static String CREATOR = "creator";
	public final static String CREATE_TIME = "createTime";
	public final static String UPDATOR = "updator";
	public final static String UPDATE_TIME = "updateTime";
	public final static String RECYCLE_FLAG = "recycleFlag";
	public final static String APP_ID = "appId";
	public final static String PROJECT_ID = "projectId";
	public final static String ORDER = "order";
	public final static String PATH = "path";
	public final static String CODE = "code";
	public final static String ID = "id";
	public final static String ORG_ID = "orgId";
	public final static String PARENT_ID = "parentId";
	public final static String ISSUE_ID = "issueId";
	public final static String TABLE_ID = "tableId";
	public final static String COLLABORATORS = "collaborators";

	public final static SortedSet<String> notJsonField = new TreeSet<String>(){
		{
			add(ID);
			add	(ORG_ID);
			add	(RECYCLE_FLAG);
			add	(CREATOR);
			add	(UPDATOR);
			add	(CREATE_TIME);
			add	(UPDATE_TIME);
			add	(APP_ID);
			add	(PROJECT_ID);
			add	(TABLE_ID);
			add	(PATH);
			add	(PARENT_ID);
			add	(ISSUE_ID);
			add	(COLLABORATORS);
			add	(ORDER);
			add	(CODE);
		}
	};

	public static String wrapperJsonColumn(String jsonColumn, String keyPath) {
		String keyPathTemp = StringUtils.replace(keyPath,"\"","");
		if (notJsonField.contains(keyPathTemp)) {
			return "\"" + keyPathTemp + "\"";
		}

		String jsonPath = "\"" + jsonColumn + "\"::jsonb ";
		String[] keys = keyPath.split("[.]");
		for (String key: keys){
			jsonPath = jsonPath + " -> '" + key + "'";
		}
		return jsonPath;
	}
	
	public static String wrapperJsonColumn(String keyPath) {
		return wrapperJsonColumn("data", keyPath);
	}

	public static String wrapperAliasJsonColumn(String tableAlias,String keyPath) {
		if (tableAlias == null) {
			return wrapperJsonColumn("data", keyPath);
		}
		return tableAlias+"."+ wrapperJsonColumn("data", keyPath);
	}

	public static String wrapperAliasJsonTextColumn(String tableAlias,String keyPath) {
		if (tableAlias == null) {
			return wrapperJsonTextColumn("data", keyPath);
		}

		return tableAlias+"."+ wrapperJsonTextColumn("data", keyPath);
	}

	public static String wrapperJsonTextColumn(String jsonColumn, String keyPath) {
		String keyPathTemp = StringUtils.replace(keyPath,"\"","");
		if (notJsonField.contains(keyPathTemp)) {
			return "\"" + keyPathTemp + "\"";
		}

		return "\"" + jsonColumn + "\"::jsonb ->> '" + keyPath + "'";
	}

	public static String wrapperJsonTextColumn(String keyPath) {
		return wrapperJsonTextColumn("data", keyPath);
	}

	public static String wrapperAliasColumn(String tableAlias, String column, String columnAlias) {
		StringBuilder builder = new StringBuilder();
		if(StringUtils.isNotBlank(tableAlias)) {
			if(column.contains("\"data\"")) {
				column = column.replace("\"data\"", tableAlias +".\"data\"" ) ;
			} else {
				column = tableAlias + "." + column;
			}
			builder.append(column);
		} else {
			builder.append(column);
		}

		if(StringUtils.isNotBlank(columnAlias)) {
			builder.append(" as " + columnAlias);
		}
		return builder.toString();
	}

	public static String wrapperAliasColumn(String tableAlias, String column) {
		return wrapperAliasColumn(tableAlias, column, null);
	}
	
	public static String parseColumnName(String columnStr) {
		String[] strs = columnStr.split(" as ");
		if(strs.length == 2) {
			return strs[1];
		}
		return columnStr;
	}
	
	public static String wrapperDatabaseTable(String database, String table) {
		return "`" + database + "`." + "`" + table + "`";
	}
	
	public static String wrapperAlias(Long formId, String fieldKey) {
		return formId + "$" + fieldKey;
	}
	
	public static String wrapperAlias(String formId, String fieldKey) {
		return formId + "$" + fieldKey;
	}
	
	public static String wrapperTableName(Long orgId, Long formId) {
		return "lc_data";
//		return "_form_" + orgId + "_" + formId;
	}
	
	public static String wrapperTableName(Long orgId, String formId) {
		return "lc_data";
//		return "_form_" + orgId + "_" + formId;
	}
	
	public static String wrapperSubTableName(Long orgId, String formId, String subFormId) {
		return "_form_" + orgId + "_" + formId + "_" + subFormId;
	}
	
	public static String wrapperSubTableName(Long orgId, Long formId, String subFormId) {
		return "_form_" + orgId + "_" + formId + "_" + subFormId;
	}

	public static String wrapperSubTableName(String mainTableName, String subFormId) {
		return "lc_data";
//		return mainTableName + "_" + subFormId;
	}
	
	public static String wrapperViewName(Long orgId, String aggId) {
		return "_view_" + orgId + "_" + aggId;
	}
	
	public static String wrapperViewName(Long orgId, Long aggId) {
		return "_view_" + orgId + "_" + aggId;
	}

}
