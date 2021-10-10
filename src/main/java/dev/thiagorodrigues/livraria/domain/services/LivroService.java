package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import dev.thiagorodrigues.livraria.infra.repositories.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    private ModelMapper mapper = new ModelMapper();

    public Page<LivroResponseDto> getLivros(Pageable paginacao) {
        Page<Livro> livros = livroRepository.findAll(paginacao);

        return livros.map(livro -> mapper.map(livro, LivroResponseDto.class));
    }

    public LivroResponseDto createLivro(LivroFormDto livroFormDto) {
        Livro livro = mapper.map(livroFormDto, Livro.class);
        livro.setAutor(autorRepository.getById(livroFormDto.getAutorId()));
        livro.setId(null);

        livroRepository.save(livro);

        return mapper.map(livro, LivroResponseDto.class);
    }

}
