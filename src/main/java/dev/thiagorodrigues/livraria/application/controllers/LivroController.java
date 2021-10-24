package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.services.LivroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import java.net.URI;

@Api(tags = "Livros")
@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @ApiOperation("Listar livros")
    @GetMapping
    public Page<LivroResponseDto> list(@PageableDefault(sort = "titulo", size = 15) Pageable paginacao) {
        return livroService.list(paginacao);
    }

    @ApiOperation("Detalhar livro")
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDto> detail(@PathVariable Long id) {
        LivroResponseDto livro = livroService.detail(id);

        return ResponseEntity.ok(livro);
    }

    @ApiOperation("Cadastrar novo livro")
    @PostMapping
    public ResponseEntity<LivroResponseDto> create(@RequestBody @Valid LivroFormDto livroFormDto,
            UriComponentsBuilder uriComponentsBuilder) {
        LivroResponseDto livro = livroService.create(livroFormDto);

        URI location = uriComponentsBuilder.path("/livros/{id}").buildAndExpand(livro.getId()).toUri();

        return ResponseEntity.created(location).body(livro);
    }

    @ApiOperation("Atualizar livro")
    @PutMapping
    public ResponseEntity<LivroResponseDto> update(@RequestBody @Valid LivroUpdateFormDto livroUpdateFormDto) {
        LivroResponseDto livroResponseDto = livroService.update(livroUpdateFormDto);

        return ResponseEntity.ok(livroResponseDto);
    }

    @ApiOperation("Deletar livro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livroService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
