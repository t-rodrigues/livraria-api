package dev.thiagorodrigues.livraria.infra.repositories;

import dev.thiagorodrigues.livraria.domain.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

}
