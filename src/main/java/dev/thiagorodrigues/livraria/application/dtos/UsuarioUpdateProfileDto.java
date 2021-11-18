package dev.thiagorodrigues.livraria.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateProfileDto {

    @NotNull
    private Long id;

    @NotEmpty
    private Set<Long> profiles = new HashSet<>();

}
