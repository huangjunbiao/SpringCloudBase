package com.huang.cloudbase.authserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangjunbiao_cdv
 */
@RestController
@RequestMapping("/test")
public class AuthTestRS {

    @GetMapping("/echo")
    public String echo() {

        return "success";
    }
}
