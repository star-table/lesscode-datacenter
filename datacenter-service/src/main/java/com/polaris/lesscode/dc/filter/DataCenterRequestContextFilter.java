package com.polaris.lesscode.dc.filter;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.polaris.lesscode.filter.RequestContextFilter;

@Component
@WebFilter(filterName = "datacenterRequestContextFilter", urlPatterns = "/*")
@Order(1)
public class DataCenterRequestContextFilter extends RequestContextFilter implements Filter{

}
