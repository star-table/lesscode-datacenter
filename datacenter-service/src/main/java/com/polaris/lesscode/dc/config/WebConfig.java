/**
 * 
 */
package com.polaris.lesscode.dc.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.polaris.lesscode.feign.ResultStatusDecoder;
import com.polaris.lesscode.web.InstantConvert;

import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;

/**
 * @author Bomb
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;
    
    @Bean
    public Decoder feignDecoder() {
    	Decoder decoder = new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
        return new ResultStatusDecoder(decoder);
    }

	/*
	 * @Override public void extendMessageConverters(List<HttpMessageConverter<?>>
	 * converters) { }
	 */
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new InstantConvert());
	}
}
