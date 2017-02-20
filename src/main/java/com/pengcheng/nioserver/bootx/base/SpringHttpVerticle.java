package com.pengcheng.nioserver.bootx.base;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @version 17-2-20 下午2:07.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class SpringHttpVerticle extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(SpringHttpVerticle.class);
    private BootOptions bootOptions;
    private ConfigurableApplicationContext context;

    SpringHttpVerticle(ConfigurableApplicationContext context, BootOptions bootOptions) {
        this.bootOptions = bootOptions;
        this.context = context;
    }

    public void start() throws Exception {
        super.start();
        Router router = SpringRouterFactory.router(this.vertx, this.context, this.bootOptions.getAppName());
        HttpServer var10000 = this.vertx.createHttpServer((new HttpServerOptions()).setMaxInitialLineLength('\uffff').setMaxHeaderSize('\uffff').setTcpKeepAlive(true).setTcpNoDelay(true).setAcceptBacklog(4096).setUsePooledBuffers(true));
        router.getClass();
        var10000.requestHandler(router::accept).listen(this.bootOptions.getPort());
    }

}
