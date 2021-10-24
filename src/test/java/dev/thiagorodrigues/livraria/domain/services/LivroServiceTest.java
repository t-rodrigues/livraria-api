package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.domain.mocks.AutorFactory;
import dev.thiagorodrigues.livraria.domain.mocks.LivroFactory;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import dev.thiagorodrigues.livraria.infra.repositories.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    private final Long validLivroId = 1L;
    private final Long invalidLivroId = 100L;
    private final Long validAutorId = 1L;
    private final Long invalidAutorId = 100L;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;
    private Livro livroAtualizado;
    private LivroResponseDto livroResponseDto;
    private LivroFormDto livroFormDto;
    private LivroUpdateFormDto livroUpdateFormDto;

    @BeforeEach
    void setUp() {
        livro = LivroFactory.criarLivro();
        livroAtualizado = LivroFactory.criarLivroAtualizado();
        livroResponseDto = LivroFactory.criarLivroResponseDto();
        livroFormDto = LivroFactory.criarLivroFormDto();
        livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDto();
    }

    @Test
    void detailShouldThrowNotFoundExceptionWhenInvalidId() {
        when(livroRepository.findById(invalidLivroId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> livroService.detail(invalidLivroId));
        verify(livroRepository, times(1)).findById(invalidLivroId);
    }

    @Test
    void detailShouldReturnBookWhenSuccessful() {
        when(livroRepository.findById(validLivroId)).thenReturn(Optional.of(livro));

        var livroDto = livroService.detail(validLivroId);

        assertEquals(livroResponseDto.getId(), livroDto.getId());
        assertEquals(livroResponseDto.getTitulo(), livroDto.getTitulo());
        assertEquals(livroResponseDto.getDataLancamento(), livroDto.getDataLancamento());
        assertEquals(livroResponseDto.getAutor().getId(), livroDto.getAutor().getId());
        assertEquals(livroResponseDto.getNumeroPaginas(), livroDto.getNumeroPaginas());
    }

    @Test
    void createShouldThrowDomainExceptionWhenInvalidAuthorId() {
        doThrow(DataIntegrityViolationException.class).when(livroRepository).save(any(Livro.class));
        livroFormDto.setAutorId(invalidAutorId);

        assertThrows(DomainException.class, () -> livroService.create(livroFormDto));
        verify(autorRepository, times(1)).getById(invalidAutorId);
    }

    @Test
    void createShouldReturnBookWhenSuccessful() {
        when(autorRepository.getById(validAutorId)).thenReturn(AutorFactory.criarAutor());
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        var livroResponse = livroService.create(livroFormDto);

        assertEquals(livroResponseDto.getTitulo(), livroResponse.getTitulo());
        assertEquals(livroResponseDto.getNumeroPaginas(), livroResponse.getNumeroPaginas());
        assertEquals(livroResponseDto.getDataLancamento(), livroResponse.getDataLancamento());
        assertEquals(livroResponseDto.getAutor().getId(), livroResponse.getAutor().getId());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    void updateShouldThrowDomainExceptionWhenInvalidId() {
        doThrow(EntityNotFoundException.class).when(livroRepository).getById(anyLong());

        assertThrows(DomainException.class, () -> livroService.update(livroUpdateFormDto));
    }

    @Test
    void updateShouldThrowsDomainExceptionWhenInvalidAuthorId() {
        when(livroRepository.getById(validLivroId)).thenReturn(livro);
        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> livroService.update(livroUpdateFormDto));
    }

    @Test
    void updateShouldReturnUpdatedBookWhenSuccessful() {
        when(livroRepository.getById(anyLong())).thenReturn(livro);
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(AutorFactory.criarAutor()));

        var livroResponse = livroService.update(livroUpdateFormDto);

        assertEquals(livroAtualizado.getTitulo(), livroResponse.getTitulo());
        assertEquals(livroAtualizado.getNumeroPaginas(), livroResponse.getNumeroPaginas());
        assertEquals(livroAtualizado.getDataLancamento(), livroResponse.getDataLancamento());
        assertEquals(livroAtualizado.getAutor().getId(), livroResponse.getAutor().getId());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    void deleteShouldThrowNotFoundExceptionWhenInvalidId() {
        doThrow(EmptyResultDataAccessException.class).when(livroRepository).deleteById(anyLong());

        assertThrows(NotFoundException.class, () -> livroService.delete(1L));
    }

    @Test
    void deleteShouldReturnNothingWhenSuccessfull() {
        doNothing().when(livroRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> livroService.delete(1L));
        verify(livroRepository, times(1)).deleteById(anyLong());
    }

}
