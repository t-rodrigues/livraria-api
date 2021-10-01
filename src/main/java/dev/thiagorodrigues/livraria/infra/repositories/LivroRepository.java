package dev.thiagorodrigues.livraria.infra.repositories;

import dev.thiagorodrigues.livraria.domain.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

}
