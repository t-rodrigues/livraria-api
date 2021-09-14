package dev.thiagorodrigues.livraria.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    private ModelMapper mapper = new ModelMapper();

    public List<AutorResponseDto> getAutores() {
        var autores = autorRepository.findAll();

        return autores.stream().map(autor -> mapper.map(autor, AutorResponseDto.class)).collect(Collectors.toList());
    }

    public void createAutor(AutorFormDto autorFormDto) {
        var autor = mapper.map(autorFormDto, Autor.class);

        autorRepository.save(autor);
    }

}
