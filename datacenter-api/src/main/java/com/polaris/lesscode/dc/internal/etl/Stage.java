package com.polaris.lesscode.dc.internal.etl;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Stage {

	private String id;
	
	//filter, group, union, join, input, output
	private String type;
	
	private Long formId;
	
	private Long subformId;
	
	//inner, left, right
	private String join;
	
	private List<String> input;
	
	private List<StageField> fields;
	
	private List<StageField> dimensions;
	
	private List<StageField> metrics;
	
	private List<StageField> relation;
	
	private StageFilter filter;
	
	private List<String> mainFields;
	
	private List<String> subformFields;
}
