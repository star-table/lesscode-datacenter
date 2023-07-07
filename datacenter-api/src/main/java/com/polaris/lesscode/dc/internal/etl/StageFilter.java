package com.polaris.lesscode.dc.internal.etl;

import java.util.List;

import lombok.Data;

@Data
public class StageFilter {

	private String rel;
	
	private List<StageCondition> cond;
}
