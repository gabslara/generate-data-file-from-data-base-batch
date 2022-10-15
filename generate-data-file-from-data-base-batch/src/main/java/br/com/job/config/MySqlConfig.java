package br.com.job.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "mysqlEntityanagerFactory", transactionManagerRef = "mysqlTransactionManager", basePackages = {"br.com.job"})

public class MySqlConfig {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MySqlConfig.class);

    @Value(value = "${mysql.datasource.password}")
    private String password;

    @Value(value = "${mysql.datasource.username}")
    private String user;

    @Value(value = "${mysql.datasource.url}")
    private String url;

    @Value(value = "${mysql.datasource.driverClassName}")
    private String driverClassName;

    @Bean(name = "mysqlDataSource")
    public DataSource dataSource() {
        LOG.info("Inicializando datasource...");
        LOG.info("URL: {}", url);

        return DataSourceBuilder.create().password(password).username(user).driverClassName(driverClassName).url(url).build();
    }

    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("mysqlDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("br.com.job.model.mysql").persistenceUnit("mysql")
                .build();
    }

    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
