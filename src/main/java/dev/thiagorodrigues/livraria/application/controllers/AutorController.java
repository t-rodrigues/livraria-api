package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorResponseDto;
import dev.thiagorodrigues.livraria.domain.services.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    public Page<AutorResponseDto> getAutores(@PageableDefault(size = 15) Pageable paginacao) {
        return autorService.getAutores(paginacao);
    }

    @PostMapping
    public void createAutor(@RequestBody @Valid AutorFormDto autorFormDto) {
        autorService.createAutor(autorFormDto);
    }

}
