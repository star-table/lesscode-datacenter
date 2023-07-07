package com.polaris.lesscode.dc.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("lc_data_base")
public class DataBase {

	private Long id;
	
	private Long dsId;
	
	private String name;
	
	private Integer type;
	
	private Integer status;
	
	private Long creator;
	
	private Date createTime;
	
	private Long updator;
	
	private Date updateTime;
	
	private Long version;
	
	private Integer delFlag;
}
