package com.polaris.lesscode.dc;

import java.util.HashMap;
import java.util.Map;

import org.jooq.Field;
import org.jooq.UpdateConditionStep;
import org.jooq.impl.DSL;
import org.junit.Test;

import com.polaris.lesscode.dc.internal.dsl.SqlUtil;

public class JooqTest {

	@Test
	public void testJooq() {
		Map<Field<?>, Object> conds = new HashMap<>();
		conds.put(DSL.field("b"), 3);
		UpdateConditionStep step = DSL.update(DSL.table("test.test")).set(DSL.field("a"), 1).where(DSL.condition(conds));
		System.out.println(step.getSQL());
		System.out.println(step.getBindValues());
	}
	
	@Test
	public void testJooq1Test() {
		Map<Field<?>, Object> conds = new HashMap<>();
		conds.put(DSL.field("id"), 2);
		
		//DSL.field("(jsonb_set(\"data\"::jsonb,'{A}','?'::jsonb))", JSONB.class, 8)
		UpdateConditionStep<?> step = DSL.update(DSL.table(SqlUtil.wrapperDatabaseTable("data", "table")))
//			.set(DSL.field("data"), DSL.field("(jsonb_set(\"data\"::jsonb,'{A}','?'::jsonb))", 5))
//			.set(DSL.field("data"), 8)
			
			.set(DSL.row(DSL.field("data")), DSL.row(DSL.field("(jsonb_set(\"data\"::jsonb,'{A}','?'::jsonb))", 1)))
			
			.where(DSL.condition(conds));
		System.out.println(step.getSQL());
		System.out.println(step.getBindValues());
		System.out.println(step.getParams());
	}
}
