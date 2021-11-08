package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroUpdateFormDto;
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

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class LivroService {

    private ModelMapper modelMapper = new ModelMapper();

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    @Transactional(readOnly = true)
    public Page<LivroResponseDto> list(Pageable paginacao) {
        var livros = livroRepository.findAll(paginacao);

        return livros.map(livro -> modelMapper.map(livro, LivroResponseDto.class));
    }

    @Transactional(readOnly = true)
    public LivroResponseDto detail(Long id) {
        var livro = livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado: " + id));

        return modelMapper.map(livro, LivroResponseDto.class);
    }

    @Transactional
    public LivroResponseDto create(LivroFormDto livroFormDto) {
        try {
            var livro = modelMapper.map(livroFormDto, Livro.class);
            livro.setAutor(autorRepository.getById(livroFormDto.getAutorId()));

            livroRepository.save(livro);

            return modelMapper.map(livro, LivroResponseDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new DomainException("Autor inválido");
        }
    }

    @Transactional
    public LivroResponseDto update(LivroUpdateFormDto livroUpdateFormDto) {
        try {
            var livro = livroRepository.getById(livroUpdateFormDto.getId());

            updateBookData(livro, livroUpdateFormDto);
            livroRepository.save(livro);

            return modelMapper.map(livro, LivroResponseDto.class);
        } catch (EntityNotFoundException e) {
            throw new DomainException("Livro inválido: " + livroUpdateFormDto.getId());
        }
    }

    public void delete(Long id) {
        try {
            livroRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException("Livro inexistente");
        }
    }

    private void updateBookData(Livro livro, LivroUpdateFormDto livroUpdateFormDto) {
        var autor = autorRepository.findById(livroUpdateFormDto.getAutorId())
                .orElseThrow(() -> new DomainException("Autor inexistente"));

        livro.setTitulo(livroUpdateFormDto.getTitulo());
        livro.setDataLancamento(livroUpdateFormDto.getDataLancamento());
        livro.setNumeroPaginas(livroUpdateFormDto.getNumeroPaginas());
        livro.setAutor(autor);
    }

}
