package dev.thiagorodrigues.livraria.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import dev.thiagorodrigues.livraria.infra.repositories.LivroRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    private ModelMapper mapper = new ModelMapper();

    public List<LivroResponseDto> getLivros() {
        List<Livro> livros = livroRepository.findAll();

        return livros.stream().map(livro -> mapper.map(livro, LivroResponseDto.class)).collect(Collectors.toList());
    }

    public void createLivro(LivroFormDto livroFormDto) {
        Livro livro = mapper.map(livroFormDto, Livro.class);
        livro.setAutor(autorRepository.findById(livroFormDto.getAutor().getId()));

        livroRepository.save(livro);
    }
}
