package com.polaris.lesscode.dc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
//import com.polaris.lesscode.BaseTest;
import com.polaris.lesscode.dc.datasource.PostgreSqlDataSourceFactory;
import com.polaris.lesscode.dc.internal.dsl.*;
import com.polaris.lesscode.dc.internal.req.CreateTableReq;
import com.polaris.lesscode.dc.internal.req.UpdateValueReq;
import com.polaris.lesscode.dc.internal.service.DataService;
import com.polaris.lesscode.vo.Page;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.polaris.lesscode.dc.BaseTest;
import com.polaris.lesscode.dc.internal.model.AddStorageValue;
import com.polaris.lesscode.dc.internal.model.Storage;
import com.polaris.lesscode.dc.internal.model.StorageField;
import com.polaris.lesscode.dc.internal.model.StorageValue;
import com.polaris.lesscode.dc.internal.model.SubStorageValue;
import com.polaris.lesscode.dc.internal.req.AddValueReq;
import com.polaris.lesscode.dc.internal.service.DataCenterService;
import com.polaris.lesscode.enums.StorageFieldType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgreSqlService extends BaseTest {

	@Autowired
	private DataCenterService datacenterService;

	@Autowired
	@Qualifier("postgresDataService")
	private DataService dataService;

	@Autowired
	private PostgreSqlDataSourceFactory dataSourceFactory;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Long dsId = 2L;

	private Long dbId = 1L;
	
	@Test
	public void testDataCenter() throws InterruptedException {
		String database = "lesscode-datas-001";
		String tableName = "_form_1_" + System.currentTimeMillis();
		Table table = new Table(tableName);

		CreateTableReq createTableReq = new CreateTableReq();
		createTableReq.setTable(tableName);
		createTableReq.setSub(false);

		CreateTableReq createTableReq1 = new CreateTableReq();
		createTableReq1.setTable("_form_1_" + System.currentTimeMillis() + "_abc");
		createTableReq1.setSub(true);
		datacenterService.createTables(dsId, dbId, Arrays.asList(createTableReq, createTableReq1));
		log.info(" ====> createSchema suc");
		
		List<StorageField> addFields = new ArrayList<StorageField>();
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.DATE)); Thread.sleep(1);
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.FLOAT)); Thread.sleep(1);
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.INT)); Thread.sleep(1);
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.LONG)); Thread.sleep(1);
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.LONG_TEXT)); Thread.sleep(1);
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.STRING)); Thread.sleep(1);
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.TEXT)); Thread.sleep(1);
		addFields.add(new StorageField("_field_" + System.currentTimeMillis(), StorageFieldType.ARRAY)); 

		Long id = IdWorker.getId();
		int rows = datacenterService.execute(dsId, dbId, Executor.insert(table).columns("id", "data").value(Values.value(id, buildData(addFields))));
		log.info(" ====> insert {}", rows);

		List<Executor> executors = new ArrayList<>();
		for(int i = 0; i < 10; i ++){
			executors.add(Executor.insert(table).columns("id", "data").value(Values.value(IdWorker.getId(), buildData(addFields))));
		}
		int[] rowsList = datacenterService.execute(dsId, dbId, executors);
		log.info(" ====> insert batch {}", rows);

		int affects = datacenterService.execute(dsId, dbId, Executor.update(table).set(Sets.setJsonB("data." + addFields.get(3).getName(), 999)).where(Conditions.equal("id", id)));
		log.info(" ====> updateValue {}", affects);

		List<Map<String, Object>> datas =  datacenterService.query(dsId, dbId, Query.select().from(table));
		log.info(" ====> select {}", JSON.toJSONString(datas));

		datas =  datacenterService.query(dsId, dbId, Query.select("count(*) as count").from(table));
		log.info(" ====> count {}", JSON.toJSONString(datas));

		Page<Map<String, Object>> pageDatas = datacenterService.page(dsId, dbId, Query.select().from(table).limit(5));
		log.info(" ====> page {}", JSON.toJSONString(pageDatas));

		pageDatas = datacenterService.page(dsId, dbId, Query.select().from(table).limit(11,  1));
		log.info(" ====> page limit 11, 1 {}", JSON.toJSONString(pageDatas));

		pageDatas = datacenterService.page(dsId, dbId, Query.select().from(table).limit(10,  1));
		log.info(" ====> page limit 10, 1 {}", JSON.toJSONString(pageDatas));
	}
	
	private Map<String, Object> buildData(List<StorageField> fields) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (StorageField field : fields) {
			Object value = null;
			switch (field.getType()) {
				case DATE:
					value = sdf.format(new Date());
					break;
				case FLOAT:
					value = Math.random() * 10;
					break;
				case INT:
					value = Double.valueOf(Math.random() * 10000).intValue();
					break;
				case LONG:
					value = System.currentTimeMillis();
					break;
				case LONG_TEXT:
					value = UuidUtils.generateUuid();
					break;
				case STRING:
					value = UuidUtils.generateUuid();
					break;
				case TEXT:
					value = UuidUtils.generateUuid();
					break;
				case ARRAY:
					value = new int[]{1, 2, 3};
					break;
				default:
					break;
			}
			data.put(field.getName(), value);
		}
		return data;
	}


	@Test
	public void testExecute() throws SQLException {
		Executor executor = Executor.update(new Table("_form_305000508681097216_1307984964377214977"))
				.set(Sets.setJsonB("data.test", "{\"status\": 6}"))
				.where(Conditions.equal("id", 1307984967489388560L));
		int affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);

		Map<String, Object> map = new HashMap<>();
		map.put("status", 1);
		executor = Executor.update(new Table("_form_305000508681097216_1307984964377214977"))
				.set(Sets.setJsonB("data.test", map))
				.where(Conditions.equal("id", 1307984967489388560L));
		affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);

		executor = Executor.update(new Table("_form_305000508681097216_1307984964377214977"))
				.set(Sets.setJsonB("data.test1", 2))
				.where(Conditions.equal("id", 1307984967489388560L));
		affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);

		executor = Executor.update(new Table("_form_305000508681097216_1307984964377214977"))
				.set(Sets.setJsonB("data", map))
				.where(Conditions.equal("id", 1307984967489388560L));
		affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);

		executor = Executor.update(new Table("_form_305000508681097216_1307984964377214977"))
				.set(Sets.setJsonB("data", null))
				.where(Conditions.equal("id", 1307984967489388560L));
		affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);
	}

	@Test
	public void testExecuteInsert() throws SQLException {
		Executor executor = Executor.insert(new Table("_form_305000508681097216_1307984964377214977"))
				.columns("id", "data")
				.values(Values.value(System.currentTimeMillis(), new HashMap<>()));
		int affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);
	}

	@Test
	public void testExecuteInsertBatch() throws SQLException {
		Executor executor = Executor.insert(new Table("_form_305000508681097216_1307984964377214977"))
				.columns("id", "data")
				.values(Values.value(System.currentTimeMillis() + 1, new HashMap<>()), Values.value(System.currentTimeMillis() + 10, new HashMap<>()));
		Executor executor1 = Executor.insert(new Table("_form_305000508681097216_1307984964377214977"))
				.columns("id", "data")
				.values(Values.value(System.currentTimeMillis() + 2, new HashMap<>()), Values.value(System.currentTimeMillis() + 11, new HashMap<>()));

		List<Executor> es = new ArrayList<>();
		es.add(executor);
		es.add(executor1);

		int[] affects = dataService.execute(dataSourceFactory.getJdbcTemplate(2L, "lesscode-datas-001"), es);
		log.info(" ====> executor batch {}", affects);
	}

	@Test
	public void testLike(){
		Query query = Query.select("count(*)").from(new Table("_form_305000508681097216_1307984964377214977"))
				.where(Conditions.like(SqlUtil.wrapperJsonColumn("_field_1600682891315"), "%二%"));
		List<Map<String, Object>> results = dataService.query(dataSourceFactory.getJdbcTemplate(2L, "lesscode-datas-001"), query);
		System.out.println(JSON.toJSONString(results));
	}

	@Test
	public void testLineFeed(){
		Map<String, Object> map = new HashMap<>();
		map.put("_field_1600682891315", "abc");
		map.put("_field_1600682891316", "abc\nefg");
		map.put("_field_1600682891317", "abc\nline");
		Executor executor = Executor.insert(new Table("_form_305000508681097216_1307984964377214977"))
				.columns("id", "data")
				.values(Values.value(System.currentTimeMillis(), map));
		int affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);


		Query query = Query.select("count(*)").from(new Table("_form_305000508681097216_1307984964377214977"))
				.where(Conditions.in(SqlUtil.wrapperJsonColumn("_field_1600682891316"), Arrays.asList("abc\nefg", "abc\"efg")));
		List<Map<String, Object>> results = dataService.query(dataSourceFactory.getJdbcTemplate(2L, "lesscode-datas-001"), query);
		System.out.println(JSON.toJSONString(results));

		query = Query.select("count(*)").from(new Table("_form_305000508681097216_1307984964377214977"))
				.where(Conditions.equal(SqlUtil.wrapperJsonColumn("_field_1600682891316"), "abc\nefg"));
		results = dataService.query(dataSourceFactory.getJdbcTemplate(2L, "lesscode-datas-001"), query);
		System.out.println(JSON.toJSONString(results));

	}

	@Test
	public void testId(){
		Query query = Query.select().from(new Table("_form_202505060582035456_1376478265937686529"))
				.where(Conditions.and(
						Conditions.equal("id", 1381440461826260995L),
						Conditions.equal(SqlUtil.wrapperJsonColumn("delFlag"), 2)
				));
		List<Map<String, Object>> results = dataService.query(dataSourceFactory.getJdbcTemplate(2L, "lesscode-test-datas-crm"), query);
		System.out.println(JSON.toJSONString(results));
	}

	@Test
	public void testUpdate1(){
		Map<String, Object> m = new HashMap<>();
		m.put("a", "2021-04-13 18:15:44");

		Executor executor = Executor.update(new Table("_form_202505060582035456_1380062539685269505"))
				.set(Sets.setJsonB("data", m))
				.where(Conditions.equal(SqlUtil.wrapperJsonColumn("delFlag"), 2));
		int affects = datacenterService.execute(dsId, dbId, executor);
		log.info(" ====> executor {}", affects);
	}
}
