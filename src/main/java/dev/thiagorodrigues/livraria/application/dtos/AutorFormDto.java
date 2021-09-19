package dev.thiagorodrigues.livraria.application.dtos;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorFormDto {

    @NotBlank
    private String nome;

    @Email
    private String email;

    @Past
    private LocalDate dataNascimento;

    @NotBlank
    private String miniCurriculo;

}
