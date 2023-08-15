--liquibase formatted sql
--changeSet Roman:1
create table log
(
    id            bigint not null ,
    issue_date    date,
    returned_date date,
    book_id       bigint not null,
    reader_id     bigint not null,
    primary key (id)
);