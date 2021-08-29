/*
 * This thing does stuff.
 */

package sql_queries;

import java.util.LinkedList;
import java.util.List;

public class noDB_autofix {
	public static String[] get(String mode) {
		
		
		//TODO Update newDB.
		final String[] sqlite = {
		"drop table members;",
		"drop table autotickets;",
		"drop table botsettings;",
		"drop table cmdlog;",
		"drop table errorlog;",
		"drop table ticketlog;",
		
		"create table members (userid varchar(128) not null, usertag varchar(128) default null);",
		"create table autotickets (userid varchar(128) null, path_id int, info1 varchar(512), info2 varchar(512), info3 varchar(512), info4 varchar(512);",
		"create table botsettings (name varchar(128) not null, value varchar(256) default null, last_modified datetime, primary key(name));",
		"create table cmdlog (id int not null, userid varchar(256), userquery varchar(1024), errorid varchar(128), eventdate datetime, primary key(id));",
		"create table errorlog (err_type varchar(1024), err_stacktrace varchar(65535), eventdate datetime, appver varchar(32));",
		"create table ticketlog (ticketid varchar(5), userid varchar(128), date_created datetime, date_closed datetime default null, primary key (ticketid));",
		
		
		//Bot Settings: name, value, last_modified
		"insert into botsettings values ('ticket_message_id', null, datetime()), " +
				"('discord_bot_token_override', null, datetime())," +
				"('ticket_channel_id', null, datetime()), " +
				"('ticket_category_id', null, datetime())," +
				"('ticket_archive_cat_id', null, datetime())," +
				"('ticket_responder_role_id', null, datetime())," +
				"('automention_on_join_channel_id', null, datetime());"
		};
		
		List<String> sqlite_nodrop = new LinkedList<String>();
		for (String s : sqlite) {
			if (s.startsWith("drop")) continue;
			sqlite_nodrop.add(s);
		}
		
		switch (mode) {
			case "sqlite":
				return sqlite;
			case "sqlite-nodrop":
				return sqlite_nodrop.toArray(new String[0]);
			default:
				return sqlite; //Temp
		}
	}
}