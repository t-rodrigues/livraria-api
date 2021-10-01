create table livros (
    id bigint(20) primary key auto_increment,
    titulo varchar(200) not null,
    numero_paginas int not null,
    data_lancamento date not null
);
