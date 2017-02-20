package com.pengcheng.nioserver.bootx.base;


import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CompletableFuture;

/**
 * @version 17-2-20 上午11:40.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class Bootx {

    private static Logger logger = LoggerFactory.getLogger(Bootx.class);
    private static Vertx vertx = null;

    public Bootx() {
    }

    public static void init(VertxOptions options) {
        if (vertx != null) {
            throw new IllegalStateException("Vertx already init.");
        } else {
            vertx = Vertx.vertx(options);
        }
    }

    public static void deploy(BootOptions options) throws Exception {
        if(vertx == null) {
            throw new IllegalStateException("Method init() must be called before deploy any application.");
        } else {
            int i = 0;

            for(int instance = options.getInstance(); i < instance; ++i) {
                deployVerticle(options);
            }

        }
    }

    private static void deployVerticle(BootOptions options) throws Exception {
        ConfigurableApplicationContext context = options.getContext();
        CompletableFuture future = new CompletableFuture();
        vertx.deployVerticle(new SpringHttpVerticle(context, options), new DeploymentOptions(), (result) -> {
            if(result.failed()) {
                future.completeExceptionally(result.cause());
            } else {
                future.complete(result.result());
                logger.info("Deploy verticle {} on {}", result.result(), Integer.valueOf(options.getPort()));
            }

        });
        future.get();
    }

    public static Vertx vertx() {
        return vertx;
    }

    static {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
    }
}
