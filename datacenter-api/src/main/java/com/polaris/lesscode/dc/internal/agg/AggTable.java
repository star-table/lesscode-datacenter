package com.polaris.lesscode.dc.internal.agg;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AggTable {

	private String type;
	
	private String widgetName;
	
	private Integer height;
	
	private String width;
	
	private Boolean enable;
	
	private Boolean visible;
	
	private String title;
	
	private String linkWidgets;
	
	private List<String> forms;
	
	private String filter;
	
	private Boolean isHeadSticky;
	
	private Boolean isRowHeadSticky;
	
	private Map<String, Map<String, String>> relations;
	
	private List<AggField> joinedFields;
	
	private List<AggField> xFields;
	
	private List<AggField> yFields;
	
	private List<AggField> valueFields;
	
	private Boolean exportable;
}
