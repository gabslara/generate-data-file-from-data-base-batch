package br.com.job.batch;

import br.com.job.model.mysql.PessoaEntity;
import br.com.job.utils.FileFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.Writer;

@Configuration
public class BatchFileWriterConfig {

    private static final Logger LOG = LoggerFactory.getLogger(BatchJdbcReaderConfig.class);

    @Autowired
    FileFormat fileFormat;

    Resource arquivoSaida;

    @StepScope
    @Bean
    public FlatFileItemWriter<PessoaEntity> pessoaEntityItemWriter(
            @Value("#{jobParameters[pathDestination]}") String pathDestination) {

        LOG.info("Iniciando o Writer");

        if (pathDestination == null) {
            throw new IllegalArgumentException("Parâmetro pathDestination não informado");
        }

        String fileName = fileFormat.fileFormat(pathDestination);
        arquivoSaida = new FileSystemResource(fileName);

        return new FlatFileItemWriterBuilder<PessoaEntity>()
                .name("pessoaEntityItemWriter")
                .resource(arquivoSaida)
                .delimited()
                .delimiter("|")
                .names("id", "nome", "cpf")
                .headerCallback(callback())
                .encoding("UTF-8")
                .shouldDeleteIfEmpty(true)
                .build();
    }

    private FlatFileHeaderCallback callback() {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.append("id|nome|cpf");
            }
        };
    }
}
