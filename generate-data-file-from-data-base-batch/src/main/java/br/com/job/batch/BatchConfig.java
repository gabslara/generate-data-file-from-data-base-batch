package br.com.job.batch;

import br.com.job.component.ValidateFileComponent;
import br.com.job.model.mysql.PessoaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
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

    @Autowired
    private ValidateFileComponent validateFileComponent;

    @Bean
    public Job createFileJob(@Qualifier("getFileStep") Step getFileStep,
                             @Qualifier("validateGeneratedFileStep") Step validateGeneratedFileStep) {

        LOG.info("Iniciando o Job");
        return jobBuilderFactory
                .get("createFileJob")
                .start(getFileStep)
                .next(validateGeneratedFileStep)
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

    @Bean
    public Step validateGeneratedFileStep(ItemReader<PessoaEntity> pessoaEntityItemReader, ItemWriter<PessoaEntity> pessoaEntityItemWriter) {

        LOG.info("Iniciando o Step: validateGeneratedFileStep");
        return stepBuilderFactory
                .get("validateGeneratedFileStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        validateFileComponent.listFileNames();
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
