package br.com.job.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "h2EntityanagerFactory", transactionManagerRef = "h2TransactionManager", basePackages = {"br.com.job"})

public class H2DBConfig {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(H2DBConfig.class);

    @Value(value = "${spring.datasource.password}")
    private String password;

    @Value(value = "${spring.datasource.username}")
    private String user;

    @Value(value = "${spring.datasource.url}")
    private String url;

    @Value(value = "${spring.datasource.driverClassName}")
    private String driverClassName;

    @Primary
    @Bean(name = "h2DataSource")
    public DataSource dataSource() {
        LOG.info("Inicializando datasource...");
        LOG.info("URL: {}", url);

        return DataSourceBuilder.create().password(password).username(user).driverClassName(driverClassName).url(url).build();
    }

    @Primary
    @Bean(name = "h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("h2DataSource") DataSource dataSource) {
        return builder.dataSource(dataSource)
                .packages("br.com.job").persistenceUnit("job")
                .build();
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("h2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
