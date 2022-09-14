package com.huang.cloudbase.gateway.service;

import com.huang.cloudbase.gateway.router.CustomizeInMemoryRouteDefinitionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author huangjunbiao
 * @date 2020/12/4 13:55
 */
@Component
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CustomizeInMemoryRouteDefinitionRepository routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    /**
     * @param definition RouteDefinition
     * @return String
     * @description 添加
     * @author huangjunbiao
     * @date 2020/12/4 16:24
     */
    public String add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    public void refresh(List<RouteDefinition> routeDefinitions) {
        this.routeDefinitionWriter.refreshRouter(routeDefinitions);
    }

    /**
     * @param definition RouteDefinition
     * @return String
     * @description 更新
     * @author huangjunbiao
     * @date 2020/12/4 16:28
     */
    public String update(RouteDefinition definition) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            return "update fail,not find route  routeId: " + definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            logger.info("The route(id: {}) is updated.", definition.getId());

            return "success";
        } catch (Exception e) {
            return "update route  fail";
        }
    }

    /**
     * @param id String
     * @return String
     * @description 删除
     * @author huangjunbiao
     * @date 2020/12/4 16:33
     */
    public String delete(String id) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(id));
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "delete fail,not find route  routeId: " + id;
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
