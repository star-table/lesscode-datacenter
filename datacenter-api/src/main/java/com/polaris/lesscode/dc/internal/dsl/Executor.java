package com.polaris.lesscode.dc.internal.dsl;

import com.polaris.lesscode.exception.BusinessException;
import com.polaris.lesscode.util.GsonUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.polaris.lesscode.dc.internal.enums.ResultCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Executor Model
 * 
 * @author nico
 * @date 2021年2月1日
 */
@Data
public class Executor {

	/**
	 * 操作类型, 1 update, 2 delete, 3 insert
	 **/
	private int type;

	/**
	 * 集合名称
	 */
	private Table table;

	/**
	 * 集合名称
	 */
	protected List<Table> from;

	/**
	 * 条件
	 */
	private Condition condition;

	/**
	 * 设置条件
	 **/
	private List<Set> sets;

	/**
	 * 设置条件(RAW SET SQL)
	 **/
	private List<String> rawSets;

	/**
	 * 插入的字段
	 **/
	private List<String> columns;

	/**
	 * 插入的值
	 **/
	private List<Value> values;

	private Executor() { }

	private Executor(int type){
		this.type = type;
	}
	
	/**
	 * delete
	 **/
	public static Executor delete(Table table) {
		Executor e = new Executor(2);
		e.table = table;
		return e;
	}

	/**
	 * update
	 **/
	public static Executor update(Table table) {
		Executor e = new Executor(1);
		e.sets = new ArrayList<>();
		e.table = table;
		return e;
	}

	/**
	 * update
	 **/
	public static Executor insert(Table table) {
		Executor e = new Executor(3);
		e.table = table;
		return e;
	}

	/**
	 * from
	 **/
	public Executor from(Table table) {
		if(from == null){
			from = new ArrayList<>();
		}
		this.from.add(table);
		return this;
	}

	public Executor from(List<Table> tables){
		this.from = tables;
		return this;
	}
	public Executor from(Table... tables){
		this.from = Arrays.asList(tables);
		return this;
	}

	/**
	 * 指定条件
	 * 
	 * @param condition 条件
	 * @return
	 */
	public Executor where(Condition condition) {
		this.condition = condition;
		return this;
	}

	public Executor set(Set set){
		this.sets.add(set);
		return this;
	}

	public Executor sets(List<Set> sets){
		this.sets.addAll(sets);
		return this;
	}

	public Executor columns(List<String> columns){
		this.columns = columns;
		return this;
	}

	public Executor columns(String ...columns){
		this.columns = Arrays.asList(columns);
		return this;
	}

	public Executor values(List<Value> values){
		this.values = values;
		return this;
	}

	public final Executor values(Value... values){
		this.values = Arrays.asList(values);
		return this;
	}

	public Executor value(Value value){
		if (this.values == null){
			this.values = new ArrayList<>();
		}
		this.values.add(value);
		return this;
	}

	public Sql toSql() {
		List<Object> args = new ArrayList<>();
		StringBuilder builder = new StringBuilder();

		if(type == 1){
			builder.append("update ");
		}else if(type == 2){
			builder.append("delete from ");
		}else if(type == 3){
			builder.append("insert into ");
		}else{
			throw new BusinessException(ResultCode.INVALID_EXECUTOR_TYPE);
		}

		builder.append(table.toSql(args));
		if(type == 1){
			appendSet(builder, args);
			if(CollectionUtils.isNotEmpty(from)){
				builder.append(" from ");
				int i = 0;
				for(Table t: from){
					builder.append(t.toSql(args));
					if(i ++ < from.size() - 1){
						builder.append(",");
					}
				}
				builder.append(" ");
			}
		}
		if(type == 1 || type == 2){
			appendWhere(builder, args);
		}
		if(type == 3){
			appendInsert(builder, args);
		}
		return new Sql(builder.toString(), args);
	}

	public void appendInsert(StringBuilder builder, List<Object> args){
		if(CollectionUtils.isNotEmpty(this.columns)){
			String columnsStr = StringUtils.join(this.columns.stream().map(c -> "\"" + c + "\"").toArray(), ",");
			builder.append("(").append(columnsStr).append(") ");
		}
		builder.append("values ");
		if(CollectionUtils.isNotEmpty(this.values)){
			int i = 0;
			for(Value value: values){
				String valueStr = StringUtils.join(value.getDatas().stream().map(v -> {
					if(v instanceof Map || v instanceof Collection){
						v = GsonUtils.toJson(v);
						args.add(v);
						return "?";
					}else{
						args.add(v);
						return "?";
					}
				}).toArray(), ",");
				builder.append("(").append(valueStr).append(")");
				if(i ++ < values.size() - 1){
					builder.append(",");
				}
			}
			builder.append(";");
		}else{
			throw new BusinessException(ResultCode.INVALID_EXECUTOR_VALUES_IS_EMPTY);
		}
	}

	public void appendSet(StringBuilder builder, List<Object> args){
		builder.append(" set ");
		int i = 0;
		if(CollectionUtils.isNotEmpty(sets)){
			for(Set set: sets){
				builder.append(set.toSql(args));
				if(i++ < sets.size() - 1){
					builder.append(",");
				}
			}
		}else{
			throw new BusinessException(ResultCode.INVALID_EXECUTOR_SETS_IS_EMPTY);
		}
	}

	public void appendWhere(StringBuilder builder, List<Object> args){
		if(condition != null) {
			String condSql = condition.toSql(args);
			if(StringUtils.isNotBlank(condSql) && ! "()".equals(condSql.trim())) {
				builder.append(" where ");
				builder.append(condSql);
			}
		}
	}
	
	@Override
	public String toString() {
		return toSql().getSql();
	}


}
