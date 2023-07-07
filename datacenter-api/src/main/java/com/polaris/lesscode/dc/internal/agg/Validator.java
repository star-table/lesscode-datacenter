package com.polaris.lesscode.dc.internal.agg;

import lombok.Data;

import java.util.List;

/**
 * @auther: yuxiaoyue
 * @version: 1.0
 * @since: 2020/9/30
 */
@Data
public class Validator {
    private String label; //不满足时的显示文本
    private String expression; //具体条件表达式
}
