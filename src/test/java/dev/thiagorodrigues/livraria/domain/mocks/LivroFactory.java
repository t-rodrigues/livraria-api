package dev.thiagorodrigues.livraria.domain.mocks;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class LivroFactory {

    private static ModelMapper modelMapper = new ModelMapper();

    public static Livro criarLivro() {
        return new Livro(1L, "Lorem Ipsum", LocalDate.parse("2020-12-20"), 100, AutorFactory.criarAutor());
    }

    public static LivroResponseDto criarLivroResponseDto() {
        return modelMapper.map(criarLivro(), LivroResponseDto.class);
    }

    public static LivroFormDto criarLivroFormDto() {
        return modelMapper.map(criarLivro(), LivroFormDto.class);
    }

}
