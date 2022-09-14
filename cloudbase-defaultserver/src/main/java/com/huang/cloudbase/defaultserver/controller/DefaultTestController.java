package com.huang.cloudbase.defaultserver.controller;

import com.huang.cloudbase.defaultserver.Model;
import com.huang.cloudbase.defaultserver.client.RemoteOpenFeignClient;
import com.huang.cloudbase.defaultserver.config.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huangjunbiao
 * @date 2020/12/7 10:59
 */
@RestController
public class DefaultTestController {
    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private RemoteOpenFeignClient feignClient;

    @GetMapping("/z/test")
    public Object test(HttpServletRequest request) {
        System.out.println("请求"+new Model().getName());
        System.out.println(this.serverProperties.getData());
        System.out.println(request.getHeader("userId"));
        return this.serverProperties.isEnable();
    }

    @GetMapping("/x/test")
    public Object test2() {
        System.out.println("请求2");
        return this.serverProperties.isEnable();
    }

    @GetMapping("/testfeign")
    public String test3() {
        System.out.println("请求feignclient。。。");
        return this.feignClient.callEcho();
    }
}
