package dev.thiagorodrigues.livraria.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutorUpdateFormDto extends AutorFormDto {

    @NotNull
    private Long id;

}
