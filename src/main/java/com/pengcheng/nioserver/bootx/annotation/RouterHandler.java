package com.pengcheng.nioserver.bootx.annotation;


import io.vertx.core.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version 17-2-20 下午2:16.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RouterHandler {
    HttpMethod method() default HttpMethod.GET;

    String value();

    String tracker() default "";

    boolean worker() default true;
}
