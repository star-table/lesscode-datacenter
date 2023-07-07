/**
 * 
 */
package com.polaris.lesscode.dc.internal.feign;

import org.springframework.cloud.openfeign.FeignClient;

import com.polaris.lesscode.consts.ApplicationConsts;
import com.polaris.lesscode.dc.internal.api.DataCenterApi;
import com.polaris.lesscode.dc.internal.fallback.DataCenterFallbackFactory;

/**
 * @author admin
 *
 */
@FeignClient(value = ApplicationConsts.APPLICATION_DATACENTER,primary = false, fallbackFactory = DataCenterFallbackFactory.class)
public interface DataCenterProvider extends DataCenterApi {

}
