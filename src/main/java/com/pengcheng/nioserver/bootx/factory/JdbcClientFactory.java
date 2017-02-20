package com.pengcheng.nioserver.bootx.factory;

import com.pengcheng.nioserver.bootx.base.Bootx;
import io.vertx.ext.jdbc.JDBCClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;

/**
 * @version 17-2-20 下午5:43.
 * @Author <a href="mailto:rockway0828@gmail.com">pcliu</a>
 */
public class JdbcClientFactory implements InitializingBean, ApplicationContextAware, FactoryBean<JDBCClient> {
    private String dataSourceRef;
    private DataSource dataSource;
    private ApplicationContext applicationContext;

    public JdbcClientFactory() {
    }

    public void setDataSourceRef(String dataSourceRef) {
        this.dataSourceRef = dataSourceRef;
    }

    public JDBCClient getObject() throws Exception {
        return JDBCClient.create(Bootx.vertx(), this.dataSource);
    }

    public Class<?> getObjectType() {
        return JDBCClient.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {
        Object bean = this.applicationContext.getBean(this.dataSourceRef);
        if(!DataSource.class.isInstance(bean)) {
            throw new InvalidPropertyException(DataSource.class, "data-source-ref", "Invalid DataSource");
        } else {
            this.dataSource = (DataSource)bean;
        }
    }
}
