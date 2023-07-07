package com.polaris.lesscode.dc.internal.dsl;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 插入数据
 *
 * @author Nico
 * @date 2021/2/1 18:38
 */
@Data
public class Value {

    /**
     * 行记录列数据，如果是jsonb，则需要传个Map或者List进来
     *
     * @Author Nico
     * @Date 2021/2/1 19:50
     **/
    private List<Object> datas;

    public Value(List<Object> datas) {
        this.datas = datas;
    }

    public Value() {
        this.datas = new ArrayList<>();
    }

    public Value add(Object data){
        if(datas == null){
            datas = new ArrayList<>();
        }
        datas.add(data);
        return this;
    }

}
