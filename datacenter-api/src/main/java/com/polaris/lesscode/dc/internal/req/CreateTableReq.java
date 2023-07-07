package com.polaris.lesscode.dc.internal.req;

import lombok.Data;

import java.util.List;

@Data
public class CreateTableReq {

	private String table;

	private boolean isSub;

	private boolean summaryFlag;
}
