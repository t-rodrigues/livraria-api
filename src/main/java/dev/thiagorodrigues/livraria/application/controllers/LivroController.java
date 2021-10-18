package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.domain.services.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @GetMapping
    public Page<LivroResponseDto> listar(@PageableDefault(sort = "titulo", size = 15) Pageable paginacao) {
        return livroService.listar(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDto> detalhar(@PathVariable Long id) {
        LivroResponseDto livro = livroService.detalhar(id);

        return ResponseEntity.ok(livro);
    }

    @PostMapping
    public ResponseEntity<LivroResponseDto> criar(@RequestBody @Valid LivroFormDto livroFormDto,
            UriComponentsBuilder uriComponentsBuilder) {
        LivroResponseDto livro = livroService.criar(livroFormDto);

        URI location = uriComponentsBuilder.path("/livros/{id}").buildAndExpand(livro.getId()).toUri();

        return ResponseEntity.created(location).body(livro);
    }

}
