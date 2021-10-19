package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.AutorDetalhadoResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.domain.mocks.AutorFactory;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autor = AutorFactory.criarAutor();
    private Autor autorAtualizado = AutorFactory.criarAutorAtualizado();
    private AutorFormDto autorFormDto = AutorFactory.criarAutorFormDto();
    private AutorDetalhadoResponseDto autorDetalhadoResponseDto = AutorFactory.criarAutorDetalhadoResponseDto();
    private AutorUpdateFormDto autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();

    private final long validId = 1L;
    private final long invalidId = 100L;

    @Test
    void criarDeveriaRetornarUmAutor() {
        when(autorRepository.save(any())).thenReturn(autor);

        var autorDetalhadoResponseDto = autorService.criar(autorFormDto);

        assertEquals(autor.getNome(), autorDetalhadoResponseDto.getNome());
        assertEquals(autor.getEmail(), autorDetalhadoResponseDto.getEmail());
        assertEquals(autor.getDataNascimento(), autorDetalhadoResponseDto.getDataNascimento());
        assertEquals(autor.getMiniCurriculo(), autorDetalhadoResponseDto.getMiniCurriculo());
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void detalharDeveriaLancarNotFoundExceptionQuandoIdInvalido() {
        when(autorRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> autorService.detalhar(invalidId));
        verify(autorRepository, times(1)).findById(invalidId);
    }

    @Test
    void detalharDeveriaRetornarAutorDetalhadoDtoQuandoIdValido() {
        when(autorRepository.findById(validId)).thenReturn(Optional.of(autor));
        var autorResponse = autorService.detalhar(validId);

        assertNotNull(autorResponse);
        assertEquals(autorDetalhadoResponseDto.getId(), autorResponse.getId());
        verify(autorRepository, times(1)).findById(validId);
    }

    @Test
    void atualizarDeveriaLancarNotFoundExceptionQuandoIdInvalido() {
        doThrow(EntityNotFoundException.class).when(autorRepository).getById(anyLong());

        assertThrows(DomainException.class, () -> autorService.atualizar(autorUpdateFormDto));
        verify(autorRepository, times(1)).getById(autorUpdateFormDto.getId());
    }

    @Test
    void atualizarDeveriaRetornarAutorAtualizado() {
        when(autorRepository.getById(anyLong())).thenReturn(AutorFactory.criarAutor());
        var autorResponse = autorService.atualizar(autorUpdateFormDto);

        assertEquals(autorAtualizado.getId(), autorResponse.getId());
        assertEquals(autorAtualizado.getNome(), autorResponse.getNome());
        assertEquals(autorAtualizado.getEmail(), autorResponse.getEmail());
        assertEquals(autorAtualizado.getDataNascimento(), autorResponse.getDataNascimento());
        assertEquals(autorAtualizado.getMiniCurriculo(), autorResponse.getMiniCurriculo());
        verify(autorRepository, times(1)).getById(autorUpdateFormDto.getId());
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void deletarDeveriaLancarNotFoundExceptionQuandoIdInvalido() {
        doThrow(EmptyResultDataAccessException.class).when(autorRepository).deleteById(invalidId);

        assertThrows(NotFoundException.class, () -> autorService.deletar(invalidId));
        verify(autorRepository, times(1)).deleteById(invalidId);
    }

    @Test
    void deletarDeveriaLancarDomainExceptionQuandoAutorDependente() {
        doThrow(DataIntegrityViolationException.class).when(autorRepository).deleteById(validId);

        assertThrows(DomainException.class, () -> autorService.deletar(validId));
        verify(autorRepository, times(1)).deleteById(validId);
    }

    @Test
    void DeleteDeveriaFazerNadaQuandoAutorPodeSerDeletado() {
        assertDoesNotThrow(() -> autorService.deletar(validId));
        verify(autorRepository, times(1)).deleteById(validId);
    }

}
