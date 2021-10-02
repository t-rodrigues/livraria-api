package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutor;
import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutorDto;
import dev.thiagorodrigues.livraria.infra.repositories.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RelatorioService {

    private final LivroRepository livroRepository;

    public List<RelatorioLivrosPorAutor> relatorioLivrosPorAutor() {
        return livroRepository.relatorioLivrosPorAutor();
    }

    public List<RelatorioLivrosPorAutorDto> relatorioLivrosPorAutorDto() {
        return livroRepository.relatorioLivrosPorAutorDto();
    }

}
