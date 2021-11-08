package dev.thiagorodrigues.livraria.infra.repositories;

import dev.thiagorodrigues.livraria.domain.entities.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Perfil getByNome(String nome);

}
