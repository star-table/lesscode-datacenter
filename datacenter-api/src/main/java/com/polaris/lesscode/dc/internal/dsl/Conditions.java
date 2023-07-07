package com.polaris.lesscode.dc.internal.dsl;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.bouncycastle.util.Strings;

import java.util.*;

public class Conditions{

	public static final String AND = "and";
	public static final String BETWEEN = "between";
	public static final String EQUAL = "equal";
	public static final String GT = "gt";
	public static final String GTE = "gte";
	public static final String IN = "in";
	public static final String LIKE = "like";
	public static final String LT = "lt";
	public static final String LTE = "lte";
	public static final String NOT_IN = "not_in";
	public static final String NOT_LIKE = "not_like";
	public static final String NOT_NULL = "not_null";
	public static final String IS_NULL = "is_null";
	public static final String OR = "or";
	public static final String UN_EQUAL = "un_equal";
	public static final String ALL_IN = "all_in";
	public static final String VALUES_IN = "values_in";
	public static final String NOT_ALL_IN = "not_all_in";
	public static final String NOT_VALUES_IN = "not_values_in";
	public static final String MULTI_SELECT_EQUAL = "multi_select_equal";
	public static final String VALUES_EQUAL_VALUES = "values_equal_values";
	public static final String VALUES_INCLUDE_ANY_VALUES = "values_include_any_values";
	public static final String RAW_SQL = "raw_sql";

	public static Condition and(Condition... conds) {
		return new Condition(AND, conds, null, null, null, null, null);
	}

	public static Condition and(List<Condition> conds) {
		return new Condition(AND, conds.toArray(new Condition[]{}), null, null, null, null, null);
	}

	public static Condition or(Condition... conds) {
		return new Condition(OR, conds, null, null, null, null, null);
	}
	
	public static Condition or(List<Condition> conds) {
		return new Condition(OR, conds.toArray(new Condition[] {}), null, null, null, null, null);
	}
	
	public static Condition between(String column, Object left, Object right) {
		return new Condition(BETWEEN, null, column, left, right, null, null);
	}
	
	public static Condition equal(String column, Object value) {
		return new Condition(EQUAL, null, column, null, null, null, value);
	}

	public static Condition multiSelectEqual(String column, String value) {
		return new Condition(MULTI_SELECT_EQUAL, null, column, null, null, null, value);
	}

	public static Condition valuesEqual(String column, String value) {
		return new Condition(VALUES_EQUAL_VALUES, null, column, null, null, null, value);
	}

	public static Condition valuesIncludeAny(String column, String value) {
		return new Condition(VALUES_INCLUDE_ANY_VALUES, null, column, null, null, null, value);
	}

	public static Condition equalNoPretreat(String column, Object value) {
		return new Condition(EQUAL, null, column, null, null, null, value, true);
	}
	
	public static Condition unEqual(String column, Object value) {
		return new Condition(UN_EQUAL, null, column, null, null, null, value);
	}

	public static Condition unEqualPretreat(String column, Object value) {
		return new Condition(UN_EQUAL, null, column, null, null, null, value, true);
	}
	
	public static Condition lt(String column, Object value) {
		return new Condition(LT, null, column, null, null, null, value);
	}

	public static Condition ltPretreat(String column, Object value) {
		return new Condition(LT, null, column, null, null, null, value, true);
	}
	
	public static Condition gt(String column, Object value) {
		return new Condition(GT, null, column, null, null, null, value);
	}

	public static Condition gtPretreat(String column, Object value) {
		return new Condition(GT, null, column, null, null, null, value, true);
	}
	
	public static Condition lte(String column, Object value) {
		return new Condition(LTE, null, column, null, null, null, value);
	}

	public static Condition ltePretreat(String column, Object value) {
		return new Condition(LTE, null, column, null, null, null, value, true);
	}
	
	public static Condition gte(String column, Object value) {
		return new Condition(GTE, null, column, null, null, null, value);
	}

	public static Condition gtePretreat(String column, Object value) {
		return new Condition(GTE, null, column, null, null, null, value, true);
	}
	
	public static Condition like(String column, String value) {
		return new Condition(LIKE, null, column, null, null, null, value);
	}

	public static Condition likePretreat(String column, String value) {
		return new Condition(LIKE, null, column, null, null, null, value, true);
	}
	
	public static Condition notLike(String column, String value) {
		return new Condition(NOT_LIKE, null, column, null, null, null, value);
	}
	
	public static Condition in(String column, Object... values) {
		return new Condition(IN, null, column, null, null, values, null);
	}

	public static Condition in(String column, Collection<?> values) {
		return new Condition(IN, null, column, null, null, values.toArray(), null);
	}

	public static Condition notIn(String column, Object... values) {
		return new Condition(NOT_IN, null, column, null, null, values, null);
	}

	public static Condition notIn(String column, Collection<?> values) {
		return new Condition(NOT_IN, null, column, null, null, values.toArray(), null);
	}
	
	public static Condition nil(String column) {
		return new Condition(IS_NULL, null, column, null, null, null, null);
	}

	public static Condition nilPretreat(String column) {
		return new Condition(IS_NULL, null, column, null, null, null, null, true);
	}

	public static Condition notNil(String column) {
		return new Condition(NOT_NULL, null, column, null, null, null, null);
	}

	public static Condition notNilPretreat(String column) {
		return new Condition(NOT_NULL, null, column, null, null, null, null, true);
	}

	public static Condition allIn(String column, Object... values) {
		return new Condition(ALL_IN, null, column, null, null, values, null);
	}

	public static Condition allIn(String column, Collection<?> values) {
		return new Condition(ALL_IN, null, column, null, null, values.toArray(), null);
	}

	public static Condition valuesIn(String column, Object... values) {
		return new Condition(VALUES_IN, null, column, null, null, values, null, true);
	}

	public static Condition valuesIn(String column, Collection<?> values) {
		return new Condition(VALUES_IN, null, column, null, null, values.toArray(), null, true);
	}

	public static Condition notAllIn(String column, Object... values) {
		return new Condition(NOT_ALL_IN, null, column, null, null, values, null);
	}

	public static Condition notAllIn(String column, Collection<?> values) {
		return new Condition(NOT_ALL_IN, null, column, null, null, values.toArray(), null);
	}

	public static Condition notValuesIn(String column, Object... values) {
		return new Condition(NOT_VALUES_IN, null, column, null, null, values, null, true);
	}

	public static Condition notValuesIn(String column, Collection<?> values) {
		return new Condition(NOT_VALUES_IN, null, column, null, null, values.toArray(), null, true);
	}

	public static Condition rawSql(String column) {
		return new Condition(RAW_SQL, null, column, null, null, null, null);
	}

	public static void addConditionsAlias(Condition[] conditions, String alias) {
		if (Objects.isNull(conditions) || conditions.length == 0) {
			return;
		}
		for (Condition condition : conditions) {
			addConditionAlias(condition, alias);
		}
	}

	public static void addConditionAlias(Condition condition, String alias) {
		if (condition == null || StringUtils.isBlank(condition.getType())) {
			return;
		}

		switch (condition.getType()) {
			case "and": case "or":
				addConditionsAlias(condition.getConds(), alias);
				break;
			default:
				condition.setColumn(SqlUtil.wrapperAliasColumn(alias, condition.getColumn()));
				break;
		}
	}

	public static Condition joinCondition(ColumnInfo fromColumn, ColumnInfo toColumn, String operation) {
		if (ColumnInfo.STRING.equals(fromColumn.getType())) {
			switch (toColumn.getType()) {
				case ColumnInfo.SELECT:
					return getStringEqualSelect(fromColumn, toColumn);
				case ColumnInfo.MULTI_SELECT:
					return getStringEqualMultiSelect(fromColumn, toColumn);
			}
		} else if (ColumnInfo.SELECT.equals(fromColumn.getType())) {
			switch (toColumn.getType()) {
				case ColumnInfo.STRING:
					return getStringEqualSelect(toColumn, fromColumn);
				case ColumnInfo.SELECT:
					return getSelectEqualSelect(fromColumn, toColumn);
				case ColumnInfo.MULTI_SELECT:
					return getSelectEqualMultiSelect(fromColumn, toColumn);
			}
		} else if (ColumnInfo.MULTI_SELECT.equals(fromColumn.getType())) {
			switch (toColumn.getType()) {
				case ColumnInfo.STRING:
					return getStringEqualMultiSelect(toColumn, fromColumn);
				case ColumnInfo.SELECT:
					return getSelectEqualMultiSelect(toColumn, fromColumn);
				case ColumnInfo.MULTI_SELECT:
					return getMultiEqualMulti(fromColumn, toColumn, operation);
			}
		} else if (ColumnInfo.MEMBER.equals(fromColumn.getType())) {
			if (ColumnInfo.MEMBER.equals(toColumn.getType())) {
				if (EQUAL.equalsIgnoreCase(operation)) {
					return valuesEqual(getCoalesceColumnArray(SqlUtil.wrapperAliasJsonColumn(fromColumn.getAlias(),fromColumn.getName())),
							getCoalesceColumnArray(SqlUtil.wrapperAliasJsonColumn(toColumn.getAlias(),toColumn.getName())));
				} else {
					return valuesIncludeAny(getCoalesceColumnArray(SqlUtil.wrapperAliasJsonColumn(fromColumn.getAlias(),fromColumn.getName())),
							getCoalesceColumnArray(SqlUtil.wrapperAliasJsonColumn(toColumn.getAlias(),toColumn.getName())));
				}
			}
		}

		return getStringEqual(fromColumn, toColumn);
	}

	public static Condition getStringEqual(ColumnInfo fromColumn, ColumnInfo toColumn) {
		return equalNoPretreat(getCoalesceColumnString(SqlUtil.wrapperAliasJsonColumn(fromColumn.getAlias(), fromColumn.getName()), SqlUtil.wrapperAliasJsonTextColumn(fromColumn.getAlias(), fromColumn.getName())),
				getCoalesceColumnString(SqlUtil.wrapperAliasJsonColumn(toColumn.getAlias(), toColumn.getName()), SqlUtil.wrapperAliasJsonTextColumn(toColumn.getAlias(), toColumn.getName())));
	}

	public static String getCoalesceColumnString(String columnId, String textColumnId) {
		return "(case when " + columnId + " is null then '' else "+ textColumnId+ " end)";
	}

	public static String getCoalesceColumnArray(String columnId) {
		String textColumnId = columnId.replace("->", "->>");
		return "case when " + textColumnId + " is null then '[]'::jsonb else "+ columnId +" end";
	}

	public static Condition getStringEqualSelect(ColumnInfo stringColumn, ColumnInfo selectColumn) {
		if (selectColumn.getOptions() == null || selectColumn.getOptions().size() == 0) {
			return getStringEqual(stringColumn, selectColumn);
		}

		return equalNoPretreat(SqlUtil.wrapperAliasJsonTextColumn(stringColumn.getAlias(), stringColumn.getName()),
				getCaseWhenOptions(SqlUtil.wrapperAliasJsonTextColumn(selectColumn.getAlias(), selectColumn.getName()), selectColumn.getOptions(), true));
	}

	public static Condition getStringEqualMultiSelect(ColumnInfo stringColumn, ColumnInfo multiSelectColumn) {
		if (multiSelectColumn.getOptions() == null || multiSelectColumn.getOptions().size() == 0) {
			return getStringEqual(stringColumn, multiSelectColumn);
		}

		Map<Object, Object> reversOptions = getReversOptions(multiSelectColumn.getOptions());
		return multiSelectEqual(SqlUtil.wrapperAliasJsonColumn(multiSelectColumn.getAlias(), multiSelectColumn.getName()),
				getCaseWhenOptions(SqlUtil.wrapperAliasJsonTextColumn(stringColumn.getAlias(), stringColumn.getName()), reversOptions, false));
	}

	public static Condition getSelectEqualSelect(ColumnInfo fromColumn, ColumnInfo toColumn) {
		if (fromColumn.getOptions() == null || fromColumn.getOptions().size() == 0 || toColumn.getOptions() == null || toColumn.getOptions().size() == 0) {
			return getStringEqual(fromColumn, toColumn);
		}

		return equalNoPretreat(getCaseWhenOptions(SqlUtil.wrapperAliasJsonTextColumn(fromColumn.getAlias(),fromColumn.getName()), fromColumn.getOptions(), true),
				getCaseWhenOptions(SqlUtil.wrapperAliasJsonTextColumn(toColumn.getAlias(),toColumn.getName()), toColumn.getOptions(), true));
	}

	public static Condition getSelectEqualMultiSelect(ColumnInfo selectColumn, ColumnInfo multiSelectColumn) {
		if (selectColumn.getOptions() == null || selectColumn.getOptions().size() == 0 || multiSelectColumn.getOptions() == null || multiSelectColumn.getOptions().size() == 0) {
			return getStringEqual(selectColumn, multiSelectColumn);
		}

		Map<Object, Object> reversOptions = getReversOptions(multiSelectColumn.getOptions());
		return multiSelectEqual(SqlUtil.wrapperAliasJsonColumn(multiSelectColumn.getAlias(),multiSelectColumn.getName()),
				getCaseWhenOptions(getCaseWhenOptions(SqlUtil.wrapperAliasJsonTextColumn(selectColumn.getAlias(),selectColumn.getName()), selectColumn.getOptions(), true), reversOptions, false));
	}

	public static Condition getMultiEqualMulti(ColumnInfo fromColumn, ColumnInfo toColumn, String operation) {
		if (fromColumn.getOptions() == null || fromColumn.getOptions().size() == 0 || toColumn.getOptions() == null || toColumn.getOptions().size() == 0) {
			return getStringEqual(fromColumn, toColumn);
		}

		if (EQUAL.equalsIgnoreCase(operation)) {
			return valuesEqual(SqlUtil.wrapperAliasJsonColumn(fromColumn.getAlias(),fromColumn.getName()),
					SqlUtil.wrapperAliasJsonColumn(toColumn.getAlias(),toColumn.getName()));
		} else {
			return valuesIncludeAny(SqlUtil.wrapperAliasJsonColumn(fromColumn.getAlias(),fromColumn.getName()),
					SqlUtil.wrapperAliasJsonColumn(toColumn.getAlias(),toColumn.getName()));
		}
	}

	// 有些本应该是空的，却赋值了，这种算空值，不计算count
	public static String getEmptyCaseWhen(String columnId) {
		return "case (" + columnId + ")" +
				" when '1970-01-01 00:00:00' then NULL " +
				" when '0001-01-01 00:00:00' then NULL " +
				" when '[]' then NULL " +
				" when '{}' then NULL " +
				" when '' then NULL " +
				" else " + columnId +
				" end";
	}

	private static Map<Object,Object> getReversOptions(Map<Object,Object> options) {
		Map<Object, Object> reversOptions = new HashMap<>();
		options.forEach((key, value)-> {
			if (reversOptions.get(value.toString()) == null) {
				reversOptions.put(value.toString(), "ARRAY['"+key+"'");
			} else {
				reversOptions.put(value.toString(), reversOptions.get(value) + ", '" + key +"'");
			}
		});
		reversOptions.forEach((key, value) -> {
			reversOptions.put(key, reversOptions.get(key) + "]");
		});

		return reversOptions;
	}

	private static String getCaseWhenOptions(String selectColumn, Map<Object,Object> options, boolean valueIsString) {
		StringBuffer buffer = new StringBuffer("case (" + selectColumn + ")");
		options.forEach((key, value)-> {
			buffer.append(" when '").append(key);
			if (valueIsString) {
				buffer.append("' then '").append(value).append("'");
			} else {
				buffer.append("' then ").append(value);
			}
		});
		buffer.append(" else ");
		if (valueIsString) {
			buffer.append(selectColumn).append(" end");
		} else {
			buffer.append("ARRAY['-1']").append(" end");
		}

		return buffer.toString();
	}

}

