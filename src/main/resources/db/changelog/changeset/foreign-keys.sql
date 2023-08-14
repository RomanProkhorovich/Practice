--liquibase formatted sql
--changeSet Roman:1
--validCheckSum: 9:53333e9aaf12df00af7568a26a39dd49
alter table if exists log
    add  foreign key (book_id) references book(id);

alter table if exists log
    add  foreign key (reader_id) references reader(id);

create sequence book_seq start with 1 increment by 50 ;
create sequence reader_seq start with 1 increment by 50;