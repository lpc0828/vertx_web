package com.pengcheng.nioserver.bootx.factory;

import com.pengcheng.nioserver.bootx.base.Bootx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import org.springframework.beans.factory.FactoryBean;

/**
 * @version 17-2-20 下午2:38.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class HttpClientFactory implements FactoryBean<HttpClient> {
    private boolean keepAlive;
    private int maxPoolSize;
    private boolean pipelining;

    public HttpClientFactory() {
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setPipelining(boolean pipelining) {
        this.pipelining = pipelining;
    }

    public HttpClient getObject() throws Exception {
        return Bootx.vertx().createHttpClient((new HttpClientOptions()).setUsePooledBuffers(true).setKeepAlive(this.keepAlive).setMaxPoolSize(this.maxPoolSize).setPipelining(this.pipelining));
    }

    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    public boolean isSingleton() {
        return true;
    }
}