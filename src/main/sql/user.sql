create table user(
id bigint auto_increment primary key,
name varchar(100) not null,
birthday DATE,
nickname varchar(50),
username varchar(50) not null unique,
password varchar(255) not null,
role ENUM('USER', 'ADMIN') default 'USER'
email varchar(100) not null unique;
);