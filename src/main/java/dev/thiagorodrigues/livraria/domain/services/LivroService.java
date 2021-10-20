package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import dev.thiagorodrigues.livraria.infra.repositories.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LivroService {

    private ModelMapper mapper = new ModelMapper();

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    @Transactional(readOnly = true)
    public Page<LivroResponseDto> listar(Pageable paginacao) {
        Page<Livro> livros = livroRepository.findAll(paginacao);

        return livros.map(livro -> mapper.map(livro, LivroResponseDto.class));
    }

    @Transactional(readOnly = true)
    public LivroResponseDto detalhar(Long id) {
        var livro = livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado: " + id));

        return mapper.map(livro, LivroResponseDto.class);
    }

    @Transactional
    public LivroResponseDto criar(LivroFormDto livroFormDto) {
        try {
            Livro livro = mapper.map(livroFormDto, Livro.class);
            livro.setId(null);
            livro.setAutor(autorRepository.getById(livroFormDto.getAutorId()));

            livroRepository.save(livro);

            return mapper.map(livro, LivroResponseDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DomainException("Autor inválido");
        }
    }

}
