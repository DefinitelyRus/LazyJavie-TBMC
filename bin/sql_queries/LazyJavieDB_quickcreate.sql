-- drop database lazyjavie;
create database lazyjavie;

create table lazyjavie.members (
id int not null auto_increment,
userid varchar(255),
userpass varchar(255),
points int,
primary key(id));

create table lazyjavie.shop (
itemid int not null auto_increment,
itemname varchar(24),
price int,
quantity int,
itemdesc varchar(250),
primary key(itemid));

create table lazyjavie.bot_status (
id int not null auto_increment,
activity varchar(24),
act_desc varchar(32),
primary key(id));