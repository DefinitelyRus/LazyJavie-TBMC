/*
 * TODO Write documentation
 */
package commands;

import java.sql.SQLException;
import javax.security.auth.login.LoginException;
import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Register extends ListenerAdapter{	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		String requestby = event.getMember().getUser().getName();
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		//-------------------------REGISTER-------------------------
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "register")) {
			P.print("\nRegistration request: " + event.getMember().getUser());
			try {
				
				//Initialization
				String password = args[1];
				
				//This part is soft-removed due to minor issues and generally lack of usefulness.
				//List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
				//event.getChannel().deleteMessages(messages).queue();
				
				//Checks if the member is already registered.
				try {
					P.print("Attempting registration...");
					String s = SQLconnector.get("select * from lazyjavie.members where userid = \"" + event.getMember().getId() + "\"", "userid", true);
					
					//<MEMBER ALREADY REGISTERED>
					if (s != "Empty result.") {
						P.print("Member found. Cancelling. " + s);
						//TODO Turn this into an embed.
						event.getChannel().sendMessage("You are already registered.").queue();
						return;
					}
				}
				catch (LoginException e) {P.print("Error encountered: " + e.toString()); return;}
				catch (SQLException e) {P.print("Error encountered: " + e.toString()); return;}
				catch (Exception e) {P.print("Error encountered: " + e.toString()); return;}
				
				P.print("Registering...");
				SQLconnector.update("insert into lazyjavie.members (userid, userpass) values (\"" + event.getMember().getId() + "\", \"" + password + "\")",true);
				
				//Embed block
				EmbedBuilder successRegister = new EmbedBuilder();
				successRegister.setColor(0x77B255);
				successRegister.setTitle("You have successfully been registered, " + event.getMember().getUser().getName() + "!");
				successRegister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(successRegister.build()).queue();
				P.print("Registered member: " + event.getMember().getUser() + "");
			}
			catch (LoginException e) {P.print("Error encountered: " + e.toString()); return;}
			catch (SQLException e) {P.print("Error encountered: " + e.toString()); return;}
			catch (ArrayIndexOutOfBoundsException e) {
				P.print("Cancelling; missing argument.");
				event.getChannel().sendMessage("Please enter an argument. `>>register [anything]`").queue();
				return;
			}
			//TODO Turn ithis into an embed.
			catch (Exception e) {event.getChannel().sendMessage("Error encountered: `" + e + "`").queue(); return;}
		}
		
		//-------------------------DEREGISTER-------------------------
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "deregister")) {
			P.print("\nDeregistration request: " + event.getMember().getUser().getName());
			
			//Gets the member's password.
			P.print("Getting the member's password...");
			String pass = null;
			try {pass = SQLconnector.get("select * from lazyjavie.members where userid ='" + event.getMember().getId() + "'", "userpass", true);}
			catch (LoginException e) {P.print(e.toString()); return;}
			catch (SQLException e ) {P.print(e.toString()); return;}
			catch (Exception e) {P.print(e.toString()); return;}
			
			//<DEREGISTER: MISSING ARGS> Checks if there are missing arguments.
			P.print("Checking for invalid arguments...");
			if(args.length < 2) {
				P.print("Cancelling; missing argument.");
				EmbedBuilder deregister = new EmbedBuilder();
				deregister.setColor(0xD82D42);
				deregister.setTitle("Are you sure you want to deregister?");
				deregister.setDescription("Enter `"+ LazyJavie.prefix + "deregister <password>` to confirm.");			
				deregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(deregister.build()).queue();
				return;
			}
			
			//<DEREGISTER: SUCCESS> Deregisters the member.
			else if(args[1].equals(pass)) {
				P.print("Deregistering...");
				try {
					//[REMOVED] Purpose of SET SQL_SAFE_UPDATEs = 0 - Error Code: 1175
					SQLconnector.update("DELETE FROM lazyjavie.members WHERE userid=" + event.getMember().getId(), true);
				}
				catch (LoginException e) {P.print(e.toString()); return;}
				catch (SQLException e) {P.print(e.toString()); return;}
				catch (Exception e) {P.print(e.toString()); return;}
				
				P.print("Successfully deregistered " + event.getMember().getUser().getName());
				
				//Embed block
				EmbedBuilder successDeregister = new EmbedBuilder();
				successDeregister.setColor(0x77B255);
				successDeregister.setTitle("Successfully deleted the user: `" + event.getMember() + "` from the database.");			
				successDeregister.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(successDeregister.build()).queue();
			}
			
			//<UNREGISER: INVALID INPUT> When the user enters an incorrect password.
			else {
				P.print("Invalid password: " + event.getMessage().getContentRaw());
				//TODO Turn this into an embed.
				event.getChannel().sendMessage("Incorrect password!").queue();
			}
		}
	}
}
