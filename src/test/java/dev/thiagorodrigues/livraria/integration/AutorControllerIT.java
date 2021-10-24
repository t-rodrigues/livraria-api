package dev.thiagorodrigues.livraria.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;

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
class AutorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private long nonExistingId = 10L;
    private Autor autor;

    @BeforeEach
    void setUp() {
        autor = AutorFactory.criarAutorSemId();
        autorRepository.save(autor);
    }

    @Test
    void detalharShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/autores/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void detalharShouldReturnAutorWhenValidId() throws Exception {
        mockMvc.perform(get("/autores/{id}", autor.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(autor.getId()));
    }

    @Test
    void criarShouldReturnBadRequestWhenInvalidData() throws Exception {
        String invalidData = "{}";

        mockMvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(MethodArgumentNotValidException.class.getSimpleName()));
    }

    @Test
    void criarShouldReturnAutorWhenValidData() throws Exception {
        var autorFormDto = AutorFactory.criarAutorFormDto();
        String validData = objectMapper.writeValueAsString(autorFormDto);

        mockMvc.perform(post("/autores").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(header().exists("Location")).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void atualizarShouldReturnBadRequestWhenInvalidData() throws Exception {
        var autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();
        autorUpdateFormDto.setId(nonExistingId);
        var invalidData = objectMapper.writeValueAsString(autorUpdateFormDto);

        mockMvc.perform(put("/autores").contentType(MediaType.APPLICATION_JSON).content(invalidData))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizarShouldUpdateWhenValidData() throws Exception {
        var autorUpdateFormDto = AutorFactory.criarAutorUpdateFormDto();
        autorUpdateFormDto.setId(autor.getId());
        var validData = objectMapper.writeValueAsString(autorUpdateFormDto);

        mockMvc.perform(put("/autores").contentType(MediaType.APPLICATION_JSON).content(validData))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(autor.getId()));
    }

    @Test
    void deletarShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/autores/{id}", nonExistingId)).andExpect(status().isNotFound());
    }

    @Test
    void deletarShouldReturnBadRequestWhenAutorIsDependent() throws Exception {
        var livro = LivroFactory.criarLivroSemId();
        livro.setAutor(autor);
        livroRepository.save(livro);

        mockMvc.perform(delete("/autores/{id}", autor.getId())).andExpect(status().isBadRequest());
    }

    @Test
    void deletarShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/autores/{id}", autor.getId())).andExpect(status().isNoContent());
    }

}
