package dev.thiagorodrigues.livraria.infra.repositories;

import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutor;
import dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutorDto;
import dev.thiagorodrigues.livraria.domain.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("select l.autor.nome as autor, count(*) as quantidade, "
            + "count(*) * 1.0 / (select count(*) from Livro) * 100.0 as percentual "
            + "from Livro l group by l.autor order by percentual desc")
    List<RelatorioLivrosPorAutor> relatorioLivrosPorAutor();

    @Query("select new dev.thiagorodrigues.livraria.application.dtos.RelatorioLivrosPorAutorDto("
            + "l.autor.nome, count(*), count(*) * 1.0 / (select count(*) from Livro) * 1.0 as percentual) "
            + " from Livro l group by l.autor order by percentual desc")
    List<RelatorioLivrosPorAutorDto> relatorioLivrosPorAutorDto();

}
