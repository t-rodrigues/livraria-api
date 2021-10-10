package dev.thiagorodrigues.livraria.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AutorResponseDto {

    private Long id;
    private String nome;
    private String email;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("data_nascimento")
    private LocalDate dataNascimento;

}
