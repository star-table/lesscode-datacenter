package com.polaris.lesscode.dc.internal.dsl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ColumnInfo {
    public static final String STRING = "string";
    public static final String SELECT = "select";
    public static final String MULTI_SELECT = "multi_select";
    public static final String MEMBER = "member";

    private String alias;
    private String name;
    private String type;
    private Map<Object,Object> options;
}
