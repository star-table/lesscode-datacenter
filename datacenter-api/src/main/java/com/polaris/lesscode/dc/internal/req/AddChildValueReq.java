package com.polaris.lesscode.dc.internal.req;

import java.util.List;

import com.polaris.lesscode.dc.internal.model.AddChildStorageValue;

import lombok.Data;

@Data
public class AddChildValueReq {

	private List<AddChildStorageValue> datas;
}
