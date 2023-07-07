package com.polaris.lesscode.dc.internal.controller;

import java.util.List;
import java.util.Map;

import com.polaris.lesscode.dc.internal.dsl.Alter;
import com.polaris.lesscode.dc.internal.dsl.Executor;
import com.polaris.lesscode.dc.internal.req.QueryBySqlReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.polaris.lesscode.dc.internal.api.DataCenterApi;
import com.polaris.lesscode.dc.internal.dsl.Query;
import com.polaris.lesscode.dc.internal.req.CreateTableReq;
import com.polaris.lesscode.dc.internal.req.CreateViewReq;
import com.polaris.lesscode.dc.internal.resp.CreateViewResp;
import com.polaris.lesscode.dc.internal.resp.DbAllotResp;
import com.polaris.lesscode.dc.internal.service.DataCenterService;
import com.polaris.lesscode.vo.Page;
import com.polaris.lesscode.vo.Result;

import io.swagger.annotations.Api;

@Api("数据中心")
@RestController
public class DataCenterController implements DataCenterApi{
	
	@Autowired
	private DataCenterService dataCenterService;

	@Override
	public Result<DbAllotResp> allot(@RequestParam Long orgId){
		return Result.ok(dataCenterService.allot(orgId));
	}

	@Override
	public Result<?> createTable(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody CreateTableReq req){
		return Result.ok(dataCenterService.createTable(dsId, dbId, req));
	}

	@Override
	public Result<?> createTables(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody List<CreateTableReq> req){
		dataCenterService.createTables(dsId, dbId, req);
		return Result.ok();
	}

	@Override
	public Result<CreateViewResp> createView(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody CreateViewReq req){
		CreateViewResp resp = dataCenterService.createView(dsId, dbId, req);
		return Result.ok(resp);
	}

	@Override
	public Result<List<Map<String, Object>>> query(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody Query query){
		return Result.ok(dataCenterService.query(dsId, dbId, query));
	}

	@Override
	public Result<Page<Map<String, Object>>> page(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody Query query){
		return Result.ok(dataCenterService.page(dsId, dbId, query));
	}

	@Override
	public Result<Integer> execute(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody Executor executor){
		return Result.ok(dataCenterService.execute(dsId, dbId, executor));
	}

	@Override
	public Result<int[]> execute(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody List<Executor> executors){
		return Result.ok(dataCenterService.execute(dsId, dbId, executors));
	}

	@Override
	public Result<Integer> alter(Long dsId, Long dbId, Alter alter) {
		return Result.ok(dataCenterService.alter(dsId, dbId, alter));
	}

	@Override
	public Result<List<Map<String, Object>>> queryBySql(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody QueryBySqlReq query){
		return Result.ok(dataCenterService.queryBySql(dsId, dbId, query));
	}
}
