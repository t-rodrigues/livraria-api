package dev.thiagorodrigues.livraria.application.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@JsonInclude(Include.NON_NULL)
public class StandardError {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private List<ValidationError> errors;
    private String path;

    public static class Builder {
        private Integer status;
        private String error;
        private String message;
        private String path;

        public Builder() {
        }

        public Builder status(Integer status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public StandardError build() {
            return new StandardError(this);
        }
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }

    private StandardError(Builder builder) {
        timestamp = LocalDateTime.now();
        status = builder.status;
        error = builder.error;
        message = builder.message;
        path = builder.path;
    }

}
