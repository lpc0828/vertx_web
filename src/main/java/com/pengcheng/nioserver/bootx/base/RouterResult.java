package com.pengcheng.nioserver.bootx.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @version 17-2-20 下午2:08.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public abstract class RouterResult {

    private static SerializeConfig jsonConfig;

    static {
        jsonConfig = new SerializeConfig();
        jsonConfig.setAsmEnable(true);
    }

    public static RouterResult status(int status) {
        return new StatusResult(status);
    }

    public static RouterResult text(String text) {
        return text(text, "text/plain");
    }

    public static RouterResult text(String text, String mimeType) {
        return new TextResult(text, mimeType);
    }

    public static RouterResult json(Object object) {
        return new JsonResult(object, 0);
    }

    public static RouterResult json(Object object, int status) {
        return new JsonResult(object, status);
    }

    public static RouterResult async(Consumer<CompletableFuture<RouterResult>> task) {
        return new AsyncConsumerResult(task);
    }

    public static RouterResult async(Supplier<RouterResult> task) {
        return new AsyncSupplierResult(task);
    }

    protected abstract void writeResponse(RoutingContext context);

    private static class StatusResult extends RouterResult {

        private int status;

        private StatusResult(int status) {
            this.status = status;
        }

        @Override
        protected void writeResponse(RoutingContext context) {
            HttpServerResponse res = context.response();
            res.setStatusCode(status);
            res.end();
        }

    }

    private static class TextResult extends RouterResult {

        private String text, mimeType;

        private TextResult(String text, String mimeType) {
            this.text = text;
            this.mimeType = mimeType;
        }

        @Override
        protected void writeResponse(RoutingContext context) {
            HttpServerResponse res = context.response();
            res.putHeader("Content-Type", mimeType);
            res.end(text, "utf-8");
        }
    }

    private static class JsonResult extends RouterResult {

        private Object object;

        private int status;

        private JsonResult(Object object, int status) {
            this.object = object;
            this.status = status;
        }

        @Override
        protected void writeResponse(RoutingContext context) {
            HttpServerResponse res = context.response();
            JSONObject json = new JSONObject();
            json.put("status", status);
            json.put("data", object);
            res.putHeader("Content-Type", "application/json; charset=utf-8");
            res.end(JSON.toJSONString(json, jsonConfig,
                    SerializerFeature.DisableCircularReferenceDetect), "utf-8");
        }
    }

    private static class AsyncConsumerResult extends RouterResult {

        private Consumer<CompletableFuture<RouterResult>> task;

        private AsyncConsumerResult(Consumer<CompletableFuture<RouterResult>> task) {
            this.task = task;
        }

        @Override
        protected void writeResponse(RoutingContext context) {
            CompletableFuture<RouterResult> innerFuture = new CompletableFuture<>();
            innerFuture.thenAccept((result) ->
                    result.writeResponse(context))
                    .exceptionally((e) -> {
                        context.fail(e.getCause());
                        return null;
                    });
            task.accept(innerFuture);
        }
    }

    private static class AsyncSupplierResult extends RouterResult {

        private Supplier<RouterResult> task;

        private AsyncSupplierResult(Supplier<RouterResult> task) {
            this.task = task;
        }

        @Override
        protected void writeResponse(RoutingContext context) {
            context.vertx().executeBlocking((vertxFuture) -> {
                try {
                    RouterResult result = task.get();
                    result.writeResponse(context);
                } catch (Throwable e) {
                    context.fail(e.getCause());
                }
            }, (result) -> {
            });
        }
    }

}
