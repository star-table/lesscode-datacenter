package com.polaris.lesscode.dc.internal.sql;

import com.polaris.lesscode.dc.internal.dsl.Executor;
import com.polaris.lesscode.dc.internal.dsl.Query;
import net.sf.jsqlparser.JSQLParserException;

public abstract class Adapter {

    public abstract Query query(String sql, Object ...args) throws JSQLParserException;

    public abstract Executor execute(String sql, Object ...args);

}
