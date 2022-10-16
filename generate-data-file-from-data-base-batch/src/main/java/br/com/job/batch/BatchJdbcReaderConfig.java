package br.com.job.batch;

import br.com.job.model.mysql.PessoaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BatchJdbcReaderConfig {

    private static final Logger LOG = LoggerFactory.getLogger(BatchJdbcReaderConfig.class);

    @StepScope
    @Bean
    public JdbcPagingItemReader<PessoaEntity> pessoaEntityItemReader(
            @Qualifier("mysqlDataSource") DataSource dataSource,
            PagingQueryProvider queryProvider) {

        LOG.info("Iniciando Reader");
        return new JdbcPagingItemReaderBuilder<PessoaEntity>()
                .name("jdbcPagingReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .pageSize(100)
                .rowMapper(new BeanPropertyRowMapper<PessoaEntity>(PessoaEntity.class))
                .build();
    }

    @StepScope
    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider(
            @Value("#{jobParameters[initialId]}") String paramInicioId,
            @Value("#{jobParameters[finalId]}") String paramFinalId,
            @Qualifier("mysqlDataSource") DataSource dataSource) {

        if (paramInicioId == null || paramFinalId == null) {
            throw new IllegalArgumentException("Parâmetros não informados");
        }

        Map<String, Order> sortKeys = new HashMap<String, Order>(1);
        sortKeys.put("id", Order.ASCENDING);

        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select id, nome, cpf");
        queryProvider.setFromClause("from pessoa");
        queryProvider.setWhereClause(String.format("where id between %s and %s", paramInicioId, paramFinalId));
        queryProvider.setSortKeys(sortKeys);
        return queryProvider;
    }
}
