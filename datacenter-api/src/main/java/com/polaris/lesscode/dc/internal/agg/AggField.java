package com.polaris.lesscode.dc.internal.agg;

import java.util.List;

import lombok.Data;

@Data
public class AggField {

	private String name;
	
	private String type;
	
	private String text;
	
	private String tag;
	
	private String subform;
	
	private String category;
	
	private String formula;
	
	private List<String> forms;
	
	private Boolean isJoined;
}
