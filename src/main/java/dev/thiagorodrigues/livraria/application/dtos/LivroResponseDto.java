package dev.thiagorodrigues.livraria.application.dtos;

import java.time.LocalDate;

import dev.thiagorodrigues.livraria.domain.entities.Autor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroResponseDto {

    private String titulo;
    private LocalDate dataLancamento;
    private Integer numeroPaginas;

    private Autor autor;

}
