package dev.thiagorodrigues.livraria.application.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthFormDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

}
