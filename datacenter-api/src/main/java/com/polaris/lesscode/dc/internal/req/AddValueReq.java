package com.polaris.lesscode.dc.internal.req;

import java.util.List;

import com.polaris.lesscode.dc.internal.model.AddStorageValue;

import lombok.Data;

@Data
public class AddValueReq {

	private List<AddStorageValue> datas;
}
