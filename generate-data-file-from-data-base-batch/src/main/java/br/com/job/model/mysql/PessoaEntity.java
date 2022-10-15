package br.com.job.model.mysql;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class PessoaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;
    private String nome;
    private String cpf;
}
