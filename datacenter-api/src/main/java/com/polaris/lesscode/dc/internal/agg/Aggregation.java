package com.polaris.lesscode.dc.internal.agg;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @auther: yuxiaoyue
 * @version: 1.0
 * @since: 2020/10/9
 */
@Data
public class Aggregation {
    private String name; //聚合表名称

    private List<AggregateTable> tables; //来源表

    private Map<String, Map<String, String>> relations;

    private List<Formula> formulars; //指标

    private List<Validator> validators; //数据校验

    private String config; //前端渲染信息
}
