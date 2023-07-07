package com.polaris.lesscode.dc.internal.req;

import java.util.List;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import lombok.Data;

@Data
public class DeleteValueReq {

	private List<Long> ids;

	/**
	 * 删除条件
	 **/
	private Condition cond;
	
}
