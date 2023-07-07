package com.polaris.lesscode.dc.internal.api;

import java.util.List;
import java.util.Map;

import com.polaris.lesscode.dc.internal.dsl.Alter;
import com.polaris.lesscode.dc.internal.dsl.Executor;
import com.polaris.lesscode.dc.internal.req.QueryBySqlReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import com.polaris.lesscode.dc.internal.dsl.Query;
import com.polaris.lesscode.dc.internal.req.CreateTableReq;
import com.polaris.lesscode.dc.internal.req.CreateViewReq;
import com.polaris.lesscode.dc.internal.resp.CreateViewResp;
import com.polaris.lesscode.dc.internal.resp.DbAllotResp;
import com.polaris.lesscode.vo.Page;
import com.polaris.lesscode.vo.Result;

@Api(value="数据中心内部接口", tags={"数据中心内部接口"})
@RequestMapping("/datacenter/inner/api/v1")
public
interface DataCenterApi {

	@ApiOperation("分配数据源id")
	@GetMapping("/allot")
	Result<DbAllotResp> allot(@RequestParam("orgId") Long orgId);

	@ApiOperation("创建表")
	@PostMapping("/{dsId}/{dbId}/tables")
	Result<?> createTable(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody CreateTableReq req);

	@ApiOperation("批量创建表")
	@PostMapping("/{dsId}/{dbId}/tables-batch")
	Result<?> createTables(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody List<CreateTableReq> req);

	@ApiOperation("创建视图")
	@PostMapping("/{dsId}/{dbId}/views")
	Result<CreateViewResp> createView(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody CreateViewReq req);

	@ApiOperation("查询")
	@PostMapping("/{dsId}/{dbId}/query")
	Result<List<Map<String, Object>>> query(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody Query query);

	@ApiOperation("分页查询")
	@PostMapping("/{dsId}/{dbId}/page")
	Result<Page<Map<String, Object>>> page(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody Query query);

	@ApiOperation("修改")
	@PostMapping("/{dsId}/{dbId}/execute")
	Result<Integer> execute(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody Executor executor);

	@ApiOperation("批量修改")
	@PostMapping("/{dsId}/{dbId}/execute-batch")
	Result<int[]> execute(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody List<Executor> executors);

	@ApiOperation("修改表结构")
	@PutMapping("/{dsId}/{dbId}/alter")
	Result<Integer> alter(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody Alter alter);

	@ApiOperation("通过sql语句查询")
	@PostMapping("/{dsId}/{dbId}/queryBySql")
	Result<List<Map<String, Object>>> queryBySql(@PathVariable("dsId") Long dsId, @PathVariable("dbId") Long dbId, @RequestBody QueryBySqlReq query);
}
