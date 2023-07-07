package com.polaris.lesscode.dc.internal.dsl;

import java.util.List;

import lombok.Data;

@Data
public class Sql {

	private String sql;
	
	private List<Object> args;

	public Sql(String sql, List<Object> args) {
		this.sql = sql;
		this.args = args;
	}

	public Sql() {
	}
	
}
