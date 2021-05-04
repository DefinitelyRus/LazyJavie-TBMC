/*
 * This module will serve as a starting template for adding new commands.
 * There are explanations as to how each part works but if you don't understand
 * how certain parts work, don't be afraid to ask. :)
 */

//Package. DO NOT TOUCH.
package commands;

//Imports. DO NOT TOUCH.
import bot_init.LazyJavie;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//Add your own imports here if needed.
//import package;

//Name of the class must be the same as the .java file name.
public class Template extends ListenerAdapter {
	
	//Creates an event. This line is called everytime someone sends a message.
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		//Checks if the command exists.
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "__COMMAND_NAME__")) {
			/* 
			 * Sends a message. Change the String inside sendMessage(), don't change anything else.
			 * How it works:
			 * It checks the event and asks for the channel where the command was sent from,
			 * then sends the message "Test success!",
			 * then adds it to the queue, essentially, sending the message.
			 */
			event.getChannel().sendMessage("Test success!").queue();
			
			//A custom print command, quicker to type.
			P.print("Test success! Sender: " + event.getMember());
		}
	}
}
