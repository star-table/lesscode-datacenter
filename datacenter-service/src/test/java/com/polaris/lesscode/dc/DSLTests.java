package com.polaris.lesscode.dc;

import com.polaris.lesscode.dc.internal.dsl.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
 * @auther: yuxiaoyue
 * @version: 1.0
 * @since: 2020/9/27
 */
public class DSLTests {

    @Test
    public void testDslJoin(){
        Query query1 = Query.select().from(new Table("test","user", "u")
                .fullOuterJoin(new Table("test","class", "c")
                ).on(Conditions.equalNoPretreat("u.id", "c.uid")))
                .where(Conditions.valuesIn("u.is_delete", 2, 3));
//
//        Query query2 = Query.select().from(new Table("test","user", "u")
//                .fullOuterJoin(new Table("test","class", "c")
//                ).on(Conditions.equalNoPretreat("u.id", "c.uid")))
//                .where(Conditions.equal("u.is_delete", 2));
//
//        Query query3 = Query.select().from(new Table(query1, "q1").fullOuterJoin(new Table(query2, "q2")).on(Conditions.equalNoPretreat("q1.id", "q2.id")));
        Sql sql = query1.toSql();
        System.out.println(sql.getSql());
        System.out.println(sql.getArgs());

//        Map<String, Map<String, Object>> value = new HashMap<>();
//        Map<String, Object> doc = new HashMap<>();
//        doc.put("id", "123");
//        doc.put("name", "名字");
//        doc.put("type", 3);
//        value.put("123", doc);
//        Set s = Sets.setJsonB("data.document", value);
//        List<Object> args = new ArrayList<>();
//        System.out.println(s.toSqlJsonMapSet(args));
//        System.out.println(args);
    }
}