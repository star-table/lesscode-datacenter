package com.polaris.lesscode.dc.consts;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.polaris.lesscode.dc.util.PlaceHolderHelper;
import com.polaris.lesscode.enums.StorageFieldType;

public class PostgreSqlTemplate {

	public static final String CREATE_TABLE_TEMPLATE = "CREATE TABLE IF NOT EXISTS \"${table}\" (" +
			"		  \"id\" int8 NOT NULL," + 
			"		  \"data\" jsonb NOT NULL," +
			"		   PRIMARY KEY (\"id\")" + 
			"		);"
			+ "CREATE INDEX IF NOT EXISTS \"${table}_jsonb_index\" ON \"${table}\" USING gin (" +
			"  \"data\" \"pg_catalog\".\"jsonb_ops\"" + 
			") WITH (GIN_PENDING_LIST_LIMIT = 256);" +
			"CREATE INDEX IF NOT EXISTS \"${table}_jsonb_index_tableId\" ON \"${table}\" ((data->'tableId'),(data->'updateTime'),(data->'delFlag'));" +
			"CREATE INDEX IF NOT EXISTS \"${table}_jsonb_index_order\" ON \"${table}\" ((data->'order'));" +
			"CREATE INDEX IF NOT EXISTS \"${table}_jsonb_index_projectId\" ON ${table} ((data->'projectId'),(data->'recycleFlag'),(data->'delFlag'),(data->'planEndTime'),(data->'updateTime'));" +
			"CREATE INDEX IF NOT EXISTS \"${table}_jsonb_index_issueId\" ON \"${table}\" ((data->'issueId'),(data->'delFlag'));";

	public static final String CREATE_TABLE_STATISTICS_TEMPLATE = "CREATE TABLE IF NOT EXISTS \"${table}\" (" +
			"		  \"id\" int8 NOT NULL," +
			"		  \"data_id\" int8 NOT NULL," +
			"		  \"issue_id\" int8 NOT NULL," +
			"		  \"data_statistics\" jsonb NOT NULL," +
			"		   PRIMARY KEY (\"id\")" +
			"		);"
			+ "CREATE INDEX IF NOT EXISTS \"${table}_jsonb_index\" ON \"${table}\" USING gin (" +
			"  \"data_statistics\" \"pg_catalog\".\"jsonb_ops\"" +
			") WITH (GIN_PENDING_LIST_LIMIT = 256);"
			+ "CREATE INDEX IF NOT EXISTS index_data_id ON \"${table}\"  USING btree (data_id);"
			+ "CREATE INDEX IF NOT EXISTS index_issue_id ON \"${table}\"  USING btree (issue_id);";
	
	public static final String CREATE_SUB_TABLE_TEMPLATE = "CREATE TABLE IF NOT EXISTS \"${table}\" (" +
			"		  \"id\" int8 NOT NULL," + 
			"         \"parent_id\" int8 NOT NULL DEFAULT 0," + 
			"		  \"data\" jsonb NOT NULL," +
			"		   PRIMARY KEY (\"id\")" + 
			"		);"
			+ "CREATE INDEX IF NOT EXISTS \"jsonb_index_${table}\" ON \"${table}\" USING gin (" +
			"  \"data\" \"pg_catalog\".\"jsonb_ops\"" + 
			") WITH (GIN_PENDING_LIST_LIMIT = 256);" + 
			"CREATE INDEX IF NOT EXISTS \"parent_id_index_${table}\" ON \"${table}\" USING btree (" +
			"  \"parent_id\" \"pg_catalog\".\"int8_ops\" ASC NULLS LAST" + 
			");";

	public static final String STATISTICS_END_FIX = "_statistics";

	private static final PlaceHolderHelper PLACEHOLDER = new PlaceHolderHelper("${", "}");
	
	public static String replacePlaceholders(String value, final Properties properties) {
    	return PLACEHOLDER.replacePlaceholders(value, properties);
    }

}
