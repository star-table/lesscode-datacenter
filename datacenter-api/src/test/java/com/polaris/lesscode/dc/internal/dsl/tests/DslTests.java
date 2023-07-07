package com.polaris.lesscode.dc.internal.dsl.tests;

import com.alibaba.fastjson.JSON;
import com.polaris.lesscode.dc.internal.dsl.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DslTests {

    @Test
    public void testCondition() {
        Condition cond = Conditions.and(Conditions.equal("a", "1"), Conditions.equal("b", 2), Conditions.allIn("c", "1", "2", "b"), Conditions.allIn("d", 4, 5, 6));
        Sql sql = Query.select().from(new Table("test")).where(cond).toSql();
        System.out.println(sql.getSql());
        System.out.println(sql.getArgs());
    }
//
//    @Test
//    public void testIn() {
//        Condition cond = Conditions.in("c", "1", "2", "b");
//        Sql sql = Query.select().from(new Table("test")).where(cond).toSql();
//        System.out.println(sql.getSql());
//        System.out.println(sql.getArgs());
//    }
//
//    @Test
//    public void testQuestionMark() {
//        Condition cond = Conditions.in("c", "1", "2", "b");
//        Sql sql = Query.select().from(new Table("test")).where(cond).toSql();
//        System.out.println(sql.getSql());
//        System.out.println(sql.getArgs());
//    }
//
//    @Test
//    public void testMultiTables() {
//        Condition cond = Conditions.in("c", "1", "2", "b");
//        Sql sql = Query.select()
//                .from(new Table(null,"_form_2578_1525670427878232064","t1")
//                                .leftJoin(new Table(null,"_form_2578_1525670427878232064", "t2"))
//                                .on(Conditions.equal(SqlUtil.wrapperAliasJsonColumn("t1","title"), SqlUtil.wrapperAliasJsonColumn("t2", "title")))
////                                .leftJoin(new Table(null,"_form_2578_1525670427878232064", "t3"))
////                                .on(Conditions.equal("t1.a", "t3.b"))
//                )
//                .where(cond)
//                .toSql();
//        System.out.println(sql.getSql());
//        System.out.println(sql.getArgs());
//    }
//
//    @Test
//    public void testValuesIn() {
//        Condition cond = Conditions.valuesIn(SqlUtil.wrapperJsonColumn("field_b"), "1", "2", "b");
//        Condition cond1 = new Condition();
//        Condition rootCond = new Condition();
//        rootCond.setType("OR");
//        rootCond.setConds(new Condition[]{cond, cond1});
//        Sql sql = Query.select()
//                .from(new Table("test")).where(rootCond).toSql();
//        System.out.println(sql.getSql());
//        System.out.println(sql.getArgs());
//
//
//        Query.select().from(new Table("xxx")).where(Conditions.equal("a", 1));
//        Query.select().from(new Table("xxx")).where(Conditions.and(
//                Conditions.equal("a", 1),
//                Conditions.equal("b", 2)
//        )).toSql();
//    }
//
//    @Test
//    public void testUpdate(){
////        Executor updated = Executor.update(new Table("user"))
////                .set(Sets.setJsonB("data.a", 1))
////                .where(Conditions.equal(SqlUtil.wrapperJsonColumn("efg"), "job"));
//////        System.out.println(JSON.toJSONString(updated));
////        System.out.println(updated.toSql().getSql());
//
////        ArrayList<String> ids = new ArrayList<>();
////        ids.add("123");
////        ids.add("234");
//
//        Executor updated = Executor.update(new Table("_form_2384_1503378501179375617"))
//                .set(Sets.setJsonBArrayWithoutPretreat("data.a.b", "123", Set.ACTION_JSON_ARRAY_DEL_ITEM))
//                .where(Conditions.equal("id", 1));
//        System.out.println(JSON.toJSONString(updated));
//        System.out.println(updated.toSql().getSql());
//    }
//
//    @Test
//    public void testAlter(){
//        Alter alter = Alter.table(new Table("test"))
//                .actions(AlterAction.addColumn("id", "abababa"))
//                .actions(AlterAction.dropColumn("id", ""));
//        System.out.println(alter.toSql().getSql());
//    }
//
//    @Test
//    public void testMultiSelectEqual() {
//        Map<Object,Object> temp = new HashMap<>();
//        temp.put("590001632", "2");
//        temp.put("659499861", "a");
//        temp.put("660015207", "11");
//        ColumnInfo fromColumn = new ColumnInfo("t1","myString", "string", temp);
//        ColumnInfo toColumn = new ColumnInfo("t2","myMultiSelect", "multi_select", temp);
//
//        Condition condition = Conditions.getStringEqualMultiSelect(fromColumn, toColumn);
//        System.out.println(condition.toSql(new ArrayList<>()));
//    }
//
//    @Test
//    public void getSelectEqual() {
//        Map<Object,Object> temp = new HashMap<>();
//        temp.put("590001632", "2");
//        temp.put("659499861", "a");
//        temp.put("660015207", "11");
//        ColumnInfo fromColumn = new ColumnInfo("t1","myString", "string", temp);
//        ColumnInfo toColumn = new ColumnInfo("t2","mySelect", "select", temp);
//
//        Condition condition = Conditions.getStringEqualSelect(fromColumn, toColumn);
//        System.out.println(condition.toSql(new ArrayList<>()));
//    }
//
//    @Test
//    public void getSelectEqualMultiSelect() {
//        Map<Object,Object> temp = new HashMap<>();
//        temp.put("590001632", "2");
//        temp.put("659499861", "a");
//        temp.put("660015207", "11");
//
//        ColumnInfo fromColumn = new ColumnInfo("t1","mySelect", "select", temp);
//        ColumnInfo toColumn = new ColumnInfo("t2","myMultiSelect", "multi_select", temp);
//
//        Condition condition = Conditions.getSelectEqualMultiSelect(fromColumn, toColumn);
//        System.out.println(condition.toSql(new ArrayList<>()));
//    }
}
