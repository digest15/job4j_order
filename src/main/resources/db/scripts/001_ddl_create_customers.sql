create table customers (
    id serial primary key not null,
    name varchar(2000) NOT NULL unique
);