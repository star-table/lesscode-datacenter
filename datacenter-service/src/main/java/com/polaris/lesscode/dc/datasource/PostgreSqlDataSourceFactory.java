package com.polaris.lesscode.dc.datasource;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.client.utils.TemplateUtils;
import com.polaris.lesscode.dc.consts.PostgreSqlTemplate;
import com.polaris.lesscode.dc.entity.DataSource;
import com.polaris.lesscode.dc.mapper.DataSourceMapper;

@Component
public class PostgreSqlDataSourceFactory {

	@Autowired
	private DataSourceMapper dataSourceMapper;
	
	@Autowired
	private DataSourceConfig dataSourceConfig;
	
	private final static Map<String, JdbcTemplate> JTS = new ConcurrentHashMap<>();
	
	public JdbcTemplate getJdbcTemplate(Long dsId, String database) {
		String key = key(dsId, database);
		JdbcTemplate jt = JTS.get(key);
		if(jt == null) {
			synchronized (this) {
				jt = JTS.get(key);
				if(jt == null) {
					DruidDataSource ds = getDataSource(dsId, database);
					if(ds != null) {
						jt = new JdbcTemplate(ds);
						JTS.put(key, jt);
					}
				}
			}
		}
		return jt;
	}
	
	private DruidDataSource getDataSource(Long dsId, String database) {
		DataSource datasource = dataSourceMapper.get(dsId);
		if(datasource != null) {

			Properties properties = new Properties();
			properties.put("database", database);
			String url = PostgreSqlTemplate.replacePlaceholders(datasource.getUrl(), properties);
			
			DruidDataSource instance = new DruidDataSource();
			instance.setDriverClassName(dataSourceConfig.getDriverClassName());
            instance.setUrl(url);
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
	
	private String key(Long dsId, String database) {
		return dsId + ":" + database;
	}
}
