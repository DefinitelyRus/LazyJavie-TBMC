/*
 * 
 */
package commands;

import java.sql.SQLException;
import java.util.List;
import javax.security.auth.login.LoginException;
import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Register extends ListenerAdapter{	

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
				String returnPass = SQLconnector.get("select * from lazyjavie.members where userid = \"" + event.getMember().getId() + "\"", "userpass", true);
				EmbedBuilder successRegister = new EmbedBuilder();
				successRegister.setColor(0x77B255);
				successRegister.setTitle("You have successfully registered " + event.getMember().getUser() + " (" + returnPass + ") ");
				successRegister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(successRegister.build()).queue();
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
			 if(args.length < 2) {
					EmbedBuilder unregister = new EmbedBuilder();
					unregister.setColor(0xD82D42);
					unregister.setTitle("Are you sure you want to unregister?");
					unregister.setDescription("enter `"+ LazyJavie.prefix + "unregister yes` if you are sure." + "\r\n" + "enter `" + LazyJavie.prefix + "unregister no` if you want to cancel.");			
					unregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(unregister.build()).queue();
			 }

						if(event.getMessage().getContentRaw().equalsIgnoreCase(LazyJavie.prefix + "unregister " + "yes" )) {
							try {
								//Purpose of SET SQL_SAFE_UPDATEs = 0 - Error Code: 1175
								SQLconnector.update("SET SQL_SAFE_UPDATES = 0;\r\n"
										+ "DELETE FROM lazyjavie.members WHERE userid=" + event.getMember().getId());
							} catch (LoginException er) {
								er.printStackTrace();
							} catch (SQLException er) {
								er.printStackTrace();
							}
							
							EmbedBuilder successUnregister = new EmbedBuilder();
							successUnregister.setColor(0x77B255);
							successUnregister.setTitle("Successfully deleted the user: " + event.getMember().getId());			
							successUnregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
							event.getChannel().sendMessage(successUnregister.build()).queue();
						}
						if (event.getMessage().getContentRaw().equalsIgnoreCase(LazyJavie.prefix + "unregister " + "no" )) {
							EmbedBuilder successUnregister = new EmbedBuilder();
							successUnregister.setColor(0x77B255);
							successUnregister.setTitle("Cancelled your request.");		
							successUnregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
							event.getChannel().sendMessage(successUnregister.build()).queue();
						}

		}
   //e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel())
	}
}
