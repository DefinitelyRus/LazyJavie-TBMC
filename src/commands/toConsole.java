/*
 * ---------------!!! ADD TO README !!!---------------
 * A lazy way of communicating between the server and the console.
 */

package commands;

import bot_init.LazyJavie;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class toConsole extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String args = event.getMessage().getContentRaw();
		
		if (args.startsWith(LazyJavie.prefix + "msgc")) {
			String sender = event.getMessage().getMember().getUser().getName();
			String message = args.replace(LazyJavie.prefix + "msgc ", "");
			P.print("\n" + sender + ": " + message);
		}
	}
}
