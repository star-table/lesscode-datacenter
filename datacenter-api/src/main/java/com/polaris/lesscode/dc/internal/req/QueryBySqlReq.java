package com.polaris.lesscode.dc.internal.req;

import com.polaris.lesscode.dc.internal.dsl.Condition;
import lombok.Data;

@Data
public class QueryBySqlReq {
    String sql;
    Condition condition;
    String whereSqlPlaceholder;
}
