package com.huang.cloudbase.gateway.service;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author huangjunbiao
 * @date 2020/12/4 17:19
 */
@Lazy(false)
@Component
public class DynamicRouteServiceImplByNacos {
    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    @Autowired
    private ObjectMapper objectMapper;

    public static final long DEFAULT_TIMEOUT = 30000;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.cloud.nacos.config.server-addr:}")
    private String nacosAddr;

    @Value("${spring.cloud.nacos.config.namespace:}")
    private String nacosNamespace;

    @Value("${spring.cloud.nacos.config.group:}")
    private String nacosGroupId;

    @Value("${cloudbase-gateway-route-defines.data-id:cloudbase-gateway-route-defines}")
    private String nacosGatewayDataId;

    private ConfigService configService;

    public void dynamicRouteByNacosListener() {
        try {
            this.configService = initConfigService();
            String config = this.configService.getConfig(nacosGatewayDataId, nacosGroupId, DEFAULT_TIMEOUT);
            logger.info(config);
            this.configService.addListener(nacosGatewayDataId, nacosGroupId, new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newSingleThreadExecutor();
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    try {
                        List<RouteDefinition> definitionList = objectMapper.readValue(config, objectMapper.getTypeFactory().constructCollectionType(LinkedList.class, RouteDefinition.class));
                        definitionList.forEach(definition -> {
                            dynamicRouteService.update(definition);
                        });
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        logger.info("gateway route init...");
        this.configService = initConfigService();
        if (this.configService == null) {
            logger.warn("initConfigService fail");
            return;
        }
        try {
            String config = this.configService.getConfig(nacosGatewayDataId, nacosGroupId, DEFAULT_TIMEOUT);
            logger.info("获取网关当前配置:\r\n{}", config);
            try {
                List<RouteDefinition> definitionList = objectMapper.readValue(config, objectMapper.getTypeFactory().constructCollectionType(LinkedList.class, RouteDefinition.class));
                definitionList.forEach(definition -> {
                    logger.info("update route : {}", definition.toString());
                    dynamicRouteService.add(definition);
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } catch (NacosException e) {
            e.printStackTrace();
        }
        dynamicRouteByNacosListener();
    }

    public ConfigService initConfigService() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, this.nacosAddr);
        properties.setProperty(PropertyKeyConst.NAMESPACE, this.nacosNamespace);
        try {
            this.configService = NacosFactory.createConfigService(properties);
            return this.configService;
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}
