create table usuarios (
    id bigint(20) primary key auto_increment,
    nome varchar(200) not null,
    email varchar(200) unique not null,
    senha varchar(255) not null
);
