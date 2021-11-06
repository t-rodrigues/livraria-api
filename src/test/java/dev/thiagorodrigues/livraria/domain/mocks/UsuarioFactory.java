package dev.thiagorodrigues.livraria.domain.mocks;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import org.modelmapper.ModelMapper;

public class UsuarioFactory {

    public final static ModelMapper modelMapper = new ModelMapper();

    public static Usuario createUser() {
        return new Usuario(1L, "John Doe", "john@mail", "SuperSecret123");
    }

    public static UsuarioFormDto createUserFormDto() {
        return modelMapper.map(createUser(), UsuarioFormDto.class);
    }

    public static UsuarioResponseDto createUserResponseDto() {
        return modelMapper.map(createUser(), UsuarioResponseDto.class);
    }

}
