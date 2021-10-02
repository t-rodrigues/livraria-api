package dev.thiagorodrigues.livraria.application.dtos;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class RelatorioLivrosPorAutorDto {

    private String autor;
    private Long quantidade;
    private BigDecimal percentual;

    public RelatorioLivrosPorAutorDto(String autor, Long quantidade, Double percentual) {
        this.autor = autor;
        this.quantidade = quantidade;
        setPercentual(percentual);
    }

    private void setPercentual(Double percentual) {
        this.percentual = BigDecimal.valueOf(percentual * 100).setScale(1, RoundingMode.CEILING);
    }

}
