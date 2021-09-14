package dev.thiagorodrigues.livraria.infra.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dev.thiagorodrigues.livraria.domain.entities.Livro;

@Repository
public class LivroRepository {

    private List<Livro> livros = new ArrayList<>();

    public List<Livro> findAll() {
        return livros;
    }

    public Livro save(Livro livro) {
        livros.add(livro);

        return livro;
    }

}
