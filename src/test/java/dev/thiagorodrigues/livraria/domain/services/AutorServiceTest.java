package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.AutorDetalhadoResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.domain.mocks.AutorFactory;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AutorServiceTest {

    private final long validId = 1L;
    private final long invalidId = 100L;
    private final long dependentId = 50L;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;
    private Autor autorAtualizado;
    private AutorFormDto autorFormDto;
    private AutorDetalhadoResponseDto autorDetalhadoResponseDto;
    private AutorDetalhadoResponseDto autorAtualizadoResponseDto;
    private AutorUpdateFormDto autorUpdateFormDto;

    @BeforeEach
    private void setUp() {
        autor = AutorFactory.criarAutor();
        autorAtualizado = AutorFactory.criarAutorAtualizado();
        autorFormDto = AutorFactory.criarAutorFormDto();
        autorDetalhadoResponseDto = AutorFactory.criarAutorDetalhadoResponseDto();
        autorAtualizadoResponseDto = AutorFactory.criarAutorAtualizadoResponseDto();
        autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();

        when(modelMapper.map(autorFormDto, Autor.class)).thenReturn(autor);
        when(modelMapper.map(autor, AutorDetalhadoResponseDto.class)).thenReturn(autorDetalhadoResponseDto);
        when(modelMapper.map(autorAtualizado, AutorDetalhadoResponseDto.class)).thenReturn(autorAtualizadoResponseDto);
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);
        when(autorRepository.findById(validId)).thenReturn(Optional.of(autor));
        when(autorRepository.findById(invalidId)).thenReturn(Optional.empty());
        when(autorRepository.getById(autorUpdateFormDto.getId())).thenReturn(autorAtualizado);
        doThrow(EntityNotFoundException.class).when(autorRepository).getById(invalidId);
        doThrow(EmptyResultDataAccessException.class).when(autorRepository).deleteById(invalidId);
        doThrow(DataIntegrityViolationException.class).when(autorRepository).deleteById(dependentId);
    }

    @Test
    void createShouldReturnAuthorWhenSuccessful() {
        var autorDetalhadoResponseDto = autorService.create(autorFormDto);

        assertEquals(autor.getNome(), autorDetalhadoResponseDto.getNome());
        assertEquals(autor.getEmail(), autorDetalhadoResponseDto.getEmail());
        assertEquals(autor.getDataNascimento(), autorDetalhadoResponseDto.getDataNascimento());
        assertEquals(autor.getMiniCurriculo(), autorDetalhadoResponseDto.getMiniCurriculo());
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void detailShouldThrowNotFoundExceptionWhenInvalidId() {
        assertThrows(NotFoundException.class, () -> autorService.detail(invalidId));
        verify(autorRepository, times(1)).findById(invalidId);
    }

    @Test
    void detailShouldReturnAuthorWhenSuccessful() {
        var autorResponse = autorService.detail(validId);

        assertNotNull(autorResponse);
        assertEquals(autorDetalhadoResponseDto.getId(), autorResponse.getId());
        verify(autorRepository, times(1)).findById(validId);
    }

    @Test
    void updateShouldThrowDomainExceptionWhenInvalidId() {
        autorUpdateFormDto.setId(invalidId);

        assertThrows(DomainException.class, () -> autorService.update(autorUpdateFormDto));
        verify(autorRepository, times(1)).getById(autorUpdateFormDto.getId());
    }

    @Test
    void updateShouldReturnUpdatedAuthorWhenSuccessful() {
        var autorResponse = autorService.update(autorUpdateFormDto);

        assertEquals(autorAtualizado.getId(), autorResponse.getId());
        assertEquals(autorAtualizado.getNome(), autorResponse.getNome());
        assertEquals(autorAtualizado.getEmail(), autorResponse.getEmail());
        assertEquals(autorAtualizado.getDataNascimento(), autorResponse.getDataNascimento());
        assertEquals(autorAtualizado.getMiniCurriculo(), autorResponse.getMiniCurriculo());
        verify(autorRepository, times(1)).getById(autorUpdateFormDto.getId());
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void deleteShouldThrowNotFoundExceptionWhenInvalidId() {
        assertThrows(NotFoundException.class, () -> autorService.delete(invalidId));
        verify(autorRepository, times(1)).deleteById(invalidId);
    }

    @Test
    void deleteShouldThrowDomainExceptionWhenAuthorHasABook() {
        assertThrows(DomainException.class, () -> autorService.delete(dependentId));
        verify(autorRepository, times(1)).deleteById(dependentId);
    }

    @Test
    void deleteShouldDoNothingWhenSuccessful() {
        assertDoesNotThrow(() -> autorService.delete(validId));
        verify(autorRepository, times(1)).deleteById(validId);
    }

}
