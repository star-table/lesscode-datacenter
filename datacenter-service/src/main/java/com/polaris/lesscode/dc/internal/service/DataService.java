package com.polaris.lesscode.dc.internal.service;

import com.polaris.lesscode.dc.internal.dsl.Executor;
import com.polaris.lesscode.dc.internal.dsl.Query;
import com.polaris.lesscode.dc.internal.dsl.Sql;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 数据服务
 *
 * @author Nico
 * @date 2021/2/1 17:46
 */
public interface DataService {

    int execute(JdbcTemplate jdbcTemplate, Executor executor);

    int execute(JdbcTemplate jdbcTemplate, String sql, Object[] args);

    int execute(JdbcTemplate jdbcTemplate, Sql sql);

    void execute(JdbcTemplate jdbcTemplate, String... sql);

    int[] execute(JdbcTemplate jdbcTemplate, List<Executor> executors);

    List<Map<String, Object>> query(JdbcTemplate jdbcTemplate, Query query);

    List<Map<String, Object>> query(JdbcTemplate jdbcTemplate, String sql, Object[] args);
}
