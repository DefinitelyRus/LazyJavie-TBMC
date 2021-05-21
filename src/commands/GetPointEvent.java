/*
 * Each time a registered member sends a message, they will earn a point.
 */

package commands;

import java.sql.SQLException;
import javax.security.auth.login.LoginException;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GetPointEvent extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		try {
			//Gets the sender's discord ID.
			String memberId = event.getMember().getId();
			
			//Gets the sender's points, and increments it by 1.
			//P.print("\n[GetPointEvent] Receiving message from " + event.getMember().getUser().getName());
			Integer pts = Integer.parseInt(SQLconnector.get("select points from lazyjavie.members WHERE userid=" + memberId + ";", "points", false));
			pts += 1;
			
			//Applies the changes and uploads it to the database.
			SQLconnector.update("UPDATE lazyjavie.members "+ "SET points = " + pts + " WHERE userid=" + memberId + ";",true);
		}
		catch (NullPointerException e) {P.print("Ignoring error: " + e.toString());}
		catch (LoginException e) {e.printStackTrace();}
		catch (SQLException e) {e.printStackTrace();}
		catch (NumberFormatException e) {if (e.toString().startsWith("java.lang.NumberFormatException: For input string: \"Statement created.\"")) {}}
		catch (Exception e) {e.printStackTrace();}
	}
}