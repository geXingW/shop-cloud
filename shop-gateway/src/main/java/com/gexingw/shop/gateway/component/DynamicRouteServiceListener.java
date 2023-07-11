package com.gexingw.shop.gateway.component;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/8 16:44
 */
@Slf4j
@Component
@RefreshScope
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicRouteServiceListener {

    private final DynamicRouteService dynamicRouteService;
    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    private final NacosConfigProperties nacosConfigProperties;

    public DynamicRouteServiceListener(DynamicRouteService dynamicRouteService, NacosDiscoveryProperties nacosDiscoveryProperties, NacosConfigProperties nacosConfigProperties) {
        this.dynamicRouteService = dynamicRouteService;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.nacosConfigProperties = nacosConfigProperties;
        dynamicRouteServiceListener();
    }

    /**
     * 监听Nacos下发的动态路由配置
     */
    private void dynamicRouteServiceListener() {
        try {
            String dataId = nacosDiscoveryProperties.getService() + "-routes";
            String group = nacosConfigProperties.getGroup();
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
            properties.setProperty(PropertyKeyConst.NAMESPACE, nacosDiscoveryProperties.getNamespace());
            ConfigService configService = NacosFactory.createConfigService(properties);
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.debug("获取到Nacos路由配置信息：{}", configInfo);
                    List<RouteDefinition> routeDefinitions = JSON.parseArray(configInfo, RouteDefinition.class);
                    dynamicRouteService.updateList(routeDefinitions);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
            String configInfo = configService.getConfig(dataId, group, 5000);
            if (configInfo != null) {
                log.debug("获取到Nacos路由配置信息：{}", configInfo);
                List<RouteDefinition> routeDefinitions = JSON.parseArray(configInfo, RouteDefinition.class);
                dynamicRouteService.updateList(routeDefinitions);
            }
        } catch (NacosException ignored) {

        }
    }

}
