package com.polaris.lesscode.dc.internal.dsl;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class Order {

	private String column;
	
	private boolean isAsc;

	private boolean isIgnoreNullsOrder;

	private List<Object> args;

	public Order() {
		super();
	}

	public Order(String column, boolean isAsc) {
		this.column = column;
		this.isAsc = isAsc;
		this.isIgnoreNullsOrder = false;
	}

	public Order(String column, boolean isAsc, boolean isIgnoreNullsOrder) {
		this.column = column;
		this.isAsc = isAsc;
		this.isIgnoreNullsOrder = isIgnoreNullsOrder;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

	public boolean isIgnoreNullsOrder() {
		return isIgnoreNullsOrder;
	}

	public void setIgnoreNullsOrder(boolean isIgnoreNullsOrder) {
		this.isIgnoreNullsOrder = isIgnoreNullsOrder;
	}
	
	public String toSql(List<Object> args){
		if (CollectionUtils.isNotEmpty(this.args)){
			args.addAll(this.args);
		}
		// nulls的顺序对性能影响很大，调用层根据场景可以在筛选时排除null来规避order的损耗
		if (!this.isIgnoreNullsOrder) {
			return column + (isAsc ? " asc nulls first" : " desc nulls last");
		} else {
			return column + (isAsc ? " asc" : " desc");
		}
	}
	
}
