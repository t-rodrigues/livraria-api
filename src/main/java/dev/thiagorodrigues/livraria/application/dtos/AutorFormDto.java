package dev.thiagorodrigues.livraria.application.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorFormDto {

    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String miniCurriculo;

}
