package commands;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.security.auth.login.LoginException;
import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class adminShop extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "ashop") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			
			//Initialization
			String requestby = event.getMember().getUser().getName();
			
			//<ASHOP: MISSING ARGS> Checks if there are no additional arguments.
			if (args.length == 1) {
				P.print("\n[ashop] Invalid command by: " + event.getMember().getUser().getName());
				event.getChannel().sendMessage("Correct syntax: `" +LazyJavie.prefix+ " <subcommands> <arguments>`").queue();
				//TODO Replace message with guide & available commands.
				return;
			}
			
			//[BLACKLIST] Blacklists a role from the shop.-------------------------
			if (args[1].equalsIgnoreCase("blacklist")) {
				P.print("\n[ashop] Blacklist request by: " + event.getMember().getUser().getName());
		    	try {
		    		
		    		//Gets a list of already blacklisted roles.
		    		P.print("Gathering list...");
		    		LinkedList<String> dbblacklist = SQLconnector.getList("select * from lazyjavie.roleblacklist", "rolename", true);
		    		
		    		//Checks if the role has already been blacklisted before.
		    		P.print("Checking for duplicates...");
		    		if (dbblacklist.contains(args[1].toLowerCase())) {event.getChannel().sendMessage(args[1] + " is already blacklisted.");}
		    		else {SQLconnector.update("insert into lazyjavie.roleblacklist (rolename) values (" + args[1].toLowerCase() + ")", true);}
		    		P.print("Successfully added " +args[1]+ " to blacklist.");
		    	}
		    	catch (LoginException e) {P.print("Error encountered: " + e.toString());}
				catch (SQLException e) {P.print("Error encountered: " + e.toString());}
		    	catch (Exception e) {
		    		P.print("Error encountered: " + e);
					event.getChannel().sendMessage("Error encountered: `" +e.toString()+ "`\nCorrect syntax: `" +LazyJavie.prefix+ "blacklist <role>`").queue();
		    	}
		    }
			
			List<Role> roles = event.getGuild().getRoles();

			//-------------------------[SETPRICE] Changes the price of a reward.-------------------------
			//TODO (Optional) Combine this and the FOR loop below.
			//<SETPRICE: MISSING ARGS> No role and/or price specified.
			if(args[1].equalsIgnoreCase("setprice") && args.length >= 3) {
				P.print("\n[ashop] Setprice request by: " + event.getMember().getUser().getName());
				P.print("Request failed: Missing arguments.");
				
				//Embed block
				EmbedBuilder setPrice1 = new EmbedBuilder();
				setPrice1.setColor(0x77B255);
				setPrice1.setTitle("To set a price for a role:");
				setPrice1.setDescription(LazyJavie.prefix + "setprice [role] [price]");
				setPrice1.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(setPrice1.build()).queue();
			}
			
			for (Role r : roles) {
				//TODO fix this (might break due to some roles having spaces):
				//<SETPRICE: SUCCESS>
				if (args[1].equalsIgnoreCase("setprice") && args[2].equals(r.getName()) && args.length > 2) {
					try {
						P.print("\n[ashop] Setprice request by: " + event.getMember().getUser().getName());
						P.print("Updating price...");
						P.print("Target: " +args[2]+ "; Price: " +args[3]);
						SQLconnector.update("update lazyjavie.shop set price = " +args[3]+ " where itemname = '" +args[2]+ "';", false);
						P.print("Price updated successfully.");
						
						//Embed Block
						EmbedBuilder setPrice3 = new EmbedBuilder();
						setPrice3.setColor(0x77B255);
						setPrice3.setDescription("Price set for "+ args[2] + ": "+ args[3] + " points");
						setPrice3.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
						event.getChannel().sendMessage(setPrice3.build()).queue();
					} catch (Exception e) {
						P.print("Error encountered: " + e);
						event.getChannel().sendMessage("Error encountered: `" +e.toString()+ "`\nCorrect syntax: `" +LazyJavie.prefix+ "ashop <role> <price>`").queue();
					}
				}
			}
		}
	}
}

