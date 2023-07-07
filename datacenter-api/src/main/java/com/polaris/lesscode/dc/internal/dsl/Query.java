package com.polaris.lesscode.dc.internal.dsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.polaris.lesscode.dc.internal.enums.ResultCode;
import com.polaris.lesscode.exception.BusinessException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Odd;
import org.springframework.util.CollectionUtils;

import lombok.Data;

/**
 * Query Model
 * 
 * @author nico
 * @date 2020年7月16日
 */
@Data
public class Query {
	
	/**
	 * 集合名称
	 */
	protected List<Table> from;
	
	/**
	 * 条件
	 */
	protected Condition condition;
	
	/**
	 * skip
	 */
	protected Integer offset;
	
	/**
	 * limit
	 */
	protected Integer limit;
	
	/**
	 * 要查询的字段列表，如果为空默认查所有
	 */
	protected List<String> columns;
	
	/**
	 * 排序
	 */
	protected List<Order> orders;
	
	/**
	 * 分组字段
	 */
	protected List<String> groups;
	
	/**
	 * Unions
	 */
	protected List<Union> unions;
	
	public Query() {
	}
	
	/**
	 * select构造
	 * 
	 * @return
	 */
	public static Query select() {
		return new Query();
	}

	/**
	 * select 有参构造
	 * 
	 * @param columns 要查询的字段列表
	 * @return {@link Query}
	 */
	public static Query select(String... columns) {
		Query query = new Query();
		if(! ArrayUtils.isEmpty(columns)) {
			List<String> list = new ArrayList<>();
			for(String column: columns) {
				list.add(column);
			}
			query.setColumns(list);
		}
		return query;
	}
	
	public static Query select(List<String> columns) {
		if(! CollectionUtils.isEmpty(columns)) {
			return select(columns.toArray(new String[] {}));
		}
		return select();
	}
	
	/**
	 * 指定要查询的集合
	 *
	 * @return
	 */
	public Query from(Table table) {
		if(from == null){
			from = new ArrayList<>();
		}
		this.from.add(table);
		return this;
	}

	public Query from(List<Table> tables){
		this.from = tables;
		return this;
	}
	public Query from(Table... tables){
		this.from = Arrays.asList(tables);
		return this;
	}

	
	/**
	 * 指定条件
	 * 
	 * @param condition 条件
	 * @return
	 */
	public Query where(Condition condition) {
		this.condition = condition;
		return this;
	}
	
	/**
	 * limit
	 * 
	 * @param limit
	 * @return
	 */
	public Query limit(Integer limit) {
		this.limit = limit;
		if(this.offset == null){
			this.offset = 0;
		}
		return this;
	}

	public Query limit(Integer offset, Integer limit) {
		this.offset = offset;
		return limit(limit);
	}

	/**
	 * offset
	 *
	 * @Author Nico
	 * @Date 2021/2/20 11:48
	 **/
	public Query offset(Integer offset){
		this.offset = offset;
		return this;
	}

	/**
	 * 增加排序条件
	 * 
	 * @param column
	 * @param isAsc
	 * @return
	 */
	public Query order(String column, boolean isAsc) {
		if(orders == null) {
			orders = new ArrayList<Order>();
		}
		orders.add(new Order(column, isAsc));
		return this;
	}

	public Query order(String column, boolean isAsc, boolean isIgnoreNullsOrder) {
		if(orders == null) {
			orders = new ArrayList<Order>();
		}
		orders.add(new Order(column, isAsc, isIgnoreNullsOrder));
		return this;
	}
	
	public Query orders(List<Order> orders) {
		this.orders = orders;
		return this;
	}
	
	public Query group(String column) {
		if(groups == null) {
			groups = new ArrayList<>();
		}
		groups.add(column);
		return this;
	}
	
	public Query group(String... columns) {
		if(groups == null) {
			groups = new ArrayList<>();
		}
		for(String column: columns) {
			groups.add(column);
		}
		return this;
	}
	
	public Query group(List<String> columns) {
		if(! CollectionUtils.isEmpty(columns)) {
			return group(columns.toArray(new String[] {}));
		}
		return group();
	}
	
	public Query union(Query query) {
		if(unions == null) {
			unions = new ArrayList<>();
		}
		unions.add(new Union("union", query));
		return this;
	}
	
	public Query unionAll(Query query) {
		if(unions == null) {
			unions = new ArrayList<>();
		}
		unions.add(new Union("union all", query));
		return this;
	}
	
	public Query asc(String column) {
		order(column, true);
		return this;
	}
	
	public Query desc(String column) {
		order(column, false);
		return this;
	}
	
	public boolean hasGroup() {
		return ! CollectionUtils.isEmpty(groups);
	}

	public boolean hasAggregation() {
		if (!CollectionUtils.isEmpty(columns)) {
			for (String column : columns) {
				if (column.contains("count")
						|| column.contains("sum")
						|| column.contains("avg")
						|| column.contains("min")
						|| column.contains("max")) {
					return true;
				}
			}
		}
		return false;
	}

	public Sql toSql() {
		List<Object> args = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		
		builder.append("select ");
		if(CollectionUtils.isEmpty(columns)) {
			builder.append("* ");
		}else {
			builder.append(StringUtils.join(columns, ','));
		}
		if(CollectionUtils.isEmpty(from)){
			throw new BusinessException(ResultCode.QUERY_NOT_FROM);
		}
		builder.append(" from ");
		int i = 0;
		for(Table t: from){
			builder.append(t.toSql(args));
			if(i ++ < from.size() - 1){
				builder.append(",");
			}
		}
		if(condition != null) {
			String condSql = condition.toSql(args);
			if(StringUtils.isNotBlank(condSql) && ! "()".equals(condSql.trim())) {
				builder.append(" where ");
				builder.append(condSql);	
			}
		}
		if(! CollectionUtils.isEmpty(groups)) {
			builder.append(" group by ");
			builder.append(StringUtils.join(groups, ','));
		}
		if(! CollectionUtils.isEmpty(orders)) {
			builder.append(" order by ");
			List<String> orderSqls = new ArrayList<>();
			for (Order o: orders){
				orderSqls.add(o.toSql(args));
			}
			builder.append(StringUtils.join(orderSqls, ','));
		}
		if(limit != null && offset != null && limit > 0 && offset >= 0) {
//			builder.append(" limit ");
//			builder.append(offset + "," + limit);
			builder.append(" limit " + limit + " offset " + offset);
		}
		
		if(! CollectionUtils.isEmpty(unions)) {
			StringBuilder unionBuilder = new StringBuilder();
			List<Object> unionArgs = new ArrayList<>();
			
			unionBuilder.append("(" + builder.toString() + ")");
			if(! CollectionUtils.isEmpty(args)) {
				unionArgs.addAll(args);	
			}
			for(Union union: unions) {
				unionBuilder.append(" " + union.getUnionType() +  " ");
				Sql subSql = union.getQuery().toSql();
				unionBuilder.append("(" + subSql.getSql() + ")");
				if(! CollectionUtils.isEmpty(subSql.getArgs())) {
					unionArgs.addAll(subSql.getArgs());	
				}
			}
			builder = unionBuilder;
			args = unionArgs;
		}
		return new Sql(builder.toString(), args);
	}
	
	@Override
	public String toString() {
		return toSql().getSql();
	}

	public Query copy(){
		Query newQuery = new Query();
		newQuery.setLimit(this.getLimit());
		newQuery.setOffset(this.getOffset());
		newQuery.setOrders(this.getOrders());
		newQuery.setColumns(this.getColumns());
		newQuery.setFrom(this.getFrom());
		newQuery.setGroups(this.getGroups());
		newQuery.setUnions(this.getUnions());
		newQuery.setCondition(this.getCondition());
		return newQuery;
	}

}
