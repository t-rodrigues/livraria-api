package dev.thiagorodrigues.livraria.application.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String email;

}
