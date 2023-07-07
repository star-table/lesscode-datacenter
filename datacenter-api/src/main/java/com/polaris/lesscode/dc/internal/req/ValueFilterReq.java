package com.polaris.lesscode.dc.internal.req;

import java.util.List;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.dc.internal.dsl.Order;

import lombok.Data;

@Data
public class ValueFilterReq {

	private Integer offset;
	
	private Integer limit;
	
	private List<String> columns;
	
	private Condition condition;
	
	private List<Order> orders;

	private List<String> groups;
}
