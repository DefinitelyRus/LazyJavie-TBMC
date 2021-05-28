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
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class adminShop extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		String msg = event.getMessage().getContentRaw();
		
		
		LinkedList<String> foradminlist = null;
		String[] foradminarray = {};
		boolean isAdmin = event.getMember().hasPermission(Permission.ADMINISTRATOR);
		try {
			P.print("Getting lists from database...");
			foradminlist = SQLconnector.getList("select * from lazyjavie.helplist where adminonly = 1", "cmd", false);
			
			P.print("Converting to arrays...");
			foradminlist.toArray(foradminarray);
		}
		
    	catch (LoginException e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
		catch (SQLException e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
		catch (Exception e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
		

		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "ashop") && isAdmin) {
			
			//Initialization
			String requestby = event.getMember().getUser().getName();
			
			//<ASHOP: MISSING ARGS> Checks if there are no additional arguments.
			if (args.length == 1) {
				P.print("Missing arguments.");
				String adminRoles = "";
				// ----- Displays admin commands -----
				EmbedBuilder ashop = new EmbedBuilder();
				ashop.setColor(0xffae00);
				ashop.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				// Admin commands:
				for (String _adminRole : foradminlist) {
					if (_adminRole.equals(LazyJavie.prefix) || _adminRole.equals("$")) {} 
					else {
						adminRoles = adminRoles + "\n\n> [ADMIN] " + LazyJavie.prefix + _adminRole;
					}
				}
				ashop.addField("Correct syntax: `" +LazyJavie.prefix+ "ashop <subcommands> <arguments>` \n\r Admin Commands:", adminRoles, true);
				event.getChannel().sendMessage(ashop.build()).queue();
				return;
			}
			
			//[BLACKLIST] Blacklists a role from the shop.-------------------------
			if (args[1].equalsIgnoreCase("blacklist")) {
				P.print("\n[ashop] Blacklist request by: " + event.getMember().getUser().getName());
				
				if (args.length < 3) {
					P.print("<BlackList> Missing arguments.");
		        	EmbedBuilder missingArgsBlackList = new EmbedBuilder();
		        	missingArgsBlackList.setColor(0xD82D42);
		        	missingArgsBlackList.setDescription("Correct syntax: `" +LazyJavie.prefix+ "ashop blacklist <arguments>`");
		        	missingArgsBlackList.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(missingArgsBlackList.build()).queue();					
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
				        	EmbedBuilder alreadyBlackListed = new EmbedBuilder();
				        	alreadyBlackListed.setColor(0xD82D42);
				        	alreadyBlackListed.setDescription("`" +args[2]+ "` is already blacklisted.");
				        	alreadyBlackListed.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
							event.getChannel().sendMessage(alreadyBlackListed.build()).queue();								
			    			return;
		    			}
			    		
			    		//<BLACKLIST: SUCCESS>
			    		else {
			    			
		    				P.print("Adding " +args[2]+ " to blacklist.");
			    			SQLconnector.update("insert into lazyjavie.roleblacklist (rolename) values ('" + args[2].toLowerCase() + "')", true);
			    			P.print("Successfully added " +args[2]+ " to blacklist.");
				        	EmbedBuilder successBlackList = new EmbedBuilder();
				        	successBlackList.setColor(0xD82D42);
				        	successBlackList.setDescription("Successfully added `" +args[2].toLowerCase()+ "` to blacklist.");
				        	successBlackList.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
							event.getChannel().sendMessage(successBlackList.build()).queue();
			    			return;
			    		}
			    	}
			    	catch (LoginException e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
					catch (SQLException e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
			    	catch (Exception e) {
			    		P.print("Error encountered: " + e);
			    		SQLconnector.callError(msg, e.toString());
			    		
			        	EmbedBuilder missingArgsBlackList = new EmbedBuilder();
			        	missingArgsBlackList.setColor(0xD82D42);
			        	missingArgsBlackList.setDescription("Error encountered: `" +e.toString()+ "`\nCorrect syntax: `" +LazyJavie.prefix+ "blacklist <role>`");
			        	missingArgsBlackList.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
						event.getChannel().sendMessage(missingArgsBlackList.build()).queue();							
						return;
			    	}
				}
				// ---------- < REMOVES A BLACKLISTED ROLE > ----------
		    } else if (args[1].equalsIgnoreCase("!blacklist")) {
		    	try {
    				P.print("Removing " +args[2]+ " from blacklist.");
	    			SQLconnector.update("DELETE FROM lazyjavie.roleblacklist WHERE rolename=('" + args[2] + "')", true);
	    			P.print("Successfully deleted " +args[2]+ " from blacklist.");
		        	EmbedBuilder successBlackList = new EmbedBuilder();
		        	successBlackList.setColor(0xD82D42);
		        	successBlackList.setDescription("Successfully deleted `" + args[2]+ "` from blacklist.");
		        	successBlackList.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(successBlackList.build()).queue();
	    			return;
		    	} 
		    	catch (Exception e) {
		    		P.print("Error encountered: " + e);
		    		SQLconnector.callError(msg, e.toString());
		    		
		        	EmbedBuilder missingArgsBlackList = new EmbedBuilder();
		        	missingArgsBlackList.setColor(0xD82D42);
		        	missingArgsBlackList.setDescription("Error encountered: `" +e.toString()+ "`\nCorrect syntax: `" +LazyJavie.prefix+ "blacklist <role>`");
		        	missingArgsBlackList.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(missingArgsBlackList.build()).queue();							
					return;
		    	}
		    	// ---------- < LISTS ALL BLACKLISTED ROLES > ----------
		    } else if (args[1].equalsIgnoreCase("viewblacklist")) {
		        LinkedList<String> forblacklist = null;
		        String[] forblacklistarray = {};
		    	try {
    				P.print("Attempting to list all blacklisted roles.");	
    				forblacklist = SQLconnector.getList("select * from lazyjavie.roleblacklist", "rolename", false);
    				
    				P.print("Converting to arrays...");
    				forblacklist.toArray(forblacklistarray);
		    	} 	
		    	catch (LoginException e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
				catch (SQLException e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
				catch (Exception e) {P.print("Error encountered: " + e.toString()); SQLconnector.callError(msg, e.toString()); return;}
		    	
		    	String blacklistedRoles = "";
		    	
				for (String blRole : forblacklist) {
					if (blRole.equals("$")) {
						continue;
					}
					blacklistedRoles = blacklistedRoles + blRole + " ,"; 
				}
				P.print("Sucessfully listed all blacklisted roles.");
				EmbedBuilder listBlackList = new EmbedBuilder();
				listBlackList.setColor(0xD82D42);
				MentionType.USER.getPattern();
				listBlackList.addField("Blacklisted roles: ", blacklistedRoles, true);
				listBlackList.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(listBlackList.build()).queue();
				return;
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
								SQLconnector.callError(msg, e.toString());
								event.getChannel().sendMessage("Error encountered: `" +e.toString()+ "`\nCorrect syntax: `" +LazyJavie.prefix+ "ashop <role> <price>`").queue();
							}
						}
					}
				}
				if (args.length >= 2) {
					//<BUY: ROLE DOESNT EXIST>
		        	P.print("Purchase cancelled: Role doesnt exist");
		        	EmbedBuilder roleNoExist = new EmbedBuilder();
		        	roleNoExist.setColor(0xD82D42);
		        	roleNoExist.setDescription(args[2] + " does not exist");
		        	roleNoExist.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(roleNoExist.build()).queue();
					return;
				    }
			}
			//This will print out if any part of the function isn't properly closed with a RETURN statement.
			P.print("\nUNRETURNED FUNTION: " + event.getMessage().getContentRaw());
		}
	}
}

