package com.polaris.lesscode.dc.internal.service;

import com.alibaba.fastjson.JSON;
import com.polaris.lesscode.dc.internal.dsl.Executor;
import com.polaris.lesscode.dc.internal.dsl.Query;
import com.polaris.lesscode.dc.internal.dsl.Sql;
import com.polaris.lesscode.dc.internal.dsl.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * PG数据服务
 *
 * @author Nico
 * @date 2021/2/1 17:48
 */

@Slf4j
@Service("postgresDataService")
public class PostgresDataService implements DataService {

    @Override
    public int execute(JdbcTemplate jdbcTemplate, Executor executor) {
        return execute(jdbcTemplate, executor.toSql());
    }

    @Override
    public int execute(JdbcTemplate jdbcTemplate, String sql, Object[] args) {
        log.info("==> Preparing: {}", sql);
        log.info("==> Parameters: {}", args);
        return jdbcTemplate.update(sql, args);
    }

    @Override
    public int execute(JdbcTemplate jdbcTemplate, Sql sql) {
        return execute(jdbcTemplate, sql.getSql(), sql.getArgs().toArray());
    }

    @Override
    public void execute(JdbcTemplate jdbcTemplate, String... sqls) {
        StringBuilder executeSql = new StringBuilder();
        for(String sql: sqls){
            executeSql.append(sql);
            if(! sql.endsWith(";")){
                executeSql.append(";");
            }
        }
        if(executeSql.length() > 0){
            jdbcTemplate.execute(executeSql.toString());
        }
    }

    @Override
    public int[] execute(JdbcTemplate jdbcTemplate, List<Executor> executors) {
        List<Object> args = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        for(Executor executor: executors){
            Sql sql = executor.toSql();
            sqlBuilder.append(sql.getSql()).append(";");
            args.addAll(sql.getArgs());
        }

        log.info("==> Preparing: {}", sqlBuilder);
        log.info("==> Parameters: {}", args);
        ArgumentPreparedStatementSetter pss = new ArgumentPreparedStatementSetter(args.toArray(new Object[]{}));
        return jdbcTemplate.execute((PreparedStatementCreator) con -> con.prepareStatement(sqlBuilder.toString()), ps -> {
            try {
                if (pss != null) {
                    pss.setValues(ps);
                }
                List<Integer> rows = new ArrayList<>();

                ps.execute();
                while(ps.getUpdateCount() != -1){
                    rows.add(ps.getUpdateCount());
                    ps.getMoreResults();
                }
                int[] ints = new int[rows.size()];
                for(int i = 0; i < rows.size(); i ++){
                    ints[i] = rows.get(i);
                }
                return ints;
            }
            finally {
                ((ParameterDisposer) pss).cleanupParameters();
            }
        });
    }

    @Override
    public List<Map<String, Object>> query(JdbcTemplate jdbcTemplate, Query query) {
        Sql sql = query.toSql();

        log.info("==> Preparing: {}, Parameters:{}", sql.getSql(), sql.getArgs());
        List<Map<String, Object>> sqlResults = jdbcTemplate.queryForList(sql.getSql(), CollectionUtils.isEmpty(sql.getArgs()) ? new Object[]{} : sql.getArgs().toArray());
        return toDatas(sqlResults);
    }

    @Override
    public List<Map<String, Object>> query(JdbcTemplate jdbcTemplate, String sql, Object[] args) {
        log.info("==> Preparing: {}, Parameters:{}",sql, args);
        List<Map<String, Object>> sqlResults = jdbcTemplate.queryForList(sql, args);
        return toDatas(sqlResults);
    }

    private List<Map<String, Object>> toDatas(List<Map<String, Object>> sqlResults) {
        List<Map<String, Object>> datas = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sqlResults)) {
            sqlResults.stream().forEach(result -> {
                Map<String, Object> data = new LinkedHashMap<>();
                for (Map.Entry<String, Object> entry : result.entrySet()) {
                    if (entry.getValue() instanceof PGobject) {
                        PGobject pgObject = (PGobject) entry.getValue();
                        if ("jsonb".equals(pgObject.getType())) {
                            String jsonbValue = pgObject.getValue();
                            if (jsonbValue.startsWith("{")) {
                                // 2022-4-8 改为只展开最外层的data
                                if (entry.getKey().equals("data")) {
                                    Map<String, Object> jsonData = JSON.parseObject(pgObject.getValue(), Map.class);
                                    jsonData.remove("id");
                                    jsonData.remove("parent_id");
                                    data.putAll(jsonData);
                                } else {
                                    log.info("==> 未展开 [" + entry.getKey() + "]");
                                    data.put(entry.getKey(), JSON.parseObject(pgObject.getValue(), Map.class));
                                }
                            } else if (jsonbValue.startsWith("[")) {
                                List<Object> jsonData = JSON.parseArray(pgObject.getValue());
                                data.put(entry.getKey(), jsonData);
                            } else {
                                if (StringUtils.isNumeric(jsonbValue)) {
                                    data.put(entry.getKey(), new BigDecimal(jsonbValue));
                                } else if ("true".equals(jsonbValue)) {
                                    data.put(entry.getKey(), true);
                                } else if ("false".equals(jsonbValue)) {
                                    data.put(entry.getKey(), false);
                                } else {
                                    if (jsonbValue.length() >= 2 && jsonbValue.startsWith("\"") && jsonbValue.endsWith("\"")) {
                                        jsonbValue = jsonbValue.substring(1, jsonbValue.length() - 1);
                                    }
                                    data.put(entry.getKey(), jsonbValue);
                                }
                            }
                        }
                    } else if (!entry.getKey().equals(SqlUtil.COLLABORATORS)) {
                        if  (entry.getKey().equals(SqlUtil.APP_ID)
                                || entry.getKey().equals(SqlUtil.TABLE_ID)) {
                            data.put(entry.getKey(), entry.getValue().toString());
                        } else {
                            data.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
                datas.add(data);
            });
        }
        return datas;
    }
}
