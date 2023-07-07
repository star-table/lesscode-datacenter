package com.polaris.lesscode.dc.mapper;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polaris.lesscode.consts.CommonConsts;
import com.polaris.lesscode.dc.entity.DataBase;

public interface DataBaseMapper extends BaseMapper<DataBase>{

	default DataBase get(Long dbId) {
		return selectOne(new QueryWrapper<DataBase>()
				.eq("id", dbId)
				.eq("status", CommonConsts.ENABLE)
				.eq("del_flag", CommonConsts.NO_DELETE)
				.last(" limit 1"));
	}
	
	default List<DataBase> getList(Integer type, List<Long> dsIds){
		QueryWrapper<DataBase> query = new QueryWrapper<DataBase>();
		if(type != null) {
			query.eq("type", 1);
		}
		if(! CollectionUtils.isEmpty(dsIds)) {
			query.in("ds_id", dsIds);
		}
		return selectList(query
				.eq("status", CommonConsts.ENABLE)
				.eq("del_flag", CommonConsts.NO_DELETE)
				.orderByAsc("id"));
	}
}
