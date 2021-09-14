package dev.thiagorodrigues.livraria.application.dtos;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorFormDto {

    @NotBlank
    private String nome;

    private String email;
    private LocalDate dataNascimento;
    private String miniCurriculo;

}
