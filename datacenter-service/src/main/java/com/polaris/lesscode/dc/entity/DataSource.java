package com.polaris.lesscode.dc.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("lc_data_source")
public class DataSource {

	private Long id;
	
	private String name;
	
	private String url;
	
	private String user;
	
	private String password;
	
	private Integer status;
	
	private Long creator;
	
	private Date createTime;
	
	private Long updator;
	
	private Date updateTime;
	
	private Long version;
	
	private Integer delFlag;
}
