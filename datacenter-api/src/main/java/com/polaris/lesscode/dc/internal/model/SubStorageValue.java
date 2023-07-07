package com.polaris.lesscode.dc.internal.model;

import java.util.List;

import lombok.Data;

@Data
public class SubStorageValue {

	private Long parentId;
	
	private String subTable;
	
	private List<String> datas;

	public SubStorageValue(String subTable, List<String> datas) {
		this.subTable = subTable;
		this.datas = datas;
	}

	public SubStorageValue() {
	}

	public SubStorageValue(Long parentId, String subTable, List<String> datas) {
		this.parentId = parentId;
		this.subTable = subTable;
		this.datas = datas;
	}
	
	
}
