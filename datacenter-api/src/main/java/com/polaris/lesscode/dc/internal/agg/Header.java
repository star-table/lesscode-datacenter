package com.polaris.lesscode.dc.internal.agg;

import lombok.Data;

import java.util.List;

/**
 * @auther: yuxiaoyue
 * @version: 1.0
 * @since: 2020/9/30
 */
@Data
public class Header {
    private String label; //公式名称
    private List<String> groupByKeys; //各表分组字段
}
