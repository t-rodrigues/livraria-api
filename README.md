
# API - Livraria

Desafio do Bootcamp de Java da Alura. Construção de uma API Rest.


## Tech Stack

- Java (11)
- [Spring Boot (2.5.6)](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [Flyway](https://flywaydb.org/)
- [JJWT](https://github.com/jwtk/jjwt)
- [JUnit](https://junit.org/junit5/)
- [Lombok](https://projectlombok.org/)
- [Maven](https://maven.apache.org/)
- [ModelMapper](http://modelmapper.org/)
- [SwaggerUI (3.0.0)](https://github.com/springfox/springfox)
- [Docker](https://www.docker.com/)
- [MySQL](https://www.mysql.com/)

## Rodar localmente

Clonar o projeto

```bash
  git clone https://github.com/t-rodrigues/livraria-api.git
```

Acessar o diretorio do projeto

```bash
  cd livraria-api
```

Iniciar o projeto

```bash
  docker-compose up -d
```

Acessar o endereço do projeto no navegador `http://localhost:8080/swagger-ui/`


## Features

- Autenticação/Autorização com JWT
- CRUD de Autores
- CRUD de Livros
- CRUD de Usuários
- Envio de E-mail em Produção com Spring Mail


## Autor

- [@thiagorodrigues](https://www.github.com/t-rodrigues)
