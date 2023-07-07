package com.polaris.lesscode.dc.internal.model;

import java.util.List;

import lombok.Data;

@Data
public class AddStorageValue {

	private String table;

	private String data;

	private Boolean move;

	private Long nextDataId;
	
	private List<SubStorageValue> subDatas;

	public AddStorageValue() {
	}

	public AddStorageValue(String table, String data, Boolean move, Long nextDataId) {
		this.table = table;
		this.data = data;
		this.move = move;
		this.nextDataId = nextDataId;
	}

	public AddStorageValue(String table, String data, List<SubStorageValue> subDatas, Boolean move, Long nextDataId) {
		this.table = table;
		this.data = data;
		this.subDatas = subDatas;
		this.move = move;
		this.nextDataId = nextDataId;
	}

	public AddStorageValue(List<SubStorageValue> subDatas) {
		this.subDatas = subDatas;
	}
	
}
