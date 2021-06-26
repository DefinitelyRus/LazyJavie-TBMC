/*
 * This thing does stuff.
 */

package sql_queries;

public class noDB_autofix {
	public static String[] get(String mode) {
		
		
		//TODO Update newDB.
		String[] sqlite = {
		"drop table members;",
		"drop table cmdlog",
		"drop table errorlog",
		"create table members (id integer primary key autoincrement not null, userid varchar(256));",
		"create table cmdlog (id int not null, userid varchar(256), userquery varchar(1024), errorid varchar(128), eventdate datetime, primary key(id));",
		"create table errorlog (id int primary key, err_type varchar(1024), err_stacktrace varchar(65535), eventdate datetime, appver varchar(32));",
		
		"drop table testtable;",
		"create table testtable (id integer primary key autoincrement not null, col1 varchar(24), col2 varchar(24), col3 varchar(24), col4 varchar(24), col5 varchar(24), col6 varchar(24), col7 varchar(24));"
		};
		
		switch (mode) {
			case "sqlite":
				return sqlite;
			default:
				return sqlite; //Temp
		}
	}
}