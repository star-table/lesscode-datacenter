/**
 * 
 */
package com.polaris.lesscode.dc.internal.fallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.polaris.lesscode.dc.internal.dsl.Alter;
import com.polaris.lesscode.dc.internal.dsl.Executor;
import com.polaris.lesscode.dc.internal.req.*;
import org.springframework.stereotype.Component;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.dc.internal.api.DataCenterApi;
import com.polaris.lesscode.dc.internal.dsl.Query;
import com.polaris.lesscode.dc.internal.resp.CreateViewResp;
import com.polaris.lesscode.dc.internal.resp.DbAllotResp;
import com.polaris.lesscode.feign.AbstractBaseFallback;
import com.polaris.lesscode.vo.Page;
import com.polaris.lesscode.vo.Result;

import feign.hystrix.FallbackFactory;

/**
 * @author Bomb.
 *
 */
@Component
public class DataCenterFallbackFactory extends AbstractBaseFallback implements FallbackFactory<DataCenterApi> {

	@Override
	public DataCenterApi create(Throwable cause) {
		return new DataCenterApi() {
			
			@Override
			public Result<CreateViewResp> createView(Long dsId, Long dbId, CreateViewReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(new CreateViewResp()));
			}

			@Override
			public Result<List<Map<String, Object>>> query(Long dsId, Long dbId, Query query) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(new ArrayList<>()));
			}

			@Override
			public Result<List<Map<String, Object>>> queryBySql(Long dsId, Long dbId, QueryBySqlReq query) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(new ArrayList<>()));
			}

			@Override
			public Result<Page<Map<String, Object>>> page(Long dsId, Long dbId, Query query) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(new Page<>()));
			}

			@Override
			public Result<Integer> execute(Long dsId, Long dbId, Executor executor) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(-1));
			}

			@Override
			public Result<int[]> execute(Long dsId, Long dbId, List<Executor> executors) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(new int[]{}));
			}

			@Override
			public Result<Integer> alter(Long dsId, Long dbId, Alter alter) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(-1));
			}

			@Override
			public Result<?> createTable(Long dsId, Long dbId, CreateTableReq req) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause, (Supplier<Result<Object>>) Result::ok);
			}

			@Override
			public Result<?> createTables(Long dsId, Long dbId, List<CreateTableReq> req) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause, (Supplier<Result<Object>>) Result::ok);
			}

			@Override
			public Result<DbAllotResp> allot(Long orgId) {
				return wrappDeal(ApplicationConsts.APPLICATION_DATACENTER,cause,()-> Result.ok(new DbAllotResp()));
			}
			
		};
	}

}
