package com.polaris.lesscode.dc.internal.dsl;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * 插入数据
 *
 * @author Nico
 * @date 2021/2/1 18:38
 */
@Data
public class Values {

    public static List<Value> values(Value... values) {
        return Arrays.asList(values);
    }

    public static Value value(List<Object> datas) {
        Value v = new Value();
        v.setDatas(datas);
        return v;
    }

    public static Value value(Object... datas) {
        return value(Arrays.asList(datas));
    }

}
