package dev.thiagorodrigues.livraria.application.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LivroUpdateFormDto extends LivroFormDto {

    @NotNull
    private Long id;

}
