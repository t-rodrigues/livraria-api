alter table livros add column autor_id bigint not null;
alter table livros add foreign key (autor_id) references autores(id);
