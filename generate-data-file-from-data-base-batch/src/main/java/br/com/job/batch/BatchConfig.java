package br.com.job.batch;

import br.com.job.model.mysql.PessoaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final Logger LOG = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job createFileJob(@Qualifier("getFileStep") Step getFileStep) {

        LOG.info("Iniciando o Job");
        return jobBuilderFactory
                .get("createFileJob")
                .start(getFileStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step getFileStep(ItemReader<PessoaEntity> pessoaEntityItemReader, ItemWriter<PessoaEntity> pessoaEntityItemWriter) {

        LOG.info("Iniciando o Step: getFileStep");
        return stepBuilderFactory
                .get("getFileStep")
                .<PessoaEntity, PessoaEntity>chunk(100)
                .reader(pessoaEntityItemReader)
                .writer(pessoaEntityItemWriter)
                .build();
    }
}
