package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    private ModelMapper mapper = new ModelMapper();

    public Page<AutorResponseDto> getAutores(Pageable paginacao) {
        Page<Autor> autores = autorRepository.findAll(paginacao);

        return autores.map(autor -> mapper.map(autor, AutorResponseDto.class));
    }

    public AutorResponseDto createAutor(AutorFormDto autorFormDto) {
        Autor autor = mapper.map(autorFormDto, Autor.class);

        autorRepository.save(autor);

        return mapper.map(autor, AutorResponseDto.class);
    }

}
