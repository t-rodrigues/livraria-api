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
        autores.add(autor);

        return autor;
    }

}
