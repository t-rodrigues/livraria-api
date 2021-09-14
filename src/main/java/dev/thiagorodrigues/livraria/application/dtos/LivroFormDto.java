package dev.thiagorodrigues.livraria.application.dtos;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroFormDto {

    @Size(min = 10)
    private String titulo;

    @PastOrPresent
    private LocalDate dataLancamento;

    @Min(value = 100)
    private Integer numeroPaginas;

    private AutorFormDto autor;

}
