package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.domain.mocks.UsuarioFactory;
import dev.thiagorodrigues.livraria.infra.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

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
        usuarioUpdatedResponseDto = UsuarioFactory.createUserResponseDto();
        usuarioUpdateFormDto = UsuarioFactory.createUserUpdateFormDto();

        when(modelMapper.map(usuarioFormDto, Usuario.class)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioResponseDto.class)).thenReturn(usuarioResponseDto);
        when(modelMapper.map(usuarioUpdated, UsuarioResponseDto.class)).thenReturn(usuarioUpdatedResponseDto);
        when(usuarioRepository.existsByEmail(emailAlreadyInUse)).thenReturn(true);
        when(usuarioRepository.existsByEmail(usuarioFormDto.getEmail())).thenReturn(false);
        when(usuarioRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(usuarioRepository.findById(existingId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioRepository.save(usuarioUpdated)).thenReturn(usuarioUpdated);
        when(usuarioRepository.getById(usuarioUpdateFormDto.getId())).thenReturn(usuario);
        doThrow(EntityNotFoundException.class).when(usuarioRepository).getById(nonExistingId);
        doThrow(EmptyResultDataAccessException.class).when(usuarioRepository).deleteById(nonExistingId);
        doNothing().when(usuarioRepository).deleteById(existingId);
    }

    @Test
    void createShouldThrowDomainExceptionWhenEmailAlreadyTaken() {
        usuario.setEmail(emailAlreadyInUse);

        assertThrows(DomainException.class, () -> this.usuarioService.create(usuarioFormDto));
    }

    @Test
    void createShouldReturnAnUserWhenSuccessful() {
        var usuarioResponseDto = this.usuarioService.create(usuarioFormDto);

        assertThat(usuarioResponseDto.getEmail()).isEqualTo(usuarioFormDto.getEmail());
    }

    @Test
    void detailShouldReturnNotFoundExceptionWhenInvalidId() {
        assertThrows(NotFoundException.class, () -> this.usuarioService.detail(nonExistingId));
    }

    @Test
    void detailShouldReturnUserWhenValidId() {
        var usuarioResponseDto = this.usuarioService.detail(existingId);

        assertNotNull(usuarioResponseDto);
    }

    @Test
    void deleteShouldThrowNotFoundWhenInvalidId() {
        assertThrows(NotFoundException.class, () -> this.usuarioService.delete(nonExistingId));
    }

    @Test
    void deleteShouldReturnNothingWhenValidId() {
        assertDoesNotThrow(() -> this.usuarioService.delete(existingId));
    }

    @Test
    void updateShouldThrowDomainWhenInvalidId() {
        usuarioUpdateFormDto.setId(nonExistingId);

        assertThrows(DomainException.class, () -> this.usuarioService.update(usuarioUpdateFormDto));
    }

    @Test
    void updateShouldThrowDomainWhenEmailAlreadyTaken() {
        usuarioUpdateFormDto.setEmail(emailAlreadyInUse);

        assertThrows(DomainException.class, () -> this.usuarioService.update(usuarioUpdateFormDto));
    }

    @Test
    void updateShouldReturnUpdatedUserWhenSuccessful() {
        var usuarioResponseDto = this.usuarioService.update(usuarioUpdateFormDto);

        assertEquals(usuarioUpdateFormDto.getId(), usuarioResponseDto.getId());
    }

}
