package dev.thiagorodrigues.livraria.domain.mocks;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class LivroFactory {

    private static ModelMapper modelMapper = new ModelMapper();

    public static Livro criarLivro() {
        return new Livro(1L, "Lorem Ipsum", LocalDate.of(2020, 12, 20), 100, AutorFactory.criarAutor());
    }

    public static Livro criarLivroSemId() {
        return new Livro(null, "Lorem Ipsum", LocalDate.of(2020, 12, 20), 100, AutorFactory.criarAutor());
    }

    public static Livro criarLivro(String titulo, LocalDate dataLancamento, Integer numeroPaginas, Autor autor) {
        return new Livro(null, titulo, dataLancamento, numeroPaginas, autor);
    }

    public static Livro criarLivroAtualizado() {
        return new Livro(1L, "Updated Lorem Ipsum", LocalDate.of(2021, 10, 20), 120, AutorFactory.criarAutor());
    }

    public static LivroResponseDto criarLivroResponseDto() {
        return modelMapper.map(criarLivro(), LivroResponseDto.class);
    }

    public static LivroResponseDto criarLivroAtualizadoResponseDto() {
        return modelMapper.map(criarLivroAtualizado(), LivroResponseDto.class);
    }

    public static LivroFormDto criarLivroFormDto() {
        return modelMapper.map(criarLivro(), LivroFormDto.class);
    }

    public static LivroUpdateFormDto criarLivroUpdateFormDto() {
        return modelMapper.map(criarLivroAtualizado(), LivroUpdateFormDto.class);
    }

}
