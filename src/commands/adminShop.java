/*
 * ---------------!!! ADD TO README !!!---------------
 * ADMINSHOP or ASHOP is an admin-only command where administrators can change the settings of their server's shop.
 * 
 * Available subcommands are as follows:
 * 
 * -	$ashop blacklist <role>
 * 		This command removes a role from the shop inventory, preventing players from purchasing certain roles.
 * 
 * -	$ashop setprice <role> <price>
 * 		Updates the price of a certain role.
 * 
 * -	[TO BE ADDED] $ashop setstock <role> <stock>
 * 		Updates how many times a role can be purchased.
 * 
 * -	[TO BE ADDED] $ashop setdesc <role> <description>
 * 		Updates the product description to be displayed.
 */

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
				P.print("Missing arguments.");
				event.getChannel().sendMessage("Correct syntax: `" +LazyJavie.prefix+ "ashop <subcommands> <arguments>`").queue();
				//TODO Replace message with guide & available commands.
				return;
			}
			
			//[BLACKLIST] Blacklists a role from the shop.-------------------------
			if (args[1].equalsIgnoreCase("blacklist")) {
				P.print("\n[ashop] Blacklist request by: " + event.getMember().getUser().getName());
				
				if (args.length < 3) {
					P.print("Missing arguments.");
					event.getChannel().sendMessage("Correct syntax: `" +LazyJavie.prefix+ "ashop blacklist <arguments>`").queue();
					return;
				} else {
			    	try {
			    		//Gets a list of already blacklisted roles.
			    		P.print("Gathering list...");
			    		LinkedList<String> dbblacklist = SQLconnector.getList("select * from lazyjavie.roleblacklist", "rolename", true);
			    		
			    		//<BLACKLIST: ALREADY LISTED> Checks if the role has already been blacklisted before.
			    		P.print("Checking for duplicates...");
			    		if (dbblacklist.contains(args[2].toLowerCase())) {
			    			P.print(args[2] + " is already blacklisted.");
			    			event.getChannel().sendMessage("`" +args[2]+ "` is already blacklisted.").queue();
			    			return;
		    			}
			    		
			    		//<BLACKLIST: SUCCESS>
			    		else {
		    				P.print("Adding " +args[2]+ " to blacklist.");
			    			SQLconnector.update("insert into lazyjavie.roleblacklist (rolename) values ('" + args[2].toLowerCase() + "')", true);
			    			P.print("Successfully added " +args[2]+ " to blacklist.");
			    			event.getChannel().sendMessage("Successfully added `" +args[2].toLowerCase()+ "` to blacklist.").queue();
			    			return;
			    		}
			    	}
			    	catch (LoginException e) {P.print("Error encountered: " + e.toString()); return;}
					catch (SQLException e) {P.print("Error encountered: " + e.toString()); return;}
			    	catch (Exception e) {
			    		P.print("Error encountered: " + e);
						event.getChannel().sendMessage("Error encountered: `" +e.toString()+ "`\nCorrect syntax: `" +LazyJavie.prefix+ "blacklist <role>`").queue();
						return;
			    	}
				}
		    }
			
			List<Role> roles = event.getGuild().getRoles();

			//-------------------------[SETPRICE] Changes the price of a reward.-------------------------
			//TODO (Optional) Combine this and the FOR loop below.
			if (args[1].equalsIgnoreCase("setprice")) {
				//<SETPRICE: MISSING ARGS> No role and/or price specified.
				if(args.length < 3) {
					P.print("\n[ashop] Setprice request by: " + event.getMember().getUser().getName());
					P.print("Request failed: Missing arguments.");
					
					//Embed block
					EmbedBuilder setPrice1 = new EmbedBuilder();
					setPrice1.setColor(0x77B255);
					setPrice1.setTitle("To set a price for a role:");
					setPrice1.setDescription(LazyJavie.prefix + "ashop setprice [role] [price]");
					setPrice1.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(setPrice1.build()).queue();
					
					return;
				}
				
				else {
					for (Role r : roles) {
						//TODO fix this (might break due to some roles having spaces):
						//<SETPRICE: SUCCESS>
						if (args[2].equalsIgnoreCase(r.getName()) && args.length > 2) {
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
								return;
							} catch (Exception e) {
								P.print("Error encountered: " + e);
								event.getChannel().sendMessage("Error encountered: `" +e.toString()+ "`\nCorrect syntax: `" +LazyJavie.prefix+ "ashop <role> <price>`").queue();
							}
						}
					}
				}
			}
			//This will print out if any part of the function isn't properly closed with a RETURN statement.
			P.print("\nUNRETURNED FUNTION: " + event.getMessage().getContentRaw());
		}
	}
}

