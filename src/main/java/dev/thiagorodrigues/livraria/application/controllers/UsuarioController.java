package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Usuarios")
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @ApiOperation("Criar novo usuário")
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> register(@RequestBody @Valid UsuarioFormDto usuarioFormDto) {
        var usuarioResponseDto = this.usuarioService.create(usuarioFormDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDto);
    }

    @ApiOperation("Detalhar um usuário")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> detail(@PathVariable Long id) {
        var userResponseDto = this.usuarioService.detail(id);

        return ResponseEntity.ok(userResponseDto);
    }

    @ApiOperation("Atualizar dados do usuário")
    @PutMapping
    public ResponseEntity<UsuarioResponseDto> update(@RequestBody @Valid UsuarioUpdateFormDto usuarioUpdateFormDto) {
        var userResponseDto = this.usuarioService.update(usuarioUpdateFormDto);

        return ResponseEntity.ok(userResponseDto);
    }

    @ApiOperation("Deletar usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.usuarioService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
