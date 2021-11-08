package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.AuthFormDto;
import dev.thiagorodrigues.livraria.application.dtos.TokenDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.main.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtil;

    public TokenDto authenticate(AuthFormDto authFormDto) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(authFormDto.getEmail(),
                authFormDto.getSenha());

        authentication = this.authenticationManager.authenticate(authentication);

        var usuario = (Usuario) authentication.getPrincipal();
        var token = jwtTokenUtil.generateJwtToken(usuario.getId().toString());

        return new TokenDto(token);
    }

}
