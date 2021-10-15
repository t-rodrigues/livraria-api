package dev.thiagorodrigues.livraria.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationError {

    private String field;
    private String message;

}
