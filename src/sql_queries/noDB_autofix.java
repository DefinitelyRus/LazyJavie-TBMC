/*
 * This thing does stuff.
 */

package sql_queries;

public class noDB_autofix {
	public static String[] get(String mode) {
		
		//TODO Update newDB.
		String[] newDB = {
		"create table members (id int not null, userid varchar(256), userpass varchar(256), points int default 0, primary key(id));",
		"create table shop (itemid int not null, itemname varchar(24), price int, quantity int, itemdesc varchar(256), primary key(itemid));",
		"create table cmdlog (id int not null, userid varchar(256), userquery varchar(1024), errorid varchar(128), eventdate datetime, primary key(id));",
		"create table roleblacklist (rolename varchar(256));",
		"insert into roleblacklist (rolename) values ('admins'), ('admin'), ('moderators'), ('moderator'), ('mod'), ('bots'), ('bot'), ('everyone'), ('@everyone'), ('members'), ('member');",
		"create table helplist (cmd varchar(32) default null, dsc varchar(256) default null, adminonly bool default true, primary key (cmd));",
		"insert into helplist (cmd, dsc, adminonly) values ('help', 'Displays this list of available commands.', false), ('register', 'Registers your discord account to the database, allowing you to accumulate points.', false), ('deregister', 'Deregisters your discord account from the database.', false), ('shop', 'A place where you can buy perks and rewards.', false), ('ping', 'Pings the bot and returns the latency.', false), ('test', 'Pings the bot.', true), ('clear', 'Clears 2-100 messages sent within 14 days from now.', true), ('ashop', 'Where you can manage your shop.', true);",
		};
		
		switch (mode) {
			case "createNew":
				return newDB;
			default:
				return newDB; //Temp
		}
	}
}