package com.polaris.lesscode.dc.internal.dsl;

import lombok.Data;

@Data
public class View {

	private String viewName;
	
	private Query query;

	public View() {
	}

	public View(String viewName, Query query) {
		this.viewName = viewName;
		this.query = query;
	}
	
	public Sql toSql() {
		if(query == null) {
			return null;
		}
		Sql querySql = query.toSql();
		String sql = "create view " + viewName + " as " + querySql.getSql();
		return new Sql(sql, querySql.getArgs());
	}
	
}
