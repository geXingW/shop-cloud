package com.gexingw.shop.gateway.component;

import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 16:39
 */
@SuppressWarnings("unused")
@Service
public class DynamicRouteService implements ApplicationEventPublisherAware {

    private final RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    public DynamicRouteService(RouteDefinitionWriter routeDefinitionWriter) {
        this.routeDefinitionWriter = routeDefinitionWriter;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 增加路由
     */
    public String save(RouteDefinition definition) {
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "save success";
        } catch (Exception e) {
            e.printStackTrace();
            return "save failure";
        }
    }

    /**
     * 更新路由
     */
    public void update(RouteDefinition definition) {
        try {
            //noinspection ReactiveStreamsUnusedPublisher
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
            this.routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新路由
     */
    public void updateList(List<RouteDefinition> routeDefinitions) {
        routeDefinitions.forEach(this::update);
    }

    /**
     * 删除路由
     */
    public String delete(String id) {
        try {
            //noinspection ReactiveStreamsUnusedPublisher
            this.routeDefinitionWriter.delete(Mono.just(id));
            return "delete success";
        } catch (Exception e) {
            e.printStackTrace();
            return "delete failure";
        }
    }


}
