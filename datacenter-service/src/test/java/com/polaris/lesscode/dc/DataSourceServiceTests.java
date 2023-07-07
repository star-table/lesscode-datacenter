package com.polaris.lesscode.dc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.polaris.lesscode.dc.BaseTest;
import com.polaris.lesscode.dc.mapper.DataSourceMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceServiceTests extends BaseTest {

	@Autowired
	private DataSourceMapper dataSourceMapper;
	
	@Test
	public void testDatasourceGet() {
		System.out.println(dataSourceMapper.get(1L));
		System.out.println(dataSourceMapper.get(2L));
	}
	
}
