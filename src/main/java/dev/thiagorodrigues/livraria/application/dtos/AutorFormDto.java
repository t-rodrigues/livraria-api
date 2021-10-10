package dev.thiagorodrigues.livraria.application.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import java.time.LocalDate;

@Getter
@Setter
public class AutorFormDto {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @Past
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonAlias("data_nascimento")
    private LocalDate dataNascimento;

    @JsonAlias("mini_curriculo")
    private String miniCurriculo;

}
