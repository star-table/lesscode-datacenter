package com.polaris.lesscode.dc.filter;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.polaris.lesscode.filter.RequestLoggerFilter;

//@Component
//@WebFilter(filterName = "datacenterRequestLoggerFilter", urlPatterns = "/*")
//@Order(2)
public class DataCenterRequestLoggerFilter extends RequestLoggerFilter implements Filter{

}
