--liquibase formatted sql
--changeSet Roman:1
create table book
(
    archived    boolean,
    released_at integer      not null,
    id          bigint       not null,
    author_name varchar(255) not null,
    title       varchar(255) not null,
    primary key (id),
    unique (released_at, author_name, title)
);
