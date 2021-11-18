package dev.thiagorodrigues.livraria.domain.mocks;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateProfileDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import org.modelmapper.ModelMapper;

import java.util.Set;

public class UsuarioFactory {

    public final static ModelMapper modelMapper = new ModelMapper();

    public static Usuario createUser() {
        return new Usuario(1L, "John Doe", "john@mail", "SuperSecret123");
    }

    public static Usuario createUpdatedUser() {
        return new Usuario(10L, "Updated John Doe", "updated@mail", "SuperSecret123");
    }

    public static Usuario createUpdatedUserWithProfiles() {
        var user = new Usuario(10L, "Updated John Doe", "updated@mail", "SuperSecret123");
        var perfis = PerfilFactory.createListPerfis();
        perfis.forEach(user::adicionarPerfil);

        return user;
    }

    public static UsuarioFormDto createUserFormDto() {
        return modelMapper.map(createUser(), UsuarioFormDto.class);
    }

    public static UsuarioResponseDto createUserResponseDto() {
        return modelMapper.map(createUser(), UsuarioResponseDto.class);
    }

    public static UsuarioUpdateFormDto createUserUpdateFormDto() {
        return modelMapper.map(createUpdatedUser(), UsuarioUpdateFormDto.class);
    }

    public static UsuarioResponseDto createUserUpdatedResponseDto() {
        return modelMapper.map(createUpdatedUser(), UsuarioResponseDto.class);
    }

    public static UsuarioUpdateProfileDto createUsuarioUpdateProfilesDto() {
        return new UsuarioUpdateProfileDto(1L, Set.of(1L, 2L));
    }

    public static UsuarioResponseDto createUpdatedUserWithProfilesResponseDto() {
        return modelMapper.map(createUpdatedUserWithProfiles(), UsuarioResponseDto.class);
    }

}
