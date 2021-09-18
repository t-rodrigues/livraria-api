package dev.thiagorodrigues.livraria.infra.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dev.thiagorodrigues.livraria.domain.entities.Autor;

@Repository
public class AutorRepository {

    private List<Autor> autores = new ArrayList<>();

    public List<Autor> findAll() {
        return autores;
    }

    public Autor save(Autor autor) {
        autor.setId(autores.size() + 1l);
        autores.add(autor);

        return autor;
    }

    public Autor findById(Long id) {
        return autores.stream().filter(autor -> autor.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Autor não cadastrado"));
    }

}
