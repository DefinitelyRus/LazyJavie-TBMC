package commands;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
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
				
				String insertcmd = "insert into lazyjavie.shop (itemid, itemname, price, quantity, itemdesc) values (";
				String z = ", ";
				if (args[1] == "inventory" || args[1] == "inv") {
					//args[2] is the item name
					//args[3] is the change to make
					
					/*
					 * Get the list of available items.
					 * Then check if args[2] is in the list of items.
					 * If not, throw an error and list the available items.
					 */
				} else if (args[1] == "add" || args[1] == "newitem") {
					//shopupdate add itemname price quantity description
					//args[2] is the item name
					//args[3] is the price
					//args[4] is the # of available items
					//args[5] is the item description
					try {
						P.print(insertcmd + "0" +z+ args[2] +z+ Integer.parseInt(args[3]) +z+ Integer.parseInt(args[4]) +z+ args[5] + ")\n");
						SQLconnector.update(insertcmd + "0" +z+ args[2] +z+ Integer.parseInt(args[3]) +z+ Integer.parseInt(args[4]) +z+ args[5] + ")");
					} catch (Exception e) {
						e.printStackTrace();
					}
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
					//TODO
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				P.print("OUT OF BOUNDS");
				e.printStackTrace();
			}
		}

		//if (args[0].equalsIgnoreCase(LazyJavie.prefix + "")) {}
	}
}
