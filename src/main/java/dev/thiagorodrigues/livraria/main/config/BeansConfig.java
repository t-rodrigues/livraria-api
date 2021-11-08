package dev.thiagorodrigues.livraria.main.config;

import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        modelMapper.createTypeMap(LivroFormDto.class, Livro.class).addMappings(mapper -> mapper.skip(Livro::setId));

        modelMapper.createTypeMap(UsuarioFormDto.class, Usuario.class)
                .addMappings(mapper -> mapper.skip(Usuario::setId));

        return modelMapper;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
