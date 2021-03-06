package dev.thiagorodrigues.livraria.infra.repositories;

import dev.thiagorodrigues.livraria.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select u from Usuario u join fetch u.perfis where u.id = :userId")
    Optional<Usuario> findByIdWithRoles(Long userId);

}
