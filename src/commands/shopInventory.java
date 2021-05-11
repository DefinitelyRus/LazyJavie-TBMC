package commands;

import bot_init.LazyJavie;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class shopInventory extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "inventory")) {
			//TODO Output all available items
		}

		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "shopupdate")) {
			try {
				if (args[1] == "inventory" || args[1] == "inv") {
					//args[2] is the item name
					//args[3] is the change to make
					
					/*
					 * Get the list of available items.
					 * Then check if args[2] is in the list of items.
					 * If not, throw an error and list the available items.
					 */
				} else if (args[1] == "add" || args[1] == "newitem") {
					//args[2] is the item name
					//args[3] is the price
					//args[4] is the # of available items
					
					/*
					 * Add the item to the list.
					 */
				} else if (args[1] == "remove" || args[1] == "delete") {
					//args[2] is the item name
					//args[3] must be "CONFIRM"
					
					/*
					 * Get the list of available items.
					 * Then check if args[2] is in the list.
					 * If not, throw an error and list the available items.
					 * 
					 * Otherwise, check if "CONFIRM" is present.
					 * If not, tell the user to add "CONFIRM"
					 * 
					 * Otherwise, delete the specified item from the list.
					 */
				} else if (args[1] == "rename") {
					
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
		}

		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "")) {
			
		}
	}
}
