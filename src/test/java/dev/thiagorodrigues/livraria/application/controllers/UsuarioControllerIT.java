package dev.thiagorodrigues.livraria.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import dev.thiagorodrigues.livraria.domain.mocks.UsuarioFactory;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuarioControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private long nonExistingId = 100L;
    private String accessToken;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        accessToken = getAccessToken();
    }

    private String getAccessToken() {
        usuario = new Usuario(null, "Admin", "admin@mail.com", "SuperSecret123");
        usuario.adicionarPerfil(perfilRepository.getByNome("ROLE_ADMIN"));
        usuarioRepository.save(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, usuario.getEmail());
        usuario = (Usuario) authentication.getPrincipal();

        return "Bearer " + jwtTokenUtils.generateJwtToken(usuario.getId().toString());
    }

    @Test
    void createShouldReturnBadRequestWhenInvalidData() throws Exception {
        String invalidData = "{}";

        mockMvc.perform(post("/usuarios").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(invalidData)).andExpect(status().isBadRequest());
    }

    @Test
    void createShouldReturnUserWhenValidData() throws Exception {
        var usuarioFormDto = UsuarioFactory.createUserFormDto();
        var validData = objectMapper.writeValueAsString(usuarioFormDto);

        mockMvc.perform(post("/usuarios").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(validData)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateShouldReturnNotFoundWhenInvalidId() throws Exception {
        var usuarioUpdateFormDto = UsuarioFactory.createUserUpdateFormDto();
        usuarioUpdateFormDto.setId(nonExistingId);
        var invalidData = objectMapper.writeValueAsString(usuarioUpdateFormDto);

        mockMvc.perform(put("/usuarios").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(invalidData)).andExpect(status().isBadRequest());
    }

    @Test
    void updateShouldReturnUpdatedUserWhenValidData() throws Exception {
        var updateFormDto = UsuarioFactory.createUserUpdateFormDto();
        updateFormDto.setId(usuario.getId());
        var validData = objectMapper.writeValueAsString(updateFormDto);

        mockMvc.perform(put("/usuarios").header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON).content(validData)).andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(updateFormDto.getNome()))
                .andExpect(jsonPath("$.email").value(updateFormDto.getEmail()));
    }

    @Test
    void detailShouldReturnUserWhenValidId() throws Exception {
        mockMvc.perform(get("/usuarios/perfil").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deleteShouldReturnNotFoundWhenInvalidId() throws Exception {
        mockMvc.perform(delete("/usuarios/{id}", nonExistingId).header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnNoContentWhenSuccessFull() throws Exception {
        mockMvc.perform(delete("/usuarios/{id}", usuario.getId()).header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isNoContent());
    }

}
