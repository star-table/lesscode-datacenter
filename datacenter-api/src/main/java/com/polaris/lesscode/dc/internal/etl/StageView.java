package com.polaris.lesscode.dc.internal.etl;

import java.util.List;

import com.polaris.lesscode.dc.internal.dsl.Query;

import lombok.Data;

@Data
public class StageView {

	private Stage stage;
	
	private Query query;
	
	private List<StageView> childs;
	
	private List<String> fields;
	
	public boolean needSubquery(){
		return "union".equals(stage.getType()) || query.hasGroup();
	}

	
}
