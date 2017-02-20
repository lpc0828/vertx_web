package com.pengcheng.nioserver.bootx.parser;

import com.pengcheng.nioserver.bootx.annotation.Router;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionDefaults;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @version 17-2-20 下午2:35.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class RouterParser  implements BeanDefinitionParser {

    public RouterParser() {
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(parserContext.getRegistry(), false);
        BeanDefinitionDefaults defaults = new BeanDefinitionDefaults();
        defaults.setLazyInit(true);
        scanner.setBeanDefinitionDefaults(defaults);
        String basePackage = element.getAttribute("base-package");
        scanner.addIncludeFilter(new AnnotationTypeFilter(Router.class));
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ",; \t\n"));
        return null;
    }
}