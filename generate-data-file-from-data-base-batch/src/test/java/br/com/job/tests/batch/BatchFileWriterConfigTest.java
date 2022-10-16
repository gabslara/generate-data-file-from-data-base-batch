package br.com.job.tests.batch;

import br.com.job.batch.BatchFileWriterConfig;
import br.com.job.utils.FileFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchFileWriterConfigTest {

    @InjectMocks
    private BatchFileWriterConfig batchWriter;

    @Mock
    private FileFormat fileFormat;

    String pathDestination;

    @Before
    public void setUp() {
        pathDestination = "arquivo_testes";
    }

    @Test
    public void testWriter() {
        when(fileFormat.fileFormat(pathDestination)).thenReturn("arquivo_testes.txt");

        assertNotNull("Resultado não pode ser null", batchWriter.pessoaEntityItemWriter(pathDestination));
    }
}