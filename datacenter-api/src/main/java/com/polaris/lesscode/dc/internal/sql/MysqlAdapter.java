package com.polaris.lesscode.dc.internal.sql;

import com.alibaba.fastjson.JSON;
import com.polaris.lesscode.dc.internal.dsl.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class MysqlAdapter extends Adapter{

    private List<String> toColumns(List<SelectItem> selectItems){
        return selectItems.stream().map(SelectItem::toString).map(this::toColumn).collect(Collectors.toList());
    }

    private String toColumn(Expression e){
        return toColumn(e.toString());
    }

    private String toColumn(String c){
        return c;
    }

    private Table toTable(FromItem fromItem){
        net.sf.jsqlparser.schema.Table selectTable = (net.sf.jsqlparser.schema.Table) fromItem;
        return new Table(selectTable.getSchemaName(), selectTable.getName(), selectTable.getAlias().getName());
    }

    private Object toValue(Expression e, LinkedBlockingQueue<Object> args){
        if (e instanceof JdbcParameter){
            return args.poll();
        }else if (e instanceof LongValue){
            return ((LongValue) e).getValue();
        }else if (e instanceof DoubleValue){
            return ((DoubleValue) e).getValue();
        }else {
            return e.toString();
        }
    }

    private Condition toCondition(Expression expression, LinkedBlockingQueue<Object> args, boolean noPretreat){
        if (expression instanceof EqualsTo){
            return new Condition(Conditions.EQUAL, null, toColumn(((EqualsTo) expression).getLeftExpression()), null, null, null, toValue(((EqualsTo) expression).getRightExpression(), args), noPretreat);
        }else if (expression instanceof NotEqualsTo){
            return new Condition(Conditions.UN_EQUAL, null, toColumn(((NotEqualsTo) expression).getLeftExpression()), null, null, null, toValue(((NotEqualsTo) expression).getRightExpression(), args), noPretreat);
        }else if (expression instanceof LikeExpression){
            return new Condition(Conditions.LIKE, null, toColumn(((LikeExpression) expression).getLeftExpression()), null, null, null, toValue(((LikeExpression) expression).getRightExpression(), args), noPretreat);
        }else if (expression instanceof IsNullExpression){
            return new Condition(Conditions.IS_NULL, null, toColumn(((IsNullExpression) expression).getLeftExpression()), null, null, null, null, noPretreat);
        }else if (expression instanceof GreaterThan){
            return new Condition(Conditions.GT, null, toColumn(((GreaterThan) expression).getLeftExpression()), null, null, null, toValue(((GreaterThan) expression).getRightExpression(), args), noPretreat);
        }else if (expression instanceof GreaterThanEquals){
            return new Condition(Conditions.GTE, null, toColumn(((GreaterThanEquals) expression).getLeftExpression()), null, null, null, toValue(((GreaterThanEquals) expression).getRightExpression(), args), noPretreat);
        }else if (expression instanceof MinorThan){
            return new Condition(Conditions.LT, null, toColumn(((MinorThan) expression).getLeftExpression()), null, null, null, toValue(((MinorThan) expression).getRightExpression(), args), noPretreat);
        }else if (expression instanceof MinorThanEquals){
            return new Condition(Conditions.LTE, null, toColumn(((MinorThanEquals) expression).getLeftExpression()), null, null, null, toValue(((MinorThanEquals) expression).getRightExpression(), args), noPretreat);
        }else if (expression instanceof Between){
            return new Condition(Conditions.BETWEEN, null, toColumn(((Between) expression).getLeftExpression()), toValue(((Between) expression).getBetweenExpressionStart(), args), toValue(((Between) expression).getBetweenExpressionEnd(), args), null, null, noPretreat);
        }else if (expression instanceof InExpression){
            InExpression inExpression = (InExpression) expression;
            ExpressionList expressionList = (ExpressionList) inExpression.getRightItemsList();
            List<Object> objects = new ArrayList<>();
            for (Expression e: expressionList.getExpressions()){
                objects.add(toValue(e, args));
            }
            return new Condition(Conditions.IN, null, toColumn(inExpression.getLeftExpression()), null, null, objects.toArray(), null, noPretreat);
        }else if (expression instanceof AndExpression){
            return Conditions.and(toCondition(((AndExpression) expression).getLeftExpression(), args, noPretreat), toCondition(((AndExpression) expression).getRightExpression(), args, noPretreat));
        }else if (expression instanceof OrExpression){
            return Conditions.and(toCondition(((OrExpression) expression).getLeftExpression(), args, noPretreat), toCondition(((OrExpression) expression).getRightExpression(), args, noPretreat));
        }
        return null;
    }

    @Override
    public Query query(String sql, Object... args) throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse(sql);

        PlainSelect selectBody = (PlainSelect) select.getSelectBody();
        net.sf.jsqlparser.schema.Table selectTable = (net.sf.jsqlparser.schema.Table) selectBody.getFromItem();

        LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<>(Arrays.asList(args));

        Table table = toTable(selectBody.getFromItem());
        if (CollectionUtils.isNotEmpty(selectBody.getJoins())){
            Table temp = table;
            for (Join join: selectBody.getJoins()){
                Table rightTable = toTable(join.getRightItem());;
                if (join.isLeft()){
                    temp.leftJoin(rightTable).on(toCondition(join.getOnExpression(), queue, true));
                }else if (join.isRight()){
                    temp.rightJoin(rightTable).on(toCondition(join.getOnExpression(), queue, true));
                }else if (join.isFull()){
                    temp.fullOuterJoin(rightTable).on(toCondition(join.getOnExpression(), queue, true));
                }
                temp = rightTable;
            }
        }
        return Query.select(toColumns(selectBody.getSelectItems())).from(table).where(toCondition(selectBody.getWhere(), queue, false));
    }

    @Override
    public Executor execute(String sql, Object... args) {
        return null;
    }

    public static void main(String[] args) throws JSQLParserException {
        MysqlAdapter adapter = new MysqlAdapter();
        Query query = adapter.query("select a,b,c from user u left join org o on u.id = o.id where a = ? and id in (?,?,?) and b > ?",1,2,3,4,5);
        Sql sql = query.toSql();
        System.out.println(sql.getSql());
        System.out.println(sql.getArgs());
    }
}
