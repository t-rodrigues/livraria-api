package dev.thiagorodrigues.livraria.application.dtos;

import java.math.BigDecimal;

public interface RelatorioLivrosPorAutor {

    String getAutor();

    Long getQuantidade();

    BigDecimal getPercentual();

}
