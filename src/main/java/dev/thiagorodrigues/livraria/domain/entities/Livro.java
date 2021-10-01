package dev.thiagorodrigues.livraria.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private LocalDate dataLancamento;
    private Integer numeroPaginas;

    @ManyToOne
    private Autor autor;

}
