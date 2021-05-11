/*
 * 
 */
package commands;

import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.LoginException;

import bot_init.EventWaiter;
import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TestRegister extends ListenerAdapter{
	private EventWaiter waiter;
	

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		String requestby = event.getMember().getUser().getName();
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "register")) {
			P.print("\nRegistration request: " + event.getMember().getUser());
			try {
				P.print("Attempting registration...");
				String password = args[1];
				List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
				event.getChannel().deleteMessages(messages).queue();
				
				SQLconnector.update("insert into lazyjavie.members (userid, userpass) values (\"" + event.getMember().getId() + "\", \"" + password + "\")");
				String returnPass = SQLconnector.get("select * from lazyjavie.members where userid = \"" + event.getMember().getId() + "\"", "userpass");
				event.getChannel().sendMessage(event.getMember().getAsMention() + " has been registered successfully. (" + returnPass + ")").queue();
				P.print("Registered member: " + event.getMember().getUser() + "");
			}
			catch (Exception e) {
				if (e.toString().startsWith("java.lang.ArrayIndexOutOfBoundsException: Index")) {
					event.getChannel().sendMessage("Please enter an argument. `>>register [anything]`").queue();
				} else { event.getChannel().sendMessage("Error: `" + e + "`").queue();}

				P.print(e.toString());
			}

			
		}
		
		//UNREGISTER
		 if (args[0].equalsIgnoreCase(LazyJavie.prefix + "unregister")) {
				EmbedBuilder unregister = new EmbedBuilder();
				unregister.setColor(0xD82D42);
				unregister.setTitle("Are you sure you want to unregister?");
				unregister.setDescription("enter `>>yes` if you are sure." + "\r\n" + "enter `>>no` if you want to cancel.");			
				unregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(unregister.build()).queue();
				 waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> args[0].equalsIgnoreCase(LazyJavie.prefix + "yes") , e -> {
						if(args[0].equalsIgnoreCase(LazyJavie.prefix + "yes" )) {
							try {
								SQLconnector.update("DELETE FROM lazyjavie.members WHERE userid=" + event.getMember().getId());
							} catch (LoginException er) {
								// TODO Auto-generated catch block
								er.printStackTrace();
							} catch (SQLException er) {
								// TODO Auto-generated catch block
								er.printStackTrace();
							}
							EmbedBuilder successUnregister = new EmbedBuilder();
							successUnregister.setColor(0x77B255);
							successUnregister.setTitle("Successfully deleted the user: " + event.getMember().getId());			
							successUnregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
							event.getChannel().sendMessage(successUnregister.build()).queue();
							
						}
				 });
				 waiter.waitForEvent(GuildMessageReceivedEvent.class, e -> args[0].equalsIgnoreCase(LazyJavie.prefix + "no")  , e -> {
							EmbedBuilder successUnregister = new EmbedBuilder();
							successUnregister.setColor(0x77B255);
							successUnregister.setTitle("Cancelled your request.");		
							successUnregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
							event.getChannel().sendMessage(successUnregister.build()).queue();

				 });
		}
   //e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel())


	}
}
