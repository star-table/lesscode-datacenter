package com.polaris.lesscode.dc.internal.req;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import com.polaris.lesscode.dc.internal.dsl.Order;
import lombok.Data;

import java.util.List;

/**
 * @author: Liu.B.J
 * @date: 2020/12/28 11:55
 * @description:
 */
@Data
public class AllValueReq {

    private List<String> columns;

    private Condition condition;

    private List<Order> orders;

    private List<String> groups;

}
