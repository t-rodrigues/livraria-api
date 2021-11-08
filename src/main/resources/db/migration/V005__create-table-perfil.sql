create table perfis (
  id bigint PRIMARY KEY AUTO_INCREMENT,
  nome varchar(100) not null
);

create table usuarios_perfis (
  usuario_id bigint not null,
  perfil_id bigint not null,

  primary key(usuario_id, perfil_id),
  foreign key(usuario_id) references usuarios(id),
  foreign key(perfil_id) references perfis(id)
);

insert into perfis (nome) values ('ROLE_ADMIN');
insert into perfis (nome) values ('ROLE_USER');
