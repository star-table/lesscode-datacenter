package com.polaris.lesscode.dc.internal.service;

import java.util.*;
import java.util.stream.Collectors;

import com.polaris.lesscode.dc.consts.PostgreSqlTemplate;
import com.polaris.lesscode.dc.datasource.PostgreSqlDataSourceFactory;
import com.polaris.lesscode.dc.internal.dsl.*;
import com.polaris.lesscode.dc.internal.enums.ResultCode;
import com.polaris.lesscode.dc.internal.req.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.polaris.lesscode.dc.entity.DataBase;
import com.polaris.lesscode.dc.entity.DataSource;
import com.polaris.lesscode.dc.internal.model.AddStorageValue;
import com.polaris.lesscode.dc.internal.model.Storage;
import com.polaris.lesscode.dc.internal.model.StorageValue;
import com.polaris.lesscode.dc.internal.resp.CreateViewResp;
import com.polaris.lesscode.dc.internal.resp.DbAllotResp;
import com.polaris.lesscode.dc.mapper.DataBaseMapper;
import com.polaris.lesscode.dc.mapper.DataSourceMapper;
import com.polaris.lesscode.exception.SystemException;
import com.polaris.lesscode.vo.Page;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DataCenterService {
	
	@Autowired
	private DataSourceMapper dataSourceMapper;
	
	@Autowired
	private DataBaseMapper dataBaseMapper;
	
	@Autowired
	private DataService dataService;

	@Autowired
	private PostgreSqlDataSourceFactory dataSourceFactory;

	public DbAllotResp allot(Long orgId) {
		List<DataSource> dataSources = dataSourceMapper.getList();
		if(CollectionUtils.isEmpty(dataSources)) {
			throw new SystemException(ResultCode.NO_AVAILABLE_DATASOURCE.getCode(), ResultCode.NO_AVAILABLE_DATASOURCE.getMessage());
		}
		
		List<DataBase> databases = dataBaseMapper.getList(1, dataSources.stream().map(DataSource::getId).collect(Collectors.toList()));
		if(CollectionUtils.isEmpty(databases)) {
			throw new SystemException(ResultCode.NO_AVAILABLE_DATABASE.getCode(), ResultCode.NO_AVAILABLE_DATABASE.getMessage());
		}
		
		Map<Long, List<DataBase>> databasesGroup = databases.stream().collect(Collectors.groupingBy(DataBase::getDsId));
		
		//allot data source
		List<Long> avaliableDsIds = databasesGroup.keySet().stream().sorted().collect(Collectors.toList());
		Long dataSourceSelector = avaliableDsIds.get((int)(orgId % avaliableDsIds.size()));
		
		//allot database
		List<DataBase> targetDatabases = databasesGroup.get(dataSourceSelector);
		DataBase db = targetDatabases.get((int)(orgId % targetDatabases.size()));
		
		DbAllotResp allotResp = new DbAllotResp();
		allotResp.setDbId(db.getId());
		allotResp.setDsId(db.getDsId());
		return allotResp;
	}
	
	public int createTable(Long dsId, Long dbId, CreateTableReq req) {
		DataBase database = getDataBase(dsId, dbId);
		String databaseName = database.getName();
		String tableName = req.getTable();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);

		Properties properties = new Properties();
		properties.put("database", database);
		properties.put("table", tableName);
		String createTableSql = PostgreSqlTemplate.replacePlaceholders(req.isSub() ? PostgreSqlTemplate.CREATE_SUB_TABLE_TEMPLATE : PostgreSqlTemplate.CREATE_TABLE_TEMPLATE, properties);
		int excId = dataService.execute(jdbcTemplate, createTableSql, null);

		// 如果是创建汇总表，则创建一张汇总的统计表，用于统计各种数据
		if (req.isSummaryFlag()) {
			properties.put("table", tableName + PostgreSqlTemplate.STATISTICS_END_FIX);
			String createStatisticsSql = PostgreSqlTemplate.replacePlaceholders(PostgreSqlTemplate.CREATE_TABLE_STATISTICS_TEMPLATE, properties);
			return dataService.execute(jdbcTemplate, createStatisticsSql, null);
		}

		return excId;
	}

	public void createTables(Long dsId, Long dbId, List<CreateTableReq> reqs) {
		DataBase database = getDataBase(dsId, dbId);
		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);

		List<String> sqls = new ArrayList<>();
		Properties properties = new Properties();
		if(CollectionUtils.isNotEmpty(reqs)){
			for(CreateTableReq req: reqs){
				properties.put("database", database);
				properties.put("table", req.getTable());
				String createTableSql = PostgreSqlTemplate.replacePlaceholders(req.isSub() ? PostgreSqlTemplate.CREATE_SUB_TABLE_TEMPLATE : PostgreSqlTemplate.CREATE_TABLE_TEMPLATE, properties);
				sqls.add(createTableSql);

				// 如果是创建汇总表，则创建一张汇总的统计表，用于统计各种数据
				if (req.isSummaryFlag()) {
					properties.put("table", req.getTable() + PostgreSqlTemplate.STATISTICS_END_FIX);
					String createStatisticsSql = PostgreSqlTemplate.replacePlaceholders(PostgreSqlTemplate.CREATE_TABLE_STATISTICS_TEMPLATE, properties);
					sqls.add(createStatisticsSql);
				}
			}
		}
		dataService.execute(jdbcTemplate, sqls.toArray(new String[]{}));
	}
	
	public CreateViewResp createView(Long dsId, Long dbId, CreateViewReq req) {
		DataBase database = getDataBase(dsId, dbId);
		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);
		
		View view = new View(req.getViewName(), req.getQuery());
		dataService.execute(jdbcTemplate, view.toSql());
		
		CreateViewResp resp = new CreateViewResp();
		resp.setViewName(view.getViewName());
		return resp;
	}

	public List<Map<String, Object>> query(Long dsId, Long dbId, Query query) {
		DataBase database = getDataBase(dsId, dbId);
		
		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);

		List<Map<String, Object>> values = dataService.query(jdbcTemplate, query);
		return values;
	}

	public List<Map<String, Object>> queryBySql(Long dsId, Long dbId, QueryBySqlReq query) {
		DataBase database = getDataBase(dsId, dbId);

		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);

		List<Object> args = new ArrayList<>();
		String whereSql = query.getCondition().toSql(args);
		String sql = StringUtils.replace(query.getSql(), query.getWhereSqlPlaceholder(), whereSql);

		return dataService.query(jdbcTemplate, sql, args.toArray());
	}

	public Page<Map<String, Object>> page(Long dsId, Long dbId, Query query) {
		DataBase database = getDataBase(dsId, dbId);

		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);

		Query countQuery = query.copy();
		countQuery.setColumns(Collections.singletonList("count(*) as count"));
		countQuery.setLimit(null);
		countQuery.setOffset(null);
		countQuery.setOrders(null);

		long total = 0;
		List<Map<String, Object>> values = new ArrayList<>();

		List<Map<String, Object>> countDatas = dataService.query(jdbcTemplate, countQuery);
		if(CollectionUtils.isNotEmpty(countDatas)){
			total = (long) countDatas.get(0).get("count");
		}
		if(total > 0 && total > query.getOffset()){
			values = dataService.query(jdbcTemplate, query);
		}
		return new Page<>(total, values);
	}

	public int execute(Long dsId, Long dbId, Executor executor) {
		DataBase database = getDataBase(dsId, dbId);

		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);
		return dataService.execute(jdbcTemplate, executor);
	}

	public int[] execute(Long dsId, Long dbId, List<Executor> executors) {
		DataBase database = getDataBase(dsId, dbId);

		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);
		return dataService.execute(jdbcTemplate, executors);
	}

	public int alter(Long dsId, Long dbId, Alter alter){
		DataBase database = getDataBase(dsId, dbId);

		String databaseName = database.getName();
		JdbcTemplate jdbcTemplate = dataSourceFactory.getJdbcTemplate(dsId, databaseName);
		return dataService.execute(jdbcTemplate, alter.toSql());
	}
	
	private DataBase getDataBase(Long dsId, Long dbId){
		DataSource dataSource = dataSourceMapper.get(dsId);
		if(dataSource == null) {
			throw new SystemException(ResultCode.CURRENT_DATASOURCE_DISABLE.getCode(), ResultCode.CURRENT_DATASOURCE_DISABLE.getMessage());
		}

		DataBase database = dataBaseMapper.get(dbId);
		if(database == null) {
			throw new SystemException(ResultCode.CURRENT_DATABASE_DISABLE.getCode(), ResultCode.CURRENT_DATABASE_DISABLE.getMessage());
		}
		return database;
	}

}
