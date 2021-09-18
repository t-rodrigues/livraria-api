package dev.thiagorodrigues.livraria.application.dtos;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroResponseDto {

    private Long id;
    private String titulo;
    private LocalDate dataLancamento;
    private Integer numeroPaginas;

    private LivroResponseAutor autor;

}
