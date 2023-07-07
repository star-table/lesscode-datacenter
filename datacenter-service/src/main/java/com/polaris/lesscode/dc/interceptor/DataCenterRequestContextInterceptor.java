package com.polaris.lesscode.dc.interceptor;

import org.springframework.stereotype.Component;

import com.polaris.lesscode.interceptor.RequestContextInterceptor;

import feign.RequestInterceptor;

@Component
public class DataCenterRequestContextInterceptor extends RequestContextInterceptor implements RequestInterceptor{

}
