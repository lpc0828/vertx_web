package com.pengcheng.nioserver.bootx.base;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.pengcheng.nioserver.bootx.annotation.RouterHandler;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @version 17-2-20 下午2:07.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class SpringRouterFactory {

    private static Logger logger = LoggerFactory.getLogger(SpringRouterFactory.class);
    private static HashSet<Class<?>> SUPPORT_RETURN_TYPE = new HashSet(Arrays.asList(new Class[]{RouterResult.class}));
    private static HashSet<Class<?>> SUPPORT_PARAMETER_TYPE = new HashSet(Arrays.asList(new Class[]{RoutingContext.class, Vertx.class, HttpServerRequest.class, HttpServerResponse.class, MultiMap.class}));
    private static Handler<RoutingContext> failHandler = (context) -> {
        Throwable e = context.failure();
        if(!(e instanceof IllegalArgumentException) && !(e instanceof NullPointerException) && !(e instanceof IllegalStateException)) {
            if(e instanceof GenericException) {
                GenericException generic = (GenericException)e;
                context.response().setStatusCode(200);
                context.response().end(JSON.toJSONString(generic.getAttributes()));
            } else {
                logger.error("Route exception", e);
                context.response().setStatusCode(500).end("Internal Exception");
            }
        } else {
            logger.error("Illegal Arguments", e);
            context.response().setStatusCode(400).end(e.getMessage() != null?e.getMessage():"Illegal Arguments.");
        }

    };

    private SpringRouterFactory() {
    }

    static Router router(Vertx vertx, ApplicationContext applicationContext, String appName) {
        Router router = Router.router(vertx);
        Map handlers = applicationContext.getBeansWithAnnotation(com.pengcheng.nioserver.bootx.annotation.Router.class);
        handlers.values().forEach((handler) -> {
            registerHandlers(router, handler, appName);
        });
        return router;
    }

    private static void registerHandlers(Router router, Object handler, String appName) {
        com.pengcheng.nioserver.bootx.annotation.Router controller = (com.pengcheng.nioserver.bootx.annotation.Router)handler.getClass().getAnnotation(com.pengcheng.nioserver.bootx.annotation.Router.class);
        Method[] var4 = handler.getClass().getMethods();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            RouterHandler mapping = (RouterHandler)method.getAnnotation(RouterHandler.class);
            if(mapping != null) {
                if(!SUPPORT_RETURN_TYPE.contains(method.getReturnType())) {
                    throw new RuntimeException("Unsupported return type " + method.getReturnType().getSimpleName() + " for method " + method.getName() + " on handler " + handler.getClass().getSimpleName());
                }

                Class[] var9 = method.getParameterTypes();
                int var10 = var9.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    Class type = var9[var11];
                    if(!SUPPORT_PARAMETER_TYPE.contains(type)) {
                        throw new RuntimeException("Unsupported parameter type " + type.getSimpleName() + " for method " + method.getName() + " on handler " + handler.getClass().getSimpleName());
                    }
                }

                registerMethod(router, handler, method, controller, mapping, appName);
            }
        }

    }

    private static void registerMethod(Router router, Object handler, Method method, com.pengcheng.nioserver.bootx.annotation.Router controller, RouterHandler mapping, String appName) {
        if(mapping.method() == HttpMethod.POST || mapping.method() == HttpMethod.PUT) {
            router.route(mapping.method(), controller.value() + mapping.value()).handler(BodyHandler.create());
        }

        if(mapping.worker()) {
            router.route(mapping.method(), controller.value() + mapping.value()).blockingHandler(new SpringRouterFactory.RouterHandlerImpl(handler, method, mapping.method(), mapping.tracker(), appName)).failureHandler(failHandler);
        } else {
            router.route(mapping.method(), controller.value() + mapping.value()).handler(new SpringRouterFactory.RouterHandlerImpl(handler, method, mapping.method(), mapping.tracker(), appName)).failureHandler(failHandler);
        }

        logger.info("Register handler {} {}{} on {}.{}", new Object[]{mapping.method(), controller.value(), mapping.value(), handler.getClass().getSimpleName(), method.getName()});
    }

    private static class RouterHandlerImpl implements Handler<RoutingContext> {
        Object handler;
        Method method;
        String tracker;
        HttpMethod httpMethod;

        RouterHandlerImpl(Object handler, Method method, HttpMethod httpMethod, String tracker, String appName) {
            this.handler = handler;
            this.method = method;
            if(!StringUtils.isEmpty(appName)) {
                appName = appName + "_";
            }

            if(StringUtils.isEmpty(tracker)) {
                this.tracker = (appName + "router_" + method.getName()).toLowerCase();
            } else {
                this.tracker = (appName + "router_" + tracker).toLowerCase();
            }

            this.httpMethod = httpMethod;
        }

        public void handle(RoutingContext context) {
            try {
                Stopwatch e = Stopwatch.createStarted();
                HttpServerRequest req = context.request();
                String source = req.getHeader("source");
                RouterResult result = this.invoke(this.method, this.handler, context);
                result.writeResponse(context);
                if(StringUtils.isEmpty(source)) {
                    source = "unknown";
                }
                //DMonitor.recordOne(this.tracker + "_" + source, e.stop().elapsed(TimeUnit.MILLISECONDS));
            } catch (NullPointerException | IllegalStateException | IllegalArgumentException var6) {
                SpringRouterFactory.logger.error("Invalid handler declaration", var6);
                context.fail(var6);
            } catch (Throwable var7) {
                context.fail(var7);
            }

        }

        private RouterResult invoke(Method method, Object handler, RoutingContext context) throws Throwable {
            try {
                HashMap e = new HashMap();
                e.put(RoutingContext.class, context);
                e.put(Vertx.class, context.vertx());
                e.put(HttpServerRequest.class, context.request());
                e.put(HttpServerResponse.class, context.response());
                e.put(MultiMap.class, context.request().params());
                ArrayList arguments = new ArrayList(method.getParameterCount());
                Class[] var6 = method.getParameterTypes();
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    Class type = var6[var8];
                    arguments.add(e.get(type));
                }

                return (RouterResult)method.invoke(handler, arguments.toArray());
            } catch (InvocationTargetException var10) {
                throw var10.getTargetException();
            }
        }
    }
}
