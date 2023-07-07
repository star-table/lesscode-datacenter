package com.polaris.lesscode.dc.internal.agg;

import lombok.Data;

import java.util.List;

/**
 * @auther: yuxiaoyue
 * @version: 1.0
 * @since: 2020/9/30
 */
@Data
public class Formula {
    private String label; //公式名称
    private String expression; //公式具体指标（包含加减括号）

    //display config
//    private Boolean ifDisplayThousandth; //是否显示千分符
//    private Boolean ifDisplayPercentage; //是否显示百分比
//    private Boolean ifDisplayFractionalPart; //是否显示小数部分
//    private Integer fractionalPartLength; //显示几位小数


}
