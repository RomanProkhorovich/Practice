--liquibase formatted sql
--changeSet Roman:1
create table reader
(
    active    boolean,
    id        bigint       not null,
    email     varchar(255) not null unique,
    firstname varchar(255) not null,
    lastname  varchar(255) not null,
    password  varchar(255) not null,
    role      varchar(255) not null check (role in ('ADMIN', 'USER')),
    surname   varchar(255),
    primary key (id)
);