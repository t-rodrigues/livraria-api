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

    @MockBean
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId = 1L;
    private long nonExistingId = 100L;
    private AutorDetalhadoResponseDto autorDetalhadoResponseDto;
    private AutorDetalhadoResponseDto autorAtualizadoResponseDto;

    @BeforeEach
    void setUp() {
        autorDetalhadoResponseDto = AutorFactory.criarAutorDetalhadoResponseDto();
        autorAtualizadoResponseDto = AutorFactory.criarAutorAtualizadoResponseDto();

        when(autorService.detail(existingId)).thenReturn(autorDetalhadoResponseDto);
        when(autorService.detail(nonExistingId)).thenThrow(NotFoundException.class);
        when(autorService.create(any(AutorFormDto.class))).thenReturn(autorDetalhadoResponseDto);
        when(autorService.update(any(AutorUpdateFormDto.class))).thenReturn(autorAtualizadoResponseDto);
        doThrow(NotFoundException.class).when(autorService).delete(nonExistingId);
        doNothing().when(autorService).delete(existingId);
    }

    @Test
    void detailShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get("/autores/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void detailShouldReturnAnAuthorWhenIdExists() throws Exception {
        mockMvc.perform(get("/autores/{id}", existingId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createShouldReturnBadRequestWhenInvalidData() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createShouldReturnAuthorWhenValidData() throws Exception {
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
    void updateSholdReturnBadRequestWhenInvalidId() throws Exception {
        when(autorService.update(any())).thenThrow(DomainException.class);

        var autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();
        autorUpdateFormDto.setId(nonExistingId);
        String json = objectMapper.writeValueAsString(autorUpdateFormDto);

        mockMvc.perform(put("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(DomainException.class.getSimpleName()));
    }

    @Test
    void updateShouldReturnUpdatedAuthorWhenValidData() throws Exception {
        var autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();
        System.out.println(autorUpdateFormDto.getId());
        String json = objectMapper.writeValueAsString(autorUpdateFormDto);

        mockMvc.perform(put("/autores").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/autores/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnBadRequestWhenAutorHasDependentBook() throws Exception {
        long dependentId = 10L;
        doThrow(DomainException.class).when(autorService).delete(anyLong());

        mockMvc.perform(delete("/autores/{id}", dependentId)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(DomainException.class.getSimpleName()));
    }

    @Test
    void deleteShouldReturnNoContentWhenSuccessful() throws Exception {
        mockMvc.perform(delete("/autores/{id}", existingId)).andExpect(status().isNoContent());
    }

}
