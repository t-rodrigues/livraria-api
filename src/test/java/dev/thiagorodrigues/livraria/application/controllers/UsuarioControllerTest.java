package dev.thiagorodrigues.livraria.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.domain.mocks.UsuarioFactory;
import dev.thiagorodrigues.livraria.domain.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioFormDto usuarioFormDto;
    private UsuarioResponseDto usuarioResponseDto;

    @BeforeEach
    void setup() {
        usuarioFormDto = UsuarioFactory.createUserFormDto();
        usuarioResponseDto = UsuarioFactory.createUserResponseDto();

        when(usuarioService.criar(any(UsuarioFormDto.class))).thenReturn(usuarioResponseDto);
    }

    @Test
    void shouldReturnBadRequestWhenInvalidData() throws Exception {
        String invalidData = "{}";

        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUserWhenValidData() throws Exception {
        var validData = objectMapper.writeValueAsString(usuarioFormDto);

        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists());
    }

}
