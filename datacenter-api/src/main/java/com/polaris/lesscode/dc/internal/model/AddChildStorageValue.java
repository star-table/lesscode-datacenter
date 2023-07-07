package com.polaris.lesscode.dc.internal.model;

import lombok.Data;

@Data
public class AddChildStorageValue {

	private String table;

	private Long parentId;
	
	private String data;

	public AddChildStorageValue(String table, Long parentId, String data) {
		this.table = table;
		this.parentId = parentId;
		this.data = data;
	}

	public AddChildStorageValue() {
	}
	
}
