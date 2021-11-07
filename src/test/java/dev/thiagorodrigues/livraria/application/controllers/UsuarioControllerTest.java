package dev.thiagorodrigues.livraria.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioFormDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.UsuarioUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private long existingId = 1L;
    private long nonExistingId = 100L;
    private UsuarioResponseDto usuarioResponseDto;

    @BeforeEach
    void setup() {
        usuarioResponseDto = UsuarioFactory.createUserResponseDto();
    }

    @Test
    void createShouldReturnBadRequestWhenInvalidData() throws Exception {
        String invalidData = "{}";

        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createShouldReturnUserWhenValidData() throws Exception {
        var usuarioFormDto = UsuarioFactory.createUserFormDto();
        var validData = objectMapper.writeValueAsString(usuarioFormDto);

        when(usuarioService.create(any(UsuarioFormDto.class))).thenReturn(usuarioResponseDto);

        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateShouldReturnNotFoundWhenInvalidId() throws Exception {
        var usuarioUpdateFormDto = UsuarioFactory.createUserUpdateFormDto();
        usuarioUpdateFormDto.setId(nonExistingId);
        var invalidData = objectMapper.writeValueAsString(usuarioUpdateFormDto);

        when(usuarioService.update(any(UsuarioUpdateFormDto.class))).thenThrow(NotFoundException.class);

        mockMvc.perform(put("/usuarios").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturnUpdatedUserWhenValidData() throws Exception {
        var updateFormDto = UsuarioFactory.createUserUpdateFormDto();
        var validData = objectMapper.writeValueAsString(updateFormDto);

        when(usuarioService.update(any(UsuarioUpdateFormDto.class)))
                .thenReturn(UsuarioFactory.createUserUpdatedResponseDto());

        mockMvc.perform(put("/usuarios").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value(updateFormDto.getNome()))
                .andExpect(jsonPath("$.email").value(updateFormDto.getEmail()));
    }

    @Test
    void detailShouldReturnNotFoundWhenInvalidId() throws Exception {
        when(usuarioService.detail(nonExistingId)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/usuarios/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void detailShouldReturnUserWhenValidId() throws Exception {
        when(usuarioService.detail(existingId)).thenReturn(usuarioResponseDto);

        mockMvc.perform(get("/usuarios/{id}", existingId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioResponseDto.getId()));
    }

    @Test
    void deleteShouldReturnNotFoundWhenInvalidId() throws Exception {
        doThrow(NotFoundException.class).when(usuarioService).delete(nonExistingId);

        mockMvc.perform(delete("/usuarios/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnNoContentWhenSuccessFull() throws Exception {
        doNothing().when(usuarioService).delete(existingId);

        mockMvc.perform(delete("/usuarios/{id}", existingId)).andExpect(status().isNoContent());
    }

}
