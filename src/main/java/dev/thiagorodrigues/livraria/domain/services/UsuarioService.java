package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.infra.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public UsuarioResponseDto detail(Long id) {
        var usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));

        return modelMapper.map(usuario, UsuarioResponseDto.class);
    }

    @Transactional
    public UsuarioResponseDto create(UsuarioFormDto usuarioFormDto) {
        var usuario = modelMapper.map(usuarioFormDto, Usuario.class);

        if (checkIfEmailAlreadyTaken(usuario.getEmail())) {
            throw new DomainException("Email already in use");
        }

        this.usuarioRepository.save(usuario);

        return modelMapper.map(usuario, UsuarioResponseDto.class);
    }

    @Transactional
    public UsuarioResponseDto update(UsuarioUpdateFormDto usuarioUpdateFormDto) {
        try {
            var usuario = this.usuarioRepository.getById(usuarioUpdateFormDto.getId());

            if (!usuario.getEmail().equals(usuarioUpdateFormDto.getEmail())
                    && checkIfEmailAlreadyTaken(usuarioUpdateFormDto.getEmail())) {
                throw new DomainException("Email already taken");
            }

            updateData(usuario, usuarioUpdateFormDto);
            this.usuarioRepository.save(usuario);

            return modelMapper.map(usuario, UsuarioResponseDto.class);
        } catch (EntityNotFoundException e) {
            throw new DomainException("Invalid user data");
        }
    }

    public void delete(Long id) {
        try {
            this.usuarioRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User not found");
        }
    }

    private void updateData(Usuario usuario, UsuarioUpdateFormDto usuarioUpdateFormDto) {
        usuario.setNome(usuarioUpdateFormDto.getNome());
        usuario.setEmail(usuarioUpdateFormDto.getEmail());
    }

    private boolean checkIfEmailAlreadyTaken(String email) {
        return this.usuarioRepository.existsByEmail(email);
    }

}
