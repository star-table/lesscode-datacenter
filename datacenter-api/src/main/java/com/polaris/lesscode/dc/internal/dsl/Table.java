package com.polaris.lesscode.dc.internal.dsl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import lombok.Data;

@Data
public class Table {

	/**
	 * schame, subquery
	 */
	private String type;

	private String schema;

	private Query subquery;

	private String database;

	private String alias;

	/**
	 * 关联类型, right, left, inner, full
	 */
	private String joinType;

	/**
	 * 关联表
	 */
	private Table join;

	/**
	 * 关联条件
	 */
	private Condition on;

	public Table(String database, String schema, String alias) {
		this.type = "schema";
		this.schema = schema;
		this.database = database;
		this.alias = alias;
	}

	public Table(String database, String schema) {
		this(database, schema, null);
	}

	public Table(Query subquery, String alias) {
		this.type = "subquery";
		this.subquery = subquery;
		this.alias = alias;
	}

	public Table(String schema) {
		this(null, schema, null);
	}

	public Table() {}

	public Table leftJoin(Table join) {
		this.join = join;
		this.joinType = "left";
		return this;
	}

	public Table rightJoin(Table join) {
		this.join = join;
		this.joinType = "right";
		return this;
	}

	public Table innerJoin(Table join) {
		this.join = join;
		this.joinType = "inner";
		return this;
	}

	public Table fullOuterJoin(Table join) {
		this.join = join;
		this.joinType = "full outer";
		return this;
	}

	public Table on(Condition cond) {
		this.on = cond;
		return this;
	}

	public String toSql(List<Object> args) {
		StringBuilder builder = new StringBuilder();
		if("schema".equals(this.type)) {
			if(StringUtils.isNotBlank(database)) {
				builder.append("" + database + ".");
			}
			builder.append("\"" + schema + "\"");
		}else if(subquery != null){
			Sql sql = subquery.toSql();
			if(! CollectionUtils.isEmpty(sql.getArgs())) {
				args.addAll(sql.getArgs());
			}
			builder.append(" (" + sql.getSql() + ") ");
		}
		if(StringUtils.isNotBlank(alias)) {
			builder.append(" " + alias + " ");
		}
		if(join != null) {
			builder.append(" " + joinType + " join " + join.toSql(args) + " on " + on.toSql(args));
		}
		return builder.toString();
	}

}
