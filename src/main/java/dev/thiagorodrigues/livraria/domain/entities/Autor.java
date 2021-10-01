package dev.thiagorodrigues.livraria.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    private LocalDate dataNascimento;

    @Lob
    private String miniCurriculo;

}
