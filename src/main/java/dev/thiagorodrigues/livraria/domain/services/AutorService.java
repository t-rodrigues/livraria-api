package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    private ModelMapper mapper = new ModelMapper();

    public List<AutorResponseDto> getAutores() {
        List<Autor> autores = autorRepository.findAll();

        return autores.stream().map(autor -> mapper.map(autor, AutorResponseDto.class)).collect(Collectors.toList());
    }

    public void createAutor(AutorFormDto autorFormDto) {
        Autor autor = mapper.map(autorFormDto, Autor.class);

        autorRepository.save(autor);
    }

}
