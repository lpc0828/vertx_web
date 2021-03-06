package com.pengcheng.nioserver.bootx.parser;

import com.pengcheng.nioserver.bootx.factory.JdbcClientFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @version 17-2-20 下午2:36.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class JdbcParser implements BeanDefinitionParser {
    public JdbcParser() {
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        GenericBeanDefinition def = new GenericBeanDefinition();
        MutablePropertyValues prop = new MutablePropertyValues();
        def.setBeanClass(JdbcClientFactory.class);
        String id = element.getAttribute("id");
        prop.addPropertyValue("dataSourceRef", element.getAttribute("data-source-ref"));
        def.setPropertyValues(prop);
        registry.registerBeanDefinition(id, def);
        return null;
    }
}