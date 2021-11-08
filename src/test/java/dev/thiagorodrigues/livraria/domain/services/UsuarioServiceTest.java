package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.domain.mocks.UsuarioFactory;
import dev.thiagorodrigues.livraria.infra.repositories.PerfilRepository;
import dev.thiagorodrigues.livraria.infra.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private long existingId = 1L;
    private long nonExistingId = 100L;
    private String emailAlreadyInUse = "alreadyInUse@mail.com";
    private Usuario usuario;
    private Usuario usuarioUpdated;
    private UsuarioFormDto usuarioFormDto;
    private UsuarioResponseDto usuarioResponseDto;
    private UsuarioResponseDto usuarioUpdatedResponseDto;
    private UsuarioUpdateFormDto usuarioUpdateFormDto;

    @BeforeEach
    void setUp() {
        usuario = UsuarioFactory.createUser();
        usuarioUpdated = UsuarioFactory.createUpdatedUser();
        usuarioFormDto = UsuarioFactory.createUserFormDto();
        usuarioResponseDto = UsuarioFactory.createUserResponseDto();
        usuarioUpdatedResponseDto = UsuarioFactory.createUserUpdatedResponseDto();
        usuarioUpdateFormDto = UsuarioFactory.createUserUpdateFormDto();
    }

    @Test
    void createShouldThrowDomainExceptionWhenEmailAlreadyTaken() {
        when(usuarioRepository.existsByEmail(emailAlreadyInUse)).thenReturn(true);

        usuario.setEmail(emailAlreadyInUse);

        assertThrows(DomainException.class, () -> this.usuarioService.create(usuarioFormDto));
    }

    @Test
    void createShouldReturnAnUserWhenSuccessful() {
        when(usuarioRepository.existsByEmail(usuarioFormDto.getEmail())).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuarioFormDto, Usuario.class)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioResponseDto.class)).thenReturn(usuarioResponseDto);

        var usuarioResponseDto = this.usuarioService.create(usuarioFormDto);

        assertThat(usuarioResponseDto.getEmail()).isEqualTo(usuarioFormDto.getEmail());
    }

    @Test
    void detailShouldReturnNotFoundExceptionWhenInvalidId() {
        when(usuarioRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> this.usuarioService.detail(nonExistingId));
    }

    @Test
    void detailShouldReturnUserWhenValidId() {
        when(usuarioRepository.findById(existingId)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioResponseDto.class)).thenReturn(usuarioResponseDto);

        var usuarioResponseDto = this.usuarioService.detail(existingId);

        assertNotNull(usuarioResponseDto);
    }

    @Test
    void deleteShouldThrowNotFoundWhenInvalidId() {
        doThrow(EmptyResultDataAccessException.class).when(usuarioRepository).deleteById(nonExistingId);

        assertThrows(NotFoundException.class, () -> this.usuarioService.delete(nonExistingId));
    }

    @Test
    void deleteShouldReturnNothingWhenValidId() {
        doNothing().when(usuarioRepository).deleteById(existingId);

        assertDoesNotThrow(() -> this.usuarioService.delete(existingId));
    }

    @Test
    void updateShouldThrowDomainWhenInvalidId() {
        doThrow(EntityNotFoundException.class).when(usuarioRepository).getById(nonExistingId);

        usuarioUpdateFormDto.setId(nonExistingId);

        assertThrows(DomainException.class, () -> this.usuarioService.update(usuarioUpdateFormDto));
    }

    @Test
    void updateShouldThrowDomainExceptionWhenEmailAlreadyTaken() {
        when(usuarioRepository.getById(anyLong())).thenReturn(usuario);
        when(usuarioRepository.existsByEmail(emailAlreadyInUse)).thenReturn(true);

        usuarioUpdateFormDto.setEmail(emailAlreadyInUse);

        assertThrows(DomainException.class, () -> this.usuarioService.update(usuarioUpdateFormDto));
    }

    @Test
    void updateShouldReturnUpdatedUserWhenValidData() {
        when(usuarioRepository.getById(anyLong())).thenReturn(usuarioUpdated);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(usuarioUpdated, UsuarioResponseDto.class)).thenReturn(usuarioUpdatedResponseDto);

        var response = this.usuarioService.update(usuarioUpdateFormDto);

        assertEquals(usuarioUpdateFormDto.getEmail(), response.getEmail());
        assertEquals(usuarioUpdateFormDto.getNome(), response.getNome());
    }

}
