package dev.thiagorodrigues.livraria.domain.mocks;

import dev.thiagorodrigues.livraria.application.dtos.AutorDetalhadoResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

public class AutorFactory {

    private static ModelMapper modelMapper = new ModelMapper();

    public static Autor criarAutor() {
        return new Autor(1L, "John Doe", "john@mail.com", LocalDate.parse("1990-12-29"), "Curriculo");
    }

    public static Autor criarAutor(String nome, String email, LocalDate dataNascimento, String miniCurriculo) {
        return new Autor(null, nome, email, dataNascimento, miniCurriculo);
    }

    public static Autor criarAutorAtualizado() {
        return new Autor(1L, "Updated John Doe", "updated@mail.com", LocalDate.parse("1990-12-29"),
                "Updated Curriculo");
    }

    public static AutorFormDto criarAutorFormDto() {
        return modelMapper.map(criarAutor(), AutorFormDto.class);
    }

    public static AutorDetalhadoResponseDto criarAutorDetalhadoResponseDto() {
        return modelMapper.map(criarAutor(), AutorDetalhadoResponseDto.class);
    }

    public static AutorUpdateFormDto criarAutorUpdateFormDto() {
        return modelMapper.map(criarAutorAtualizado(), AutorUpdateFormDto.class);
    }

}
