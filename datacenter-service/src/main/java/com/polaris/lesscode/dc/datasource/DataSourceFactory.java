package com.polaris.lesscode.dc.datasource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.polaris.lesscode.dc.entity.DataSource;
import com.polaris.lesscode.dc.mapper.DataSourceMapper;

@Component
public class DataSourceFactory {

	@Autowired
	private DataSourceMapper dataSourceMapper;
	
	@Autowired
	private DataSourceConfig dataSourceConfig;
	
	private final static Map<Long, JdbcTemplate> JTS = new ConcurrentHashMap<>();
	
	public JdbcTemplate getJdbcTemplate(Long dsId) {
		JdbcTemplate jt = JTS.get(dsId);
		if(jt == null) {
			synchronized (this) {
				jt = JTS.get(dsId);
				if(jt == null) {
					DruidDataSource ds = getDataSource(dsId);
					if(ds != null) {
						jt = new JdbcTemplate(ds);
						JTS.put(dsId, jt);
					}
				}
			}
		}
		return jt;
	}
	
	private DruidDataSource getDataSource(Long dsId) {
		DataSource datasource = dataSourceMapper.get(dsId);
		if(datasource != null) {
			DruidDataSource instance = new DruidDataSource();
			instance.setDriverClassName(dataSourceConfig.getDriverClassName());
            instance.setUrl(datasource.getUrl());
            instance.setUsername(datasource.getUser());
            instance.setPassword(datasource.getPassword());
            instance.setInitialSize(dataSourceConfig.getInitialSize());
            instance.setMinIdle(dataSourceConfig.getMinIdle());
            instance.setMaxActive(dataSourceConfig.getMaxActive());
            instance.setMaxWait(dataSourceConfig.getMaxWait());
            instance.setTimeBetweenEvictionRunsMillis(dataSourceConfig.getTimeBetweenEvictionRunsMillis());
            instance.setMinEvictableIdleTimeMillis(dataSourceConfig.getMinEvictableIdleTimeMillis());
            instance.setTestWhileIdle(false);
            return instance;
		}
		return null;
	}
}
