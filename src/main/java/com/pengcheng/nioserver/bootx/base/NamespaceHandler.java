package com.pengcheng.nioserver.bootx.base;

import com.pengcheng.nioserver.bootx.parser.HttpClientParser;
import com.pengcheng.nioserver.bootx.parser.JdbcParser;
import com.pengcheng.nioserver.bootx.parser.RedisClientParser;
import com.pengcheng.nioserver.bootx.parser.RouterParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @version 17-2-20 下午2:34.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class NamespaceHandler extends NamespaceHandlerSupport {

    public NamespaceHandler() {
    }

    public void init() {
        this.registerBeanDefinitionParser("router", new RouterParser());
        this.registerBeanDefinitionParser("http", new HttpClientParser());
        this.registerBeanDefinitionParser("jdbc", new JdbcParser());
        this.registerBeanDefinitionParser("redis", new RedisClientParser());
    }
}
