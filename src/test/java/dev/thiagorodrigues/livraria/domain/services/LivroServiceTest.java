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

import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

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
    void detalharDeveriaLancarNotFoundExceptionQuandoIdInvalido() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> livroService.detalhar(100L));
        verify(livroRepository, times(1)).findById(anyLong());
    }

    @Test
    void detalharDeveriaRetornarLivroResponseDtoQuandoIdValido() {
        when(livroRepository.findById(anyLong())).thenReturn(Optional.of(livro));

        var livroDto = livroService.detalhar(1L);

        assertEquals(livroResponseDto.getId(), livroDto.getId());
        assertEquals(livroResponseDto.getTitulo(), livroDto.getTitulo());
        assertEquals(livroResponseDto.getDataLancamento(), livroDto.getDataLancamento());
        assertEquals(livroResponseDto.getAutor().getId(), livroDto.getAutor().getId());
        assertEquals(livroResponseDto.getNumeroPaginas(), livroDto.getNumeroPaginas());
    }

    @Test
    void criarDeveriaLancarDomainExceptionQuandoAutorIdInvalido() {
        doThrow(DataIntegrityViolationException.class).when(livroRepository).save(any(Livro.class));

        assertThrows(DomainException.class, () -> livroService.criar(livroFormDto));
        verify(autorRepository, times(1)).getById(anyLong());
    }

    @Test
    void criarDeveriaRetornarLivroQuandoDadosValidos() {
        when(autorRepository.getById(anyLong())).thenReturn(AutorFactory.criarAutor());
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        var livroResponse = livroService.criar(livroFormDto);

        assertEquals(livroResponseDto.getTitulo(), livroResponse.getTitulo());
        assertEquals(livroResponseDto.getNumeroPaginas(), livroResponse.getNumeroPaginas());
        assertEquals(livroResponseDto.getDataLancamento(), livroResponse.getDataLancamento());
        assertEquals(livroResponseDto.getAutor().getId(), livroResponse.getAutor().getId());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    void atualizarDeveriaLancarDomainException() {
        doThrow(EntityNotFoundException.class).when(livroRepository).getById(anyLong());

        assertThrows(DomainException.class, () -> livroService.atualizar(livroUpdateFormDto));
    }

    @Test
    void atualizarDeveriaLancarDomainExceptionQuandoAutorIdInvalido() {
        when(livroRepository.getById(anyLong())).thenReturn(livro);
        when(autorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> livroService.atualizar(livroUpdateFormDto));
    }

    @Test
    void atualizarDeveriaRetornarLivroAtualizadoQuandoDadosValidos() {
        when(livroRepository.getById(anyLong())).thenReturn(livro);
        when(autorRepository.findById(anyLong())).thenReturn(Optional.of(AutorFactory.criarAutor()));

        var livroResponse = livroService.atualizar(livroUpdateFormDto);

        assertEquals(livroAtualizado.getTitulo(), livroResponse.getTitulo());
        assertEquals(livroAtualizado.getNumeroPaginas(), livroResponse.getNumeroPaginas());
        assertEquals(livroAtualizado.getDataLancamento(), livroResponse.getDataLancamento());
        assertEquals(livroAtualizado.getAutor().getId(), livroResponse.getAutor().getId());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

}
