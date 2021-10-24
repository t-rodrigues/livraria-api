package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutor;
import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutorDto;
import dev.thiagorodrigues.livraria.domain.services.RelatorioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Relatórios")
@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @ApiOperation("Livros por Autor com Interface")
    @GetMapping("/livrosPorAutor")
    public List<RelatorioLivrosPorAutor> relatorioLivrosPorAutor() {
        return relatorioService.relatorioLivrosPorAutor();
    }

    @ApiOperation("Livros por Autor com classe Dto")
    @GetMapping("/livrosPorAutorDto")
    public List<RelatorioLivrosPorAutorDto> relatorioLivrosPorAutorDto() {
        return relatorioService.relatorioLivrosPorAutorDto();
    }

}
