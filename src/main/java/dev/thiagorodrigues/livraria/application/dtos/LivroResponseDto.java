package dev.thiagorodrigues.livraria.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LivroResponseDto {

    private Long id;
    private String titulo;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataLancamento;
    private Integer numeroPaginas;

    private LivroResponseAutor autor;

}
