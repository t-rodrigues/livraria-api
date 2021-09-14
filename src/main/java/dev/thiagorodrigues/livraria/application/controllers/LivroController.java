package dev.thiagorodrigues.livraria.application.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.domain.services.LivroService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    public List<LivroResponseDto> getLivros() {
        return livroService.getLivros();
    }

    @PostMapping
    public void createLivro(@RequestBody @Valid LivroFormDto livroFormDto) {
        livroService.createLivro(livroFormDto);
    }
}
