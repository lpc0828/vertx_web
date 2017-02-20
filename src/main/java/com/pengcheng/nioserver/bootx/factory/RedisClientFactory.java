package com.pengcheng.nioserver.bootx.factory;

import com.pengcheng.nioserver.bootx.base.Bootx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.springframework.beans.factory.FactoryBean;

/**
 * @version 17-2-20 下午5:49.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class RedisClientFactory implements FactoryBean<RedisClient> {
    private String host;
    private int port;

    public RedisClientFactory() {
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public RedisClient getObject() throws Exception {
        return RedisClient.create(Bootx.vertx(), (new RedisOptions()).setHost(this.host).setPort(this.port).setTcpKeepAlive(true).setTcpNoDelay(true));
    }

    public Class<?> getObjectType() {
        return RedisClient.class;
    }

    public boolean isSingleton() {
        return true;
    }
}