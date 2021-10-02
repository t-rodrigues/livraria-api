package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.domain.services.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    public Page<LivroResponseDto> getLivros(@PageableDefault(sort = "titulo", size = 15) Pageable paginacao) {
        return livroService.getLivros(paginacao);
    }

    @PostMapping
    public void createLivro(@RequestBody @Valid LivroFormDto livroFormDto) {
        livroService.createLivro(livroFormDto);
    }

}
