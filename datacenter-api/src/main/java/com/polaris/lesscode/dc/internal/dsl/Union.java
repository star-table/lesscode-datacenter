package com.polaris.lesscode.dc.internal.dsl;

import lombok.Data;

@Data
public class Union {

	//union, unionall
	private String unionType;
	
	private Query query;

	public Union(String unionType, Query query) {
		this.unionType = unionType;
		this.query = query;
	}

	public Union() {
	}
	
}
