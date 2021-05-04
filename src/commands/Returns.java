/*
 * 
 */
package commands;

import bot_init.LazyJavie;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Returns extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		//BOT TOKEN. Returns the bot's token... not really.
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "bottoken")) {
			event.getChannel().sendMessage("Bot token: ||Never gonna give you up~ Never gonna let you down~||").queue();
		}
		
		//PING. Returns the latency.
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "ping")) {
			P.print("\nRequesting ping: " + event.getMember().getUser());
			long ping = event.getJDA().getGatewayPing();
			P.print("Latency gathered.");
			event.getChannel().sendMessage(ping + "ms").queue();
			P.print("Ping: " + ping + "ms\n");
		}
		
		//TEST. Just returns a confirmation message to see if the bot works.
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "test")) {
			event.getChannel().sendMessage("Test success!").queue();
			P.print("Test success! Sender: " + event.getMember());
		}
	}
}
