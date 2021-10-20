package dev.thiagorodrigues.livraria.application.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import java.time.LocalDate;

@Getter
@Setter
public class LivroFormDto {

    @Size(min = 10)
    private String titulo;

    @PastOrPresent
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonAlias("data_lancamento")
    private LocalDate dataLancamento;

    @Min(value = 100)
    @JsonAlias("numero_paginas")
    private Integer numeroPaginas;

    @NotNull
    @JsonAlias("autor_id")
    private Long autorId;

}
