package com.polaris.lesscode.dc.internal.etl;

import java.util.Map;

import lombok.Data;

@Data
public class StageField {

	private String name;
	
	private String title;
	
	private String type;
	
	private String op;
	
	private Map<String, String> src;
}
