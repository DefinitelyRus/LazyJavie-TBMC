/*
 * This file serves as the initializer for the entire program.
 */

package home;

import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import commands.P;
import net.dv8tion.jda.api.JDA;

@Deprecated
public class LazyJavie {
	
	//Initialization of objects and variables
	public static JDA jda;
	public static String prefix = "$";
	public static String token = "";
	
	//Startup
	@Deprecated
	public static void main(String[] args)  throws LoginException, SQLException{
		
	}
	
	//Version handling; this will no longer be used, but will be kept for future reference.
	@Deprecated
	public static String version(boolean toUpdate, boolean toPrint) {
		//Automatic version handling.
		String version;
		String defaultVer = "1.0";
		String build = null;
		String defaultBuild = "5";
		String title = null;	//Only set to a proper title BEFORE releasing.
		int intBuild = 0;
		try {
			//TODO Save version in a .json file, not SQL database.
			version = SQLconnector.get("select * from lazyjavie.version_handler", "ver_release", false);
			build = SQLconnector.get("select * from lazyjavie.version_handler", "build", false);
			
			//Checks if build count is empty.
			if (build.equals("Empty result.")) {
				P.print("Build not found; setting default...");
				build = defaultBuild;
			}
			else {
				//Checks if the program encounters an SQL error.
				if (build.startsWith("Error encountered: java.sql.SQLSyntaxErrorException")) {
					//Starts the automatic database setup.
					SQLconnector.NoDBfixer();
					
					//Recurs the function from itself.
					return version(toUpdate, toPrint);
				}

				//Updates the build number.
				try {
					intBuild = Integer.parseInt(build);
					if (toUpdate == true) intBuild++;
				}
				catch (Exception e) {
					P.print("Error encountered: " + e.toString()); return e.toString();
				}
			}
			
			//Checks if the version is empty or if the in-code version is different.
			if (version.equals("Empty result.") || !version.equals(defaultVer.toString())) {
				P.print("Version not found; setting default...");
				version = defaultVer;
				//SQLconnector.update("insert into lazyjavie.version_handler (ver_release) values (" +version+ ")", false);
			}
			
			//Converts explicitly to 1 decimal place.
			DecimalFormat df = new DecimalFormat("0.0");
			version = df.format(Float.parseFloat(version));
			
			//Updates the version listing.
			if (toUpdate == true) SQLconnector.update("insert into lazyjavie.version_handler (ver_release, build, title) values (" +version+ ", " +intBuild+ ", " +title+ ")", false);
			
			if (toPrint == true) P.print("LazyJavie v" +version+ " build " +intBuild);
			return "v" +version+ " build " + intBuild;
		}
		catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print("Error encountered: " + e.toString()); return e.toString();}
	}
}