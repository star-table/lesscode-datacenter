package com.polaris.lesscode.dc.internal.dsl;

import com.polaris.lesscode.util.JsonUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class Condition{

	private String type;
	
	private Condition[] conds;
	
	private String column;
	
	private Object left;
	
	private Object right;
	
	private Object values;

	private Object[] valuesArr;
	
	private Object value;

	private Object displayValue;
	
	private boolean noPretreat; 
	
	public Condition(String type, Condition[] conds, String column, Object left, Object right, Object[] values,
			Object value) {
		this(type, conds, column, left, right, values, value, false);
	}
	
	public Condition(String type, Condition[] conds, String column, Object left, Object right, Object[] values,
			Object value, boolean noPretreat) {
		this.type = type;
		this.conds = conds;
		this.column = column;
		this.left = left;
		this.right = right;
		this.values = values;
		this.value = value;
		this.noPretreat = noPretreat;
	}

	public Condition() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Condition[] getConds() {
		return conds;
	}

	public void setConds(Condition[] conds) {
		this.conds = conds;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Object getLeft() {
		return left;
	}

	public void setLeft(Object left) {
		this.left = left;
	}

	public Object getRight() {
		return right;
	}

	public void setRight(Object right) {
		this.right = right;
	}

	public Object[] getValues() {
		return getValuesArr();
	}

	public void setValues(Object values) {
		this.values = values;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isNoPretreat() {
		return noPretreat;
	}

	public void setNoPretreat(boolean noPretreat) {
		this.noPretreat = noPretreat;
	}

	private Object[] getValuesArr() {
		if (valuesArr != null) {
			return valuesArr;
		}

		if (values instanceof Collection) {
			valuesArr = ((Collection<?>) values).toArray();
			return valuesArr;
		}

		if (values instanceof Object[]) {
			valuesArr = (Object[]) values;
			return valuesArr;
		}
		valuesArr = new Object[]{values};
		return valuesArr;
	}

	public String toSql(List<Object> args) {
		Object[] valuesArr = getValuesArr();
		if (noPretreat) {
			if(Conditions.AND.equalsIgnoreCase(type)) {
				return "(" + StringUtils.join(Arrays.stream(conds).map(i -> i.toSql(args)).filter(condSql -> StringUtils.isNotBlank(condSql) && !Objects.equals("()", condSql.trim())).collect(Collectors.toList()), " and ") + ")";
			}else if(Conditions.BETWEEN.equalsIgnoreCase(type)) {
				return column + " between " + jsonbWithoutPretreat(column, left) + " and " + jsonbWithoutPretreat(column, right) + " ";
			}else if(Conditions.EQUAL.equalsIgnoreCase(type)) {
				return column + " = " + jsonbWithoutPretreat(column, value) + " ";
			}else if(Conditions.GT.equalsIgnoreCase(type)) {
				return column + " > " + jsonbWithoutPretreat(column, value) + " ";
			}else if(Conditions.GTE.equalsIgnoreCase(type)) {
				return column + " >= " + jsonbWithoutPretreat(column, value) + " ";
			}else if(Conditions.IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
				return column + " in ('" + StringUtils.join(Arrays.stream(valuesArr).map(i -> jsonbWithoutPretreat(column, i)).collect(Collectors.toList()), "','") + "')";
			}else if(Conditions.LIKE.equalsIgnoreCase(type)) {
				if(column.contains("->") && ! column.contains("->>")){
					column = column.replaceAll("->", "->>");
				}
				return column + " ilike " + jsonbWithoutPretreat(column, value) + " ";
			}else if(Conditions.LT.equalsIgnoreCase(type)) {
				return column + " < " + jsonbWithoutPretreat(column, value) + " ";
			}else if(Conditions.LTE.equalsIgnoreCase(type)) {
				return column + " <= " + jsonbWithoutPretreat(column, value) + " ";
			}else if(Conditions.NOT_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
				return "(" + column + " is null or " + column + " not in (" + StringUtils.join(Arrays.stream(valuesArr).map(i -> jsonbWithoutPretreat(column, i)).collect(Collectors.toList()), ',') + "))";
			}else if(Conditions.NOT_LIKE.equalsIgnoreCase(type)) {
				if(column.contains("->") && ! column.contains("->>")){
					column = column.replaceAll("->", "->>");
				}
				return "(" + column + " is null or " + column + " not ilike " + jsonbWithoutPretreat(column, value) + ")";
			}else if(Conditions.NOT_NULL.equalsIgnoreCase(type)) {
				return "(" + column + " is not null)";
			}else if(Conditions.IS_NULL.equalsIgnoreCase(type)) {
				return "(" + column + " is null )";
			}else if(Conditions.OR.equalsIgnoreCase(type)) {
				return "(" + StringUtils.join(Arrays.stream(conds).map(i -> i.toSql(args)).filter(condSql -> StringUtils.isNotBlank(condSql) && !Objects.equals("()", condSql.trim())).collect(Collectors.toList()), " or ") + ")";
			}else if(Conditions.UN_EQUAL.equalsIgnoreCase(type)) {
				return "(" + column + " is null or " + column + " != " + jsonbWithoutPretreat(column, value) + ")";
			}else if (Conditions.ALL_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
				return "jsonb_exists_all( " + column + " ,ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> jsonbWithoutPretreat(column, i)).collect(Collectors.toList()), ',') + "])";
			} else if (Conditions.VALUES_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
//				return "jsonb_exists_any( " + column + "  ,ARRAY[" + StringUtils.join(Arrays.asList(values).stream().map(i -> jsonbWithoutPretreat(column, i)).collect(Collectors.toList()), ',') + "])";
				return "ARRAY(SELECT jsonb_array_elements_text(" + Conditions.getCoalesceColumnArray(column) + ")) && ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> "'" + jsonbWithoutPretreat(column, i) + "'").collect(Collectors.toList()), ',') + "]";
			}else if (Conditions.NOT_ALL_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
				return "NOT jsonb_exists_all( " + column + " ,ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> jsonbWithoutPretreat(column, i)).collect(Collectors.toList()), ',') + "])";
			} else if (Conditions.NOT_VALUES_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
//				return "not jsonb_exists_any( " + column + "  ,ARRAY[" + StringUtils.join(Arrays.asList(values).stream().map(i -> jsonbWithoutPretreat(column, i)).collect(Collectors.toList()), ',') + "])";
				return "NOT ARRAY(SELECT jsonb_array_elements_text(" + Conditions.getCoalesceColumnArray(column) + ")) && ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> "'" + jsonbWithoutPretreat(column, i) + "'").collect(Collectors.toList()), ',') + "]";
			} else if (Conditions.MULTI_SELECT_EQUAL.equalsIgnoreCase(type)) {
				return "ARRAY(SELECT jsonb_array_elements_text(" + column + ")) && (" + value.toString() +")";
			} else if (Conditions.VALUES_EQUAL_VALUES.equalsIgnoreCase(type)) {
				return column + " = " +  value.toString();
			}  else if (Conditions.VALUES_INCLUDE_ANY_VALUES.equalsIgnoreCase(type)) {
				return column + " ??| ARRAY(SELECT jsonb_array_elements_text(" + value.toString() + "))";
			} else if (Conditions.RAW_SQL.equalsIgnoreCase(type)) {
				return column;
			}
		}else {
			if(Conditions.AND.equalsIgnoreCase(type)) {
				if(! ArrayUtils.isEmpty(conds)) {
					List<Condition> targetConds = Arrays.stream(conds).filter(cond -> cond != null && StringUtils.isNotBlank(cond.getType())).collect(Collectors.toList());
					if (CollectionUtils.isNotEmpty(targetConds)){
						return "(" + StringUtils.join(targetConds.stream().map(i -> i.toSql(args)).filter(condSql -> StringUtils.isNotBlank(condSql) && !Objects.equals("()", condSql.trim())).collect(Collectors.toList()), " and ") + ")";
					}
				}
			}else if(Conditions.BETWEEN.equalsIgnoreCase(type)) {
				String cond = "";
				if (Objects.nonNull(left)){
					args.add(jsonb(column, left));
					cond += column + " >= ?";
				}
				if (Objects.nonNull(right)){
					args.add(jsonb(column, right));
					if (StringUtils.isNotBlank(cond)){
						cond += " and ";
					}
					cond += column + " <= ? and " + column + " is not null";
				}
				return "(" + cond + ")";
			}else if(Conditions.EQUAL.equalsIgnoreCase(type)) {
				args.add(jsonb(column, value));
				return column + " = ? ";
			}else if(Conditions.GT.equalsIgnoreCase(type)) {
				args.add(jsonb(column, value));
				return column + " > ?";
			}else if(Conditions.GTE.equalsIgnoreCase(type)) {
				args.add(jsonb(column, value));
				return column + " >= ?";
			}else if(Conditions.IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
//				if(column.contains("->") && ! column.contains("->>")){
//					column = column.replaceAll("->", "->>");
//				}
				return column + " in ('" + StringUtils.join(Arrays.stream(valuesArr).map(i -> jsonb(column, i).toString()).collect(Collectors.toList()), "','") + "')";
			}else if(Conditions.LIKE.equalsIgnoreCase(type)) {
				args.add("%" + value + "%");
				if(column.contains("->") && ! column.contains("->>")){
					column = column.replaceAll("->", "->>");
				}
				return column + " ilike ? ";
			}else if(Conditions.LT.equalsIgnoreCase(type)) {
				args.add(jsonb(column, value));
				return "(" + column + " < ? and " + column + " is not null)";
			}else if(Conditions.LTE.equalsIgnoreCase(type)) {
				args.add(jsonb(column, value));
				return "(" + column + " <= ? and " + column + " is not null)";
			}else if(Conditions.NOT_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
				if(column.contains("->") && ! column.contains("->>")){
					column = column.replaceAll("->", "->>");
				}
				return "(" + column + " is null or " + column + " not in (" + StringUtils.join(Arrays.stream(valuesArr).map(i -> {args.add(obj(column, i)); return "?";}).collect(Collectors.toList()), ',') + "))";
			}else if(Conditions.NOT_LIKE.equalsIgnoreCase(type)) {
				args.add("%" + value + "%");
				if(column.contains("->") && ! column.contains("->>")){
					column = column.replaceAll("->", "->>");
				}
				return "(" + column + " is null or " + column + " not ilike ?)";
			}else if(Conditions.NOT_NULL.equalsIgnoreCase(type)) {
				return "(" + column + " is not null and " + column + " != '[]' and " + column + " != '\"\"')";
			}else if(Conditions.IS_NULL.equalsIgnoreCase(type)) {
				return "(" + column + " is null or " + column + " = '[]' or " + column + " = '\"\"')";
			}else if(Conditions.OR.equalsIgnoreCase(type)) {
				if(! ArrayUtils.isEmpty(conds)) {
					List<Condition> targetConds = Arrays.stream(conds).filter(cond -> cond != null && StringUtils.isNotBlank(cond.getType())).collect(Collectors.toList());
					if (CollectionUtils.isNotEmpty(targetConds)){
						return "(" + StringUtils.join(targetConds.stream().map(i -> i.toSql(args)).filter(condSql -> StringUtils.isNotBlank(condSql) && !Objects.equals("()", condSql.trim())).collect(Collectors.toList()), " or ") + ")";
					}
				}
			}else if(Conditions.UN_EQUAL.equalsIgnoreCase(type)) {
				args.add(jsonb(column, value));
				return "(" + column + " is null or " + column + " != ?)";
			}else if (Conditions.ALL_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
				return "jsonb_exists_all(" + column + " ,ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> {
					args.add(String.valueOf(i));
					return "?";
				}).collect(Collectors.toList()), ',') + "])";
			} else if (Conditions.VALUES_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
//				return "jsonb_exists_any(" + column + " ,ARRAY[" + StringUtils.join(Arrays.asList(values).stream().map(i -> {
//					args.add(String.valueOf(i));
//					return "?";
//				}).collect(Collectors.toList()), ',') + "])";
				return "ARRAY(SELECT jsonb_array_elements_text(" + Conditions.getCoalesceColumnArray(column) + ")) && ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> "'" + jsonbWithoutPretreat(column, i) + "'").collect(Collectors.toList()), ',') + "]";
			}else if (Conditions.NOT_ALL_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
				return "NOT jsonb_exists_all(" + column + " ,ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> {args.add(String.valueOf(i)); return "?";}).collect(Collectors.toList()), ',') + "])";
			} else if (Conditions.NOT_VALUES_IN.equalsIgnoreCase(type)) {
				if (ArrayUtils.isEmpty(valuesArr)){
					return "1 = 2";
				}
//				return "not jsonb_exists_any(" + column + " ,ARRAY[" + StringUtils.join(Arrays.asList(values).stream().map(i -> {
//					args.add(String.valueOf(i));
//					return "?";
//				}).collect(Collectors.toList()), ',') + "])";
				return "NOT ARRAY(SELECT jsonb_array_elements_text(" + Conditions.getCoalesceColumnArray(column) + ")) && ARRAY[" + StringUtils.join(Arrays.asList(valuesArr).stream().map(i -> "'" + jsonbWithoutPretreat(column, i) + "'").collect(Collectors.toList()), ',') + "]";
			} else if (Conditions.MULTI_SELECT_EQUAL.equalsIgnoreCase(type)) {
				return "ARRAY(SELECT jsonb_array_elements_text(" + column + ")) && (" + value.toString() +")";
			} else if (Conditions.VALUES_EQUAL_VALUES.equalsIgnoreCase(type)) {
				return column + " = " +  value.toString();
			}  else if (Conditions.VALUES_INCLUDE_ANY_VALUES.equalsIgnoreCase(type)) {
				return column + " ??| ARRAY(SELECT jsonb_array_elements_text(" + value.toString() + "))";
			} else if (Conditions.RAW_SQL.equalsIgnoreCase(type)) {
				return column;
			}
		}
		return "";
	}

	private String valuesEqualValues(String column, String value) {
		return column + " ??& ARRAY(SELECT jsonb_array_elements_text(" + value + "))";
	}

	private Object obj(String column, Object o){
		if(! column.contains("->")){
			return o;
		}
		return String.valueOf(o);
	}

	private Object jsonb(String column, Object o){
		if(! column.contains("->")){
			return o;
		}
		PGobject pGobject = new PGobject();
		pGobject.setType("JSONB");
		try {
			pGobject.setValue(null);
			if(o != null){
				if(o instanceof Number){
					pGobject.setValue(o.toString());
				}else if (o instanceof Collection || o instanceof Map){
					pGobject.setValue(JsonUtils.toJson(o));
				}else{
					pGobject.setValue("\"" + StringEscapeUtils.escapeJava(o.toString()) + "\"");
				}
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return pGobject;
	}

	private Object jsonbWithoutPretreat(String column, Object o){
		if(! column.contains("->")){
			return o;
		}
		if(o == null){
			return "null";
		}
		return o.toString();
	}
	
}

