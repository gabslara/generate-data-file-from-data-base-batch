package br.com.job.component;

import br.com.job.utils.FileFormat;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@StepScope
public class ValidateFileComponent {

    public static final String MESSAGE_EMPTY_FILE = "Não foi gerado arquivo, verificar parâmetros";

    @Autowired
    FileFormat fileNameFormat;

    @Value("#{jobParameters[pathDestination]}")
    String pathDestination;

    public void listFileNames() {

        String fileName = fileNameFormat.fileNameFormat();

        List<String> fileNames = new ArrayList<>();

        File folder = new File(pathDestination);

        //Listando arquivos que estão na pasta de destino
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().contains(fileName)) {
                fileNames.add(file.getName());
            }
        }

        //Se não foi gravado arquivo, retornar erro
        if (fileNames.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_EMPTY_FILE);
        }
    }
}
