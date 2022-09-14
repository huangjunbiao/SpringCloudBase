package com.huang.cloudbase.defaultserver.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author huangjunbiao_cdv
 */
@FeignClient(name = "cloudbase-authserver")
public interface RemoteOpenFeignClient {
    @GetMapping(path = "/auth/test/echo")
    String callEcho();
}
