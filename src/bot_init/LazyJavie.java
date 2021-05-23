/*
 * This file serves as the initializer for the entire program.
 */

package bot_init;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import commands.Clear;
import commands.GetPointEvent;
import commands.P;
import commands.Quit;
import commands.Register;
import commands.Returns;
import commands.adminShop;
import commands.shop;
import commands.shopInventory;
import commands.toConsole;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class LazyJavie {
	
	//Initialization of objects and variables
	public static JDA jda;
	public static String prefix = "$";
	public static String token = "";
	
	public static String version(boolean toUpdate, boolean toPrint) {
		//Automatic version handling.
		String version;
		String defaultVer = "1.0";
		String build = null;
		String title = null;	//Only set to a proper title BEFORE releasing.
		int intBuild = 0;
		try {
			//TODO Save version in a .json file, not SQL database.
			version = SQLconnector.get("select * from lazyjavie.version_handler", "ver_release", false);
			build = SQLconnector.get("select * from lazyjavie.version_handler", "build", false);
			
			//Checks if build count is empty.
			if (build.equals("Empty result.")) {
				P.print("Build not found; setting default...");
				build = "1";
				//SQLconnector.update("insert into lazyjavie.version_handler (build) values (" +build+ ")", false);
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
		//catch (SQLSyntaxErrorException e) {}
		catch (LoginException e) {P.print("Error encountered: " + e.toString()); return e.toString();}
		catch (SQLException e) {P.print("Error encountered: " + e.toString()); return e.toString();}
		catch (Exception e) {P.print("Error encountered: " + e.toString()); e.printStackTrace(); return e.toString();}

	}
	
	//Startup
	public static void main(String[] args) throws LoginException, SQLException {
		try {
			version(false, true);	//Set the first argument to FALSE for releases.
			
			P.print("Starting...");
			//[A] Getting the Token----------------------------------------
			P.print("[A-1] Getting token from file");
			/*
			 * This part is important because if this step is skipped,
			 * the discord bot's API token is visible publicly on GitHub.
			 * 
			 * This solution allows the token to be stored locally on the host's computer,
			 * while still having the source code be readily available on the GitHub repository.
			 */
			
			//Looks for the text file "lazyjavie_token.txt".
			File file = new File("C:\\lazyjavie_token.txt");
			P.print("[A-2] File found.");
			//Scans the file.
		    Scanner reader = new Scanner(file);
		    P.print("[A-3] Scanning...");
		    while (reader.hasNextLine()) {token = reader.nextLine(); break;}
		    P.print("[A-4] Token assigned: " + token);
		    
		    //Closes the scanner.
		    reader.close();
		    P.print("[A-5] Scanner closed.");
		      
			//[B] Logging in the bot----------------------------------------
			P.print("[B-1] Logging in...");
			jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
			
			P.print("[B-2] Setting status...");
			jda.getPresence().setStatus(OnlineStatus.ONLINE);
			jda.getPresence().setActivity(Activity.listening("to Rick Astley - Never Gonna Give You Up"));
			
			P.print("[B-3] Opening to commands...");
			//[IMPORTANT] Add new commands here.
			jda.addEventListener(new Returns());
			jda.addEventListener(new Register());
			jda.addEventListener(new Clear());
			jda.addEventListener(new shop());
			jda.addEventListener(new adminShop());
			jda.addEventListener(new GetPointEvent());
			jda.addEventListener(new toConsole());
			jda.addEventListener(new shopInventory());
			jda.addEventListener(new Quit());

			P.print("[B-4] Ready!");
		}
		//catch (NumberFormatException e) {}
		catch (FileNotFoundException file404) {
			//[A] File not found.
			P.print("Missing file error: " + file404.toString());
			return;
		}
		catch (Exception e) {
			//[A-B] Every other exception.
			P.print(e.toString());
			e.printStackTrace();
			return;
		}

	}
	
}