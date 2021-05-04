drop database lazyjavie;
create database lazyjavie;
create table lazyjavie.member_registry (
id int not null auto_increment,
userid varchar(255),
userpass varchar(255),
primary key(id));