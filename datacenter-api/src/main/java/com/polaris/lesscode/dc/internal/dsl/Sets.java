package com.polaris.lesscode.dc.internal.dsl;

/**
 * Set的集合工具
 *
 * @author Nico
 * @date 2021/2/1 16:58
 */
public class Sets {

    // NORMAL
    public static Set set(String tableAlias, String column, Object value) {
        return new Set(tableAlias, column, value, Set.TYPE_NORMAL, false);
    }

    public static Set setWithoutPretreat(String tableAlias, String column, Object value) {
        return new Set(tableAlias, column, value, Set.TYPE_NORMAL, true);
    }

    public static Set set(String column, Object value) {
        return new Set(column, value, Set.TYPE_NORMAL, false);
    }

    public static Set setWithoutPretreat(String column, Object value) {
        return new Set(column, value, Set.TYPE_NORMAL, true);
    }

    // JSON
    public static Set setJsonB(String tableAlias, String column, Object value) {
        return new Set(tableAlias, column, value, Set.TYPE_JSON, false);
    }

    public static Set setJsonBWithoutPretreat(String tableAlias, String column, Object value) {
        return new Set(tableAlias, column, value, Set.TYPE_JSON, true);
    }

    public static Set setJsonB(String column, Object value) {
        return new Set(column, value, Set.TYPE_JSON, false);
    }

    public static Set setJsonBWithoutPretreat(String column, Object value) {
        return new Set(column, value, Set.TYPE_JSON, true);
    }

    // JSON ARRAY
    public static Set setJsonBArray(String tableAlias, String column, Object value, int action) {
        return new Set(tableAlias, column, value, Set.TYPE_JSON, action, false);
    }

    public static Set setJsonBArrayWithoutPretreat(String tableAlias, String column, Object value, int action) {
        return new Set(tableAlias, column, value, Set.TYPE_JSON, action, true);
    }

    public static Set setJsonBArray(String column, Object value, int action) {
        return new Set(column, value, Set.TYPE_JSON, action, false);
    }

    public static Set setJsonBArrayWithoutPretreat(String column, Object value, int action) {
        return new Set(column, value, Set.TYPE_JSON, action, true);
    }

    // JSON MAP
    public static Set setJsonBMap(String tableAlias, String column, Object value, int action) {
        return new Set(tableAlias, column, value, Set.TYPE_JSON, action, false);
    }

    public static Set setJsonBMapWithoutPretreat(String tableAlias, String column, Object value, int action) {
        return new Set(tableAlias, column, value, Set.TYPE_JSON, action, true);
    }

    public static Set setJsonBMap(String column, Object value, int action) {
        return new Set(column, value, Set.TYPE_JSON, action, false);
    }

    public static Set setJsonBMapWithoutPretreat(String column, Object value, int action) {
        return new Set(column, value, Set.TYPE_JSON, action, true);
    }
}
