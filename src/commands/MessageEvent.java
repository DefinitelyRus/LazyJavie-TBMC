package commands;

//import bot_init.LazyJavie;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageEvent extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		//String[] args = event.getMessage().getContentRaw().split("\\s+");	//Use if you need it.
		
		/* TODO
		 * Make it so that it will add 1 point to the sender then store it to the database.
		 * 
		 * Before that, make a new table (name it whatever you want) with the columns:
		 * userID (discord ID), # of points, and a list of active perks (empty by default)
		 */
	}
}
