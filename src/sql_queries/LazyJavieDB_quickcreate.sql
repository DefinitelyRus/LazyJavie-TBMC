-- drop database lazyjavie;
-- create database lazyjavie;

drop table lazyjavie.members;
drop table lazyjavie.shop;
drop table lazyjavie.cmdlog;
drop table lazyjavie.roleblacklist;
drop table lazyjavie.sellroles;

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

create table lazyjavie.cmdlog (
id int not null auto_increment,
eventdate datetime,
userquery varchar(255),
primary key(id));

create table lazyjavie.roleblacklist (
id int not null auto_increment,
rolename varchar(255),
primary key(id));
insert into lazyjavie.roleblacklist (rolename) values ("admins"), ("admin"), ("moderators"), ("moderator"), ("mod"), ("bots"), ("bot"), ("everyone"), ("@everyone"), ("members"), ("member");

-- insert into lazyjavie.cmdlog values (0, "", current_time()); 

CREATE TABLE `sellroles` (
  `idsellRoles` int NOT NULL AUTO_INCREMENT,
  `roleName` varchar(255) DEFAULT NULL,
  `rolePrice` varchar(255) DEFAULT '0',
  PRIMARY KEY (`idsellRoles`),
  UNIQUE KEY `roleName_UNIQUE` (`roleName`)
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
