package dev.thiagorodrigues.livraria.domain.entities;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Autor {

    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String miniCurriculo;

}
