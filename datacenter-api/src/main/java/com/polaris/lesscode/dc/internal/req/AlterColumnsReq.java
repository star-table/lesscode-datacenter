package com.polaris.lesscode.dc.internal.req;

import java.util.List;

import com.polaris.lesscode.dc.internal.model.StorageField;

import lombok.Data;

@Data
public class AlterColumnsReq {
	
	private List<StorageField> adds;
	
	private List<StorageField> dels;
}
