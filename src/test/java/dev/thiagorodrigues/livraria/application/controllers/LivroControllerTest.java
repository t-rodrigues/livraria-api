package dev.thiagorodrigues.livraria.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.application.dtos.LivroFormDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.LivroUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.domain.mocks.LivroFactory;
import dev.thiagorodrigues.livraria.domain.services.LivroService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LivroController.class)
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivroService livroService;

    private Long existingId = 1L;
    private Long nonExistingId = 10L;
    private LivroResponseDto livroResponseDto;
    private LivroResponseDto livroAtualizaResponseDto;
    private LivroFormDto livroFormDto;
    private LivroUpdateFormDto livroUpdateFormDto;

    @BeforeEach
    void setUp() {
        livroResponseDto = LivroFactory.criarLivroResponseDto();
        livroAtualizaResponseDto = LivroFactory.criarLivroAtualizadoResponseDto();
        livroFormDto = LivroFactory.criarLivroFormDto();
        livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDto();

        when(livroService.detalhar(existingId)).thenReturn(livroResponseDto);
        when(livroService.detalhar(nonExistingId)).thenThrow(NotFoundException.class);
        when(livroService.criar(any(LivroFormDto.class))).thenReturn(livroResponseDto);
        when(livroService.atualizar(any(LivroUpdateFormDto.class))).thenReturn(livroAtualizaResponseDto);
        doThrow(NotFoundException.class).when(livroService).deletar(nonExistingId);
        doNothing().when(livroService).deletar(existingId);
    }

    @Test
    void detalharShouldReturnAnBookWhenIdExists() throws Exception {
        mockMvc.perform(get("/livros/{id}", existingId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(livroResponseDto.getId()));
    }

    @Test
    void detalharShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        mockMvc.perform(get("/livros/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void criarShouldReturnBadRequestWhenInvalidDataWasProvided() throws Exception {
        String invalidData = "{}";

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criarShouldReturnAnBookWhenSuccessfully() throws Exception {
        String validData = objectMapper.writeValueAsString(livroFormDto);

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(header().exists("Location")).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(livroResponseDto.getId()));
    }

    @Test
    void atualizarShouldReturnAnBookWhenSuccessfully() throws Exception {
        String validData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(livroResponseDto.getId()))
                .andExpect(jsonPath("$.titulo").value(livroAtualizaResponseDto.getTitulo()));
    }

    @Test
    void atualizarShouldReturnBadRequestWhenInvalidData() throws Exception {
        livroUpdateFormDto.setNumeroPaginas(10);
        String invalidData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletarShouldReturnBadRequestWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", nonExistingId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarShouldDoNothingWhenValidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", existingId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
