package com.polaris.lesscode.dc.internal.agg;

import lombok.Data;

import java.util.List;

/**
 * @auther: yuxiaoyue
 * @version: 1.0
 * @since: 2020/9/30
 */
@Data
public class SourceTable {
    private String table; //来源表
    private List<String> relatedColumns; //该表下的关联字段

}
