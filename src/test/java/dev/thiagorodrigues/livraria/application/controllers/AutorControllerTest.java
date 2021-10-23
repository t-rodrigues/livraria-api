package dev.thiagorodrigues.livraria.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.application.dtos.AutorDetalhadoResponseDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorFormDto;
import dev.thiagorodrigues.livraria.application.dtos.AutorUpdateFormDto;
import dev.thiagorodrigues.livraria.domain.exceptions.DomainException;
import dev.thiagorodrigues.livraria.domain.exceptions.NotFoundException;
import dev.thiagorodrigues.livraria.domain.mocks.AutorFactory;
import dev.thiagorodrigues.livraria.domain.services.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

@WebMvcTest(AutorController.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AutorService autorService;

    private long existingId = 1L;
    private long nonExistingId = 100L;
    private AutorDetalhadoResponseDto autorDetalhadoResponseDto;
    private AutorDetalhadoResponseDto autorAtualizadoResponseDto;

    @BeforeEach
    void setUp() {
        autorDetalhadoResponseDto = AutorFactory.criarAutorDetalhadoResponseDto();
        autorAtualizadoResponseDto = AutorFactory.criarAutorAtualizadoResponseDto();

        when(autorService.detalhar(existingId)).thenReturn(autorDetalhadoResponseDto);
        when(autorService.detalhar(nonExistingId)).thenThrow(NotFoundException.class);
        when(autorService.criar(any(AutorFormDto.class))).thenReturn(autorDetalhadoResponseDto);
        when(autorService.atualizar(any(AutorUpdateFormDto.class))).thenReturn(autorAtualizadoResponseDto);
        doThrow(NotFoundException.class).when(autorService).deletar(nonExistingId);
        doNothing().when(autorService).deletar(existingId);
    }

    @Test
    void detalharShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get("/autores/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void detalharShouldReturnAnAutorWhenIdExists() throws Exception {
        mockMvc.perform(get("/autores/{id}", existingId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void criarShouldReturnBadRequestWhenInvalidData() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criarShouldReturnAutorWhenValidData() throws Exception {
        var autorFormDto = AutorFactory.criarAutorFormDto();
        String json = objectMapper.writeValueAsString(autorFormDto);

        mockMvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.nome").value(autorFormDto.getNome()))
                .andExpect(jsonPath("$.email").value(autorFormDto.getEmail()))
                .andExpect(jsonPath("$.data_nascimento").exists())
                .andExpect(jsonPath("$.mini_curriculo").value(autorFormDto.getMiniCurriculo()));
    }

    @Test
    void atualizarSholdReturnBadRequestWhenInvalidId() throws Exception {
        when(autorService.atualizar(any())).thenThrow(DomainException.class);

        var autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();
        autorUpdateFormDto.setId(nonExistingId);
        String json = objectMapper.writeValueAsString(autorUpdateFormDto);

        mockMvc.perform(put("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(DomainException.class.getSimpleName()));
    }

    @Test
    void atualizarShouldReturnUpdatedAutorWhenValidData() throws Exception {
        var autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();
        System.out.println(autorUpdateFormDto.getId());
        String json = objectMapper.writeValueAsString(autorUpdateFormDto);

        mockMvc.perform(put("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void deletarShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/autores/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void deletarShouldReturnBadRequestWhenAutorHasDependentBook() throws Exception {
        long dependentId = 10L;
        doThrow(DomainException.class).when(autorService).deletar(anyLong());

        mockMvc.perform(delete("/autores/{id}", dependentId)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(DomainException.class.getSimpleName()));
    }

    @Test
    void deletarShouldReturnNoContentWhenSuccessful() throws Exception {
        mockMvc.perform(delete("/autores/{id}", existingId)).andExpect(status().isNoContent());
    }

}
