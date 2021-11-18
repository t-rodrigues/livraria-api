package dev.thiagorodrigues.livraria.domain.services;

import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateProfileDto;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.infra.providers.mail.MailService;
import dev.thiagorodrigues.livraria.infra.repositories.PerfilRepository;
import dev.thiagorodrigues.livraria.infra.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public UsuarioResponseDto detail(Long id) {
        var usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));

        return modelMapper.map(usuario, UsuarioResponseDto.class);
    }

    @Transactional
    public UsuarioResponseDto create(UsuarioFormDto usuarioFormDto) {
        try {
            var usuario = modelMapper.map(usuarioFormDto, Usuario.class);

            if (checkIfEmailAlreadyTaken(usuario.getEmail())) {
                throw new DomainException("Email already in use");
            }

            usuario.setSenha(bCryptPasswordEncoder.encode(usuarioFormDto.getSenha()));
            usuario.adicionarPerfil(perfilRepository.getByNome("ROLE_USER"));
            sendEmail(usuario);
            this.usuarioRepository.save(usuario);

            return modelMapper.map(usuario, UsuarioResponseDto.class);
        } catch (Exception e) {
            throw new DomainException(e.getMessage());
        }
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

    @Transactional
    public UsuarioResponseDto updateProfiles(UsuarioUpdateProfileDto updateProfileDto) {
        try {
            var usuario = this.usuarioRepository.getById(updateProfileDto.getId());

            usuario.getPerfis().clear();
            var perfis = this.perfilRepository.findAllById(updateProfileDto.getProfiles());
            perfis.forEach(usuario::adicionarPerfil);
            this.usuarioRepository.save(usuario);

            return modelMapper.map(usuario, UsuarioResponseDto.class);
        } catch (EntityNotFoundException e) {
            throw new DomainException("User not found");
        }
    }

    public void delete(Long id) {
        try {
            this.usuarioRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void updateData(Usuario usuario, UsuarioUpdateFormDto usuarioUpdateFormDto) {
        usuario.setNome(usuarioUpdateFormDto.getNome());
        usuario.setEmail(usuarioUpdateFormDto.getEmail());
    }

    private boolean checkIfEmailAlreadyTaken(String email) {
        return this.usuarioRepository.existsByEmail(email);
    }

    private void sendEmail(Usuario usuario) {
        var recipient = String.format("%s <%s>", usuario.getNome(), usuario.getEmail());
        var subject = "Livraria - Bem vindo(a)";
        var content = String.format("Olá, %s!\n\n" + "Seu cadastro foi realizado com sucesso!"
                + "\n\nPara acessar sua conta no Sistema Livraria:"
                + "\nLogin: %s\nSenha: informada no momento do cadastro.\n\n" + "Atenciosamente,\nEquipe Livraria.",
                usuario.getNome(), usuario.getEmail());

        this.mailService.sendMail(recipient, subject, content);
    }

}
