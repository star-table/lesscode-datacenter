package com.polaris.lesscode.dc.internal.model;

import lombok.Data;

@Data
public class StorageValue {

	private Long id;
	
	private String data;

	private Boolean move;

	private Long nextDataId;

	public StorageValue(Long id, String data, Boolean move, Long nextDataId) {
		this.id = id;
		this.data = data;
		this.move = move;
		this.nextDataId = nextDataId;
	}
	public StorageValue() {
	}

	public StorageValue(String data) {
		this.data = data;
	}
	
}
