package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.AutorDetalhadoResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.services.AutorService;
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

@Api(tags = "Autores")
@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @ApiOperation("Listar autores")
    @GetMapping
    public Page<AutorResponseDto> listar(@PageableDefault(size = 15) Pageable paginacao) {
        return autorService.listar(paginacao);
    }

    @ApiOperation("Detalhar um autor")
    @GetMapping("/{id}")
    public ResponseEntity<AutorDetalhadoResponseDto> detalhar(@PathVariable Long id) {
        var autorDetalhadoResponseDto = autorService.detalhar(id);

        return ResponseEntity.ok(autorDetalhadoResponseDto);
    }

    @ApiOperation("Criar novo autor")
    @PostMapping
    public ResponseEntity<AutorDetalhadoResponseDto> criar(@RequestBody @Valid AutorFormDto autorFormDto,
            UriComponentsBuilder uriComponentsBuilder) {
        var autorResponseDto = autorService.criar(autorFormDto);

        URI location = uriComponentsBuilder.path("/autores/{id}").buildAndExpand(autorResponseDto.getId()).toUri();

        return ResponseEntity.created(location).body(autorResponseDto);
    }

    @ApiOperation("Atualizar autor")
    @PutMapping
    public ResponseEntity<AutorDetalhadoResponseDto> atualizar(
            @RequestBody @Valid AutorUpdateFormDto autorUpdateFormDto) {
        var autorDetalhadoResponseDto = autorService.atualizar(autorUpdateFormDto);

        return ResponseEntity.ok(autorDetalhadoResponseDto);
    }

    @ApiOperation("Deletar autor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorService.deletar(id);

        return ResponseEntity.noContent().build();
    }

}
