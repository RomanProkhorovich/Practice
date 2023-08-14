
create sequence book_seq start with 1 increment by 50 Hibernate:
create sequence reader_seq start with 1 increment by 50 Hibernate:
create table book
(
    archived    boolean,
    released_at integer      not null,
    id          bigint       not null,
    author_name varchar(255) not null,
    title       varchar(255) not null,
    primary key (id),
    constraint UKbsnjsqipui77i706kq95htjcy unique (released_at, author_name, title)
)
create table log
(
    issue_date    date,
    returned_date date,
    book_id       bigint not null,
    reader_id     bigint not null,
    primary key (book_id, reader_id)
)
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
)
alter table if exists log
    add constraint FK2qeiajmq3b0gw4ylmt2kmqkmn foreign key (book_id) references book Hibernate:
alter table if exists log
    add constraint FKhp2ia5tx5qf15267q4r9hoopr foreign key (reader_id) references reader