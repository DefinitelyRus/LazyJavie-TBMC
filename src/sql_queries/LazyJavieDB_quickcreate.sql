drop database lazyjavie;
create database lazyjavie;

-- drop table lazyjavie.members;
-- drop table lazyjavie.shop;
-- drop table lazyjavie.cmdlog;
-- drop table lazyjavie.roleblacklist;
-- drop table lazyjavie.sellroles;

create table lazyjavie.members (
id int not null auto_increment,
userid varchar(255),
userpass varchar(255),
points int default 0,
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

create table lazyjavie.sellroles (
  idsellRoles int NOT NULL AUTO_INCREMENT,
  roleName varchar(255) DEFAULT NULL,
  rolePrice int default null,
  PRIMARY KEY (idsellRoles),
  UNIQUE KEY roleName_UNIQUE (roleName)
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

create table lazyjavie.helplist (
id int not null auto_increment,
cmd varchar(32) default null,
dsc varchar(256) default null,
adminonly bool default true,
primary key (id));

insert into lazyjavie.helplist (cmd, dsc, adminonly) values
("help", "Displays this list of available commands.", false),
("points", "Shows how much points you've accumulated.", false),
("register", "Registers your discord account to the database, allowing you to accumulate points.", false),
("deregister", "Deregisters your discord account from the database.", false),
("shop", "A place where you can buy perks and rewards.", false),
("ping", "Pings the bot and returns the latency.", false),
("test", "Pings the bot.", true),
("clear", "Clears 2-100 messages sent within 14 days from now.", true),
("ashop", "Where you can manage your shop.", true);


