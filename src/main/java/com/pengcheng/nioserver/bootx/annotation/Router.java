package com.pengcheng.nioserver.bootx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version 17-2-20 下午2:15.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Router {
    String value() default "";
}
