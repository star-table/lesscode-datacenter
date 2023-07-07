package com.polaris.lesscode.dc.internal.req;

import com.polaris.lesscode.dc.internal.dsl.Query;

import lombok.Data;

@Data
public class CreateViewReq {

	private String viewName;
	
	private Query query;

	public CreateViewReq(String viewName, Query query) {
		this.viewName = viewName;
		this.query = query;
	}

	public CreateViewReq() {
	}
	
}
