package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutor;
import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutorDto;
import dev.thiagorodrigues.livraria.domain.services.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/livrosPorAutor")
    public List<RelatorioLivrosPorAutor> relatorioLivrosPorAutor() {
        return relatorioService.relatorioLivrosPorAutor();
    }

    @GetMapping("/livrosPorAutorDto")
    public List<RelatorioLivrosPorAutorDto> relatorioLivrosPorAutorDto() {
        return relatorioService.relatorioLivrosPorAutorDto();
    }

}
