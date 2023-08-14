--liquibase formatted sql
--changeSet Roman:1
create table log
(
    issue_date    date,
    returned_date date,
    book_id       bigint not null,
    reader_id     bigint not null,
    primary key (book_id, reader_id)
);