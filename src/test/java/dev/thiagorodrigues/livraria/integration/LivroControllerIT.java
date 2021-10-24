package dev.thiagorodrigues.livraria.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import dev.thiagorodrigues.livraria.domain.mocks.AutorFactory;
import dev.thiagorodrigues.livraria.domain.mocks.LivroFactory;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import dev.thiagorodrigues.livraria.infra.repositories.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class LivroControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private long nonExistingId = 100L;
    private Autor autor;
    private Livro livro;

    @BeforeEach
    void setUp() {
        autor = AutorFactory.criarAutorSemId();
        autorRepository.save(autor);

        livro = LivroFactory.criarLivroSemId();
        livro.setAutor(autor);
        livroRepository.save(livro);
    }

    @Test
    void detalharShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get("/livros/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void detalharShouldReturnLivroWhenValidId() throws Exception {
        mockMvc.perform(get("/livros/{id}", livro.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(livro.getId()));
    }

    @Test
    void criarShouldReturnBadRequestWhenInvalidData() throws Exception {
        var invalidData = "{}";

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criarShouldReturnLivroWhenValidData() throws Exception {
        var livroFormDto = LivroFactory.criarLivroFormDto();
        livroFormDto.setAutorId(autor.getId());
        var validData = objectMapper.writeValueAsString(livroFormDto);

        mockMvc.perform(post("/livros").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(header().exists("Location")).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void atualizarShouldReturnBadRequestWhenInvalidData() throws Exception {
        var livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDto();
        livroUpdateFormDto.setId(nonExistingId);
        var invalidData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizarShouldReturnUpdateLivroWhenValidData() throws Exception {
        var livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDto();
        livroUpdateFormDto.setId(livro.getId());
        livroUpdateFormDto.setAutorId(autor.getId());
        var validData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(status().isOk()).andExpect(jsonPath("$.titulo").value(livroUpdateFormDto.getTitulo()));
    }

    @Test
    void deletarShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void deletarShouldReturnNoContentWhenSuccessful() throws Exception {
        mockMvc.perform(delete("/livros/{id}", livro.getId())).andExpect(status().isNoContent());
    }

}
