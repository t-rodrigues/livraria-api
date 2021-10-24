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
    public Page<AutorResponseDto> list(@PageableDefault(size = 15) Pageable paginacao) {
        return autorService.list(paginacao);
    }

    @ApiOperation("Detalhar um autor")
    @GetMapping("/{id}")
    public ResponseEntity<AutorDetalhadoResponseDto> detail(@PathVariable Long id) {
        var autorDetalhadoResponseDto = autorService.detail(id);

        return ResponseEntity.ok(autorDetalhadoResponseDto);
    }

    @ApiOperation("Criar novo autor")
    @PostMapping
    public ResponseEntity<AutorDetalhadoResponseDto> create(@RequestBody @Valid AutorFormDto autorFormDto,
            UriComponentsBuilder uriComponentsBuilder) {
        var autorResponseDto = autorService.create(autorFormDto);

        URI location = uriComponentsBuilder.path("/autores/{id}").buildAndExpand(autorResponseDto.getId()).toUri();

        return ResponseEntity.created(location).body(autorResponseDto);
    }

    @ApiOperation("Atualizar autor")
    @PutMapping
    public ResponseEntity<AutorDetalhadoResponseDto> update(@RequestBody @Valid AutorUpdateFormDto autorUpdateFormDto) {
        var autorDetalhadoResponseDto = autorService.update(autorUpdateFormDto);

        return ResponseEntity.ok(autorDetalhadoResponseDto);
    }

    @ApiOperation("Deletar autor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        autorService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
