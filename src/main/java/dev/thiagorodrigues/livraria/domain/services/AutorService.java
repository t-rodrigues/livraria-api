package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.AutorDetalhadoResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public Page<AutorResponseDto> listar(Pageable paginacao) {
        Page<Autor> autores = autorRepository.findAll(paginacao);

        return autores.map(autor -> modelMapper.map(autor, AutorResponseDto.class));
    }

    @Transactional(readOnly = true)
    public AutorDetalhadoResponseDto detalhar(long id) {
        var autor = autorRepository.findById(id).orElseThrow(() -> new NotFoundException("Autor inexistente: " + id));

        return modelMapper.map(autor, AutorDetalhadoResponseDto.class);
    }

    @Transactional
    public AutorDetalhadoResponseDto criar(AutorFormDto autorFormDto) {
        Autor autor = modelMapper.map(autorFormDto, Autor.class);

        autorRepository.save(autor);

        return modelMapper.map(autor, AutorDetalhadoResponseDto.class);
    }

    @Transactional
    public AutorDetalhadoResponseDto atualizar(AutorUpdateFormDto autorUpdateFormDto) {
        try {
            var autor = autorRepository.getById(autorUpdateFormDto.getId());
            atualizarDadosAutor(autor, autorUpdateFormDto);

            autorRepository.save(autor);

            return modelMapper.map(autor, AutorDetalhadoResponseDto.class);
        } catch (EntityNotFoundException e) {
            throw new DomainException("Autor inválido");
        }
    }

    public void deletar(Long id) {
        try {
            autorRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Autor inexistente: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DomainException("Autor não pode ser excluido");
        }
    }

    private void atualizarDadosAutor(Autor autor, AutorUpdateFormDto autorUpdateFormDto) {
        autor.setNome(autorUpdateFormDto.getNome());
        autor.setEmail(autorUpdateFormDto.getEmail());
        autor.setDataNascimento(autorUpdateFormDto.getDataNascimento());
        autor.setMiniCurriculo(autorUpdateFormDto.getMiniCurriculo());
    }

}
