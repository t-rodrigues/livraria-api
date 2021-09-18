package dev.thiagorodrigues.livraria.domain.entities;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Livro {

    private Long id;
    private String titulo;
    private LocalDate dataLancamento;
    private Integer numeroPaginas;
    private Autor autor;

}
