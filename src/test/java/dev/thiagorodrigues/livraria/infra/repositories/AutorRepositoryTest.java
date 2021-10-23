package dev.thiagorodrigues.livraria.infra.repositories;

import dev.thiagorodrigues.livraria.domain.entities.Autor;
import dev.thiagorodrigues.livraria.domain.mocks.AutorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class AutorRepositoryTest {

    @Autowired
    private AutorRepository autorRepository;

    private Autor autorToBeSaved;
    private Autor autorSaved;

    @BeforeEach
    private void setUp() {
        autorToBeSaved = AutorFactory.criarAutorSemId();
        autorSaved = this.autorRepository.save(autorToBeSaved);
    }

    @Test
    void saveShouldPersistWhenSuccessful() {
        var autorSaved = this.autorRepository.save(autorToBeSaved);

        assertNotNull(autorSaved);
        assertNotNull(autorSaved.getId());
        assertEquals(autorToBeSaved.getNome(), autorSaved.getNome());
    }

    @Test
    void saveShouldUpdateWhenSuccessful() {
        autorSaved.setNome("Updated");
        var autorUpdated = this.autorRepository.save(autorSaved);

        assertNotNull(autorUpdated);
        assertNotNull(autorUpdated.getId());
        assertEquals(autorUpdated.getNome(), autorSaved.getNome());
    }

    @Test
    void deleteShouldRemoveWhenSuccessful() {
        assertDoesNotThrow(() -> {
            this.autorRepository.deleteById(autorToBeSaved.getId());
            var autor = this.autorRepository.findById(autorToBeSaved.getId());

            assertTrue(autor.isEmpty());
        });
    }

    @Test
    void findByIdShouldReturnWhenSuccessful() {
        var autor = this.autorRepository.findById(autorSaved.getId());

        assertTrue(autor.isPresent());
    }

    @Test
    void findByIdShouldReturnEmptyWhenAutorNotFound() {
        long nonExistingId = 1000L;
        var autor = this.autorRepository.findById(nonExistingId);

        assertTrue(autor.isEmpty());
    }

}
