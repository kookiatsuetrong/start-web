create database sample default character set 'UTF8';
use sample;

create table users
(
	number        int unique not null auto_increment,
	email         varchar(200) unique not null,
	password      varchar(200) not null,
	first_name    varchar(200) not null,
	last_name     varchar(200) not null
);

insert into users(email, password, first_name, last_name)
	values('user@email.com', upper(sha2('User1234', 512)), 'First', 'Last');

create user me identified by 'password';
grant all on sample.* to me;
