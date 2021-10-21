create table autores (
    id bigint(20) primary key auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null,
    data_nascimento date not null,
    mini_curriculo text
);
