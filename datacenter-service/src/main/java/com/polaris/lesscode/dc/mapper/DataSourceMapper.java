package com.polaris.lesscode.dc.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polaris.lesscode.consts.CommonConsts;
import com.polaris.lesscode.dc.entity.DataSource;

public interface DataSourceMapper extends BaseMapper<DataSource>{

	default DataSource get(Long dsId) {
		return selectOne(new QueryWrapper<DataSource>()
				.eq("id", dsId)
				.eq("status", CommonConsts.ENABLE)
				.eq("del_flag", CommonConsts.NO_DELETE)
				.last(" limit 1"));
	}
	
	default List<DataSource> getList(){
		return selectList(new QueryWrapper<DataSource>()
				.eq("status", CommonConsts.ENABLE)
				.eq("del_flag", CommonConsts.NO_DELETE)
				.orderByAsc("id"));
	}
}
