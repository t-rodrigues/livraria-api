package dev.thiagorodrigues.livraria.application.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorResponseDto;
import dev.thiagorodrigues.livraria.domain.services.AutorService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @GetMapping
    public List<AutorResponseDto> getAutores() {
        return autorService.getAutores();
    }

    @PostMapping
    public void createAutor(@RequestBody AutorFormDto autorFormDto) {
        autorService.createAutor(autorFormDto);
    }

}
