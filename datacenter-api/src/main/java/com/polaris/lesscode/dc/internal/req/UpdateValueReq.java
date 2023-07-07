package com.polaris.lesscode.dc.internal.req;

import java.util.List;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.dc.internal.model.StorageValue;

import lombok.Data;

@Data
public class UpdateValueReq {

	private List<StorageValue> values;

	private Condition cond;
}
