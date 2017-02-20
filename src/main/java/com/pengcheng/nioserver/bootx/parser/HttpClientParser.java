package com.pengcheng.nioserver.bootx.parser;

import com.pengcheng.nioserver.bootx.factory.HttpClientFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @version 17-2-20 下午2:37.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class HttpClientParser implements BeanDefinitionParser {
    public HttpClientParser() {
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        GenericBeanDefinition def = new GenericBeanDefinition();
        MutablePropertyValues prop = new MutablePropertyValues();
        def.setBeanClass(HttpClientFactory.class);
        prop.addPropertyValue("keepAlive", element.getAttribute("keep-alive"));
        prop.addPropertyValue("maxPoolSize", element.getAttribute("max-pool-size"));
        prop.addPropertyValue("pipelining", element.getAttribute("pipelining"));
        def.setPropertyValues(prop);
        registry.registerBeanDefinition("httpClient", def);
        return null;
    }
}