package com.polaris.lesscode.dc.internal.etl;

import lombok.Data;

@Data
public class StageCondition {

	private Object value;
	
	private String type;
	
	private String method;
	
	private String field;
}
