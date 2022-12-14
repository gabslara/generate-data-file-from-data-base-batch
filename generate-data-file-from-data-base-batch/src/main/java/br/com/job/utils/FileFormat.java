package br.com.job.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class FileFormat {

    public String fileFormat(String pathDestination) {

        String dataArquivo = dateFormat(LocalDate.now());

        return pathDestination + "/arquivo_dados_pessoa_" + dataArquivo + ".txt";
    }

    private static String dateFormat(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

        return formatter.format(date);
    }

    public String fileNameFormat(){
        String dataArquivo = dateFormat(LocalDate.now());

        return "arquivo_dados_pessoa_" + dataArquivo + ".txt";
    }
}
