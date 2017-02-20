package com.pengcheng.nioserver.bootx.base;

import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @version 17-2-20 下午2:34.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class FactoryBeanRegistry {

    private FactoryBeanRegistry() {
    }
    static <T> void register(GenericApplicationContext context, Class<T> beanClass) {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(beanClass);
        context.registerBeanDefinition(beanClass.getSimpleName(), definition);
    }
}
