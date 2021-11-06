package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.mocks.UsuarioFactory;
import dev.thiagorodrigues.livraria.infra.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private String emailAlreadyInUse = "alreadyInUse@mail.com";
    private Usuario usuario;
    private UsuarioFormDto usuarioFormDto;
    private UsuarioResponseDto usuarioResponseDto;

    @BeforeEach
    void setUp() {
        usuario = UsuarioFactory.createUser();
        usuarioFormDto = UsuarioFactory.createUserFormDto();
        usuarioResponseDto = UsuarioFactory.createUserResponseDto();

        when(modelMapper.map(usuarioFormDto, Usuario.class)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioResponseDto.class)).thenReturn(usuarioResponseDto);
        when(usuarioRepository.existsByEmail(emailAlreadyInUse)).thenReturn(true);
        when(usuarioRepository.existsByEmail(usuarioFormDto.getEmail())).thenReturn(false);
    }

    @Test
    void shouldThrowDomainExceptionWhenEmailAlreadyTaken() {
        usuario.setEmail(emailAlreadyInUse);

        assertThrows(DomainException.class, () -> this.usuarioService.criar(usuarioFormDto));
    }

    @Test
    void shouldReturnAnUserWhenSuccessful() {
        var usuarioResponseDto = this.usuarioService.criar(usuarioFormDto);

        assertThat(usuarioResponseDto.getEmail()).isEqualTo(usuarioFormDto.getEmail());
    }

}
