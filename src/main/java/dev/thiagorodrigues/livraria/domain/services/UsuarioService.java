package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.infra.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public UsuarioResponseDto criar(UsuarioFormDto usuarioFormDto) {
        var usuario = modelMapper.map(usuarioFormDto, Usuario.class);

        if (checkIfEmailAlreadyTaken(usuario.getEmail())) {
            throw new DomainException("Email already in use");
        }

        this.usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioResponseDto.class);
    }

    private boolean checkIfEmailAlreadyTaken(String email) {
        return this.usuarioRepository.existsByEmail(email);
    }

}
