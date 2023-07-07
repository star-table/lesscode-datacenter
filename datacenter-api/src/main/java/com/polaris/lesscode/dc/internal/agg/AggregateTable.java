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
public class AggregateTable {
    private String name;

    private List<String> groupedKeys;

    private List<String> indices;

}
