package dev.thiagorodrigues.livraria.domain.mocks;

import dev.thiagorodrigues.livraria.domain.entities.Perfil;

import java.util.List;

public class PerfilFactory {

    public static List<Perfil> createListPerfis() {
        var admin = new Perfil(1L, "ROLE_ADMIN");
        var user = new Perfil(2L, "ROLE_USER");

        return List.of(admin, user);
    }

}
