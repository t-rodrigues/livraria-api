package dev.thiagorodrigues.livraria.application.dtos;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroFormAutorDto {

    @NotNull
    private Long id;

}
