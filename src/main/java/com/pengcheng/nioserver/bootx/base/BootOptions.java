package com.pengcheng.nioserver.bootx.base;

import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

/**
 * @version 17-2-20 下午1:55.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class BootOptions {

    private int port = 80;
    private String appName = "";
    private int instance = Runtime.getRuntime().availableProcessors() * 2;
    private Supplier<ConfigurableApplicationContext> contextSupplier;

    public BootOptions() {
    }

    public BootOptions(String location) throws IOException {
        Properties prop = new Properties();
        prop.load(ClassLoader.getSystemResourceAsStream(location));
        this.load(prop);
    }

    public BootOptions(Properties prop) {
        this.load(prop);
    }

    private void load(Properties prop) {
        if(prop.containsKey("vertx.listen")) {
            this.setPort(Integer.parseInt(prop.getProperty("vertx.listen")));
        }

        if(prop.containsKey("vertx.instance")) {
            this.setInstance(Integer.parseInt(prop.getProperty("vertx.instance")));
        }

    }

    int getPort() {
        return this.port;
    }

    String getAppName() {
        return this.appName;
    }

    public BootOptions setPort(int port) {
        this.port = port;
        return this;
    }

    int getInstance() {
        return this.instance;
    }

    public BootOptions setInstance(int instance) {
        this.instance = instance;
        return this;
    }

    public BootOptions setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    ConfigurableApplicationContext getContext() {
        return (ConfigurableApplicationContext)this.contextSupplier.get();
    }

    public BootOptions setContext(Supplier<ConfigurableApplicationContext> contextSupplier) {
        this.contextSupplier = contextSupplier;
        return this;
    }
}
