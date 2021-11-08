package dev.thiagorodrigues.livraria.application.controllers;

import dev.thiagorodrigues.livraria.application.dtos.AuthFormDto;
import dev.thiagorodrigues.livraria.application.dtos.TokenDto;
import dev.thiagorodrigues.livraria.domain.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Autenticação")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation("Autenticar usuário")
    @PostMapping
    public ResponseEntity<TokenDto> auth(@RequestBody @Valid AuthFormDto authFormDto) {
        var token = authService.authenticate(authFormDto);

        return ResponseEntity.ok(token);
    }

}
