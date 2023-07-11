package com.gexingw.shop.common.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/7/11 10:30
 */
@Component
@SuppressWarnings("unused")
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@Nullable ApplicationContext context) throws BeansException {
        SpringUtil.context = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return clazz == null ? null : context.getBean(clazz);
    }

    public static Object getBean(String beanId) {
        return beanId == null ? null : context.getBean(beanId);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (null != beanName && !"".equals(beanName.trim())) {
            return clazz == null ? null : context.getBean(beanName, clazz);
        }

        return null;
    }

    public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException {
        return context.getBeansOfType(type);
    }

    public static ApplicationContext getContext() {
        return context == null ? null : context;
    }

}
