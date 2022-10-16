package br.com.job.tests.batch;

import br.com.job.batch.BatchJdbcReaderConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BatchJdbcReaderConfigTest {

    @Autowired
    private BatchJdbcReaderConfig batchReader;

    DataSource dataSource;
    PagingQueryProvider queryProvider;

    @Before
    public void setUp() throws Exception {
        batchReader = new BatchJdbcReaderConfig();

        dataSource = dataSource();

        SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean = getSqlPagingQueryProviderFactoryBean();

        queryProvider = sqlPagingQueryProviderFactoryBean.getObject();

    }

    @Test
    public void testRead(){
        assertNotNull("Resultado não pode ser null", batchReader.pessoaEntityItemReader(dataSource, queryProvider));
    }

    @Test
    public void testQueryProvider(){
        assertNotNull("Resultado não pode ser null", batchReader.queryProvider("1", "5", dataSource));
    }

    public DataSource dataSource() {
        DriverManagerDataSource dataSource1 = new DriverManagerDataSource();
        dataSource1.setDriverClassName("org.h2.Driver");
        dataSource1.setUrl("jdbc:h2:mem:testdb");
        dataSource1.setUsername("root");
        dataSource1.setPassword("");

        return dataSource1;
    }

    public SqlPagingQueryProviderFactoryBean getSqlPagingQueryProviderFactoryBean() {
        SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        sqlPagingQueryProviderFactoryBean.setDataSource(dataSource);
        sqlPagingQueryProviderFactoryBean.setDatabaseType("HSQL");
        sqlPagingQueryProviderFactoryBean.setSelectClause("select *");
        sqlPagingQueryProviderFactoryBean.setFromClause("from pessoa");
        sqlPagingQueryProviderFactoryBean.setSortKey("id");
        return sqlPagingQueryProviderFactoryBean;
    }

    public SqlPagingQueryProviderFactoryBean queryProvider() {

        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("select id, nome, cpf");
        queryProvider.setFromClause("from pessoa");
        return queryProvider;
    }

}
