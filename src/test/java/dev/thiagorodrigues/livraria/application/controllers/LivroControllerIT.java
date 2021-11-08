package dev.thiagorodrigues.livraria.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.mocks.AutorFactory;
import dev.thiagorodrigues.livraria.domain.mocks.LivroFactory;
import dev.thiagorodrigues.livraria.infra.repositories.AutorRepository;
import dev.thiagorodrigues.livraria.infra.repositories.LivroRepository;
import dev.thiagorodrigues.livraria.infra.repositories.PerfilRepository;
import dev.thiagorodrigues.livraria.infra.repositories.UsuarioRepository;
import dev.thiagorodrigues.livraria.main.security.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
@ActiveProfiles("test")
@Transactional
class LivroControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private long nonExistingId = 100L;
    private Autor autor;
    private Livro livro;
    private String accessToken;

    @BeforeEach
    void setUp() {
        autor = AutorFactory.criarAutorSemId();
        autorRepository.save(autor);

        livro = LivroFactory.criarLivroSemId();
        livro.setAutor(autor);
        livroRepository.save(livro);

        accessToken = getAccessToken();
    }

    private String getAccessToken() {
        var usuario = new Usuario(null, "Admin", "admin@mail.com", "SuperSecret123");
        usuario.adicionarPerfil(perfilRepository.getByNome("ROLE_ADMIN"));
        usuarioRepository.save(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getEmail());
        usuario = (Usuario) authentication.getPrincipal();

        return "Bearer " + jwtTokenUtils.generateJwtToken(usuario.getId().toString());
    }

    @Test
    void detailShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(get("/livros/{id}", nonExistingId).header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void detailShouldReturnBookWhenValidId() throws Exception {
        mockMvc.perform(get("/livros/{id}", livro.getId()).header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(livro.getId()));
    }

    @Test
    void createShouldReturnBadRequestWhenInvalidData() throws Exception {
        var invalidData = "{}";

        mockMvc.perform(post("/livros").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(invalidData)).andExpect(status().isBadRequest());
    }

    @Test
    void createShouldReturnBookWhenValidData() throws Exception {
        var livroFormDto = LivroFactory.criarLivroFormDto();
        livroFormDto.setAutorId(autor.getId());
        var validData = objectMapper.writeValueAsString(livroFormDto);

        mockMvc.perform(post("/livros").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(validData)).andExpect(header().exists("Location"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateShouldReturnBadRequestWhenInvalidData() throws Exception {
        var livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDto();
        livroUpdateFormDto.setId(nonExistingId);
        var invalidData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(invalidData)).andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturnUpdatedBookWhenValidData() throws Exception {
        var livroUpdateFormDto = LivroFactory.criarLivroUpdateFormDto();
        livroUpdateFormDto.setId(livro.getId());
        livroUpdateFormDto.setAutorId(autor.getId());
        var validData = objectMapper.writeValueAsString(livroUpdateFormDto);

        mockMvc.perform(put("/livros").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(validData)).andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value(livroUpdateFormDto.getTitulo()));
    }

    @Test
    void deleteShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/livros/{id}", nonExistingId).header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnNoContentWhenSuccessful() throws Exception {
        mockMvc.perform(delete("/livros/{id}", livro.getId()).header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNoContent());
    }

}
