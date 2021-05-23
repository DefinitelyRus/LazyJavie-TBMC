/*
 * ---------------!!! ADD TO README !!!---------------
 * The SHOP command allows members to buy rewards using points they have accumulated during their stay in the server.
 * As of version 1.0, members can only buy server roles as rewards.
 * This will be updated later on to allow for more rewards to be set by the server admins.
 * BUY is also the only sub-command under the SHOP command. There will be more soon as well.
 * 
 */

package commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class shop extends ListenerAdapter {
	
	@SuppressWarnings("unlikely-arg-type")
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		//<BUY: NO NAME> For when a member attempts to buy a role but doesn't enter a name.
		if(args.length == 2 && args[1].equalsIgnoreCase("buy")) {
			String requestby = event.getMember().getUser().getName();
			P.print("Missing argument.");
			//Embed block
			EmbedBuilder buyRole = new EmbedBuilder();
			buyRole.setColor(0xffae00);
			buyRole.setTitle("You didn't enter a role to buy");
			buyRole.setDescription("To purchase a role, type `" + LazyJavie.prefix + "shop buy [role]`" + "\r\n" + "");
		    buyRole.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
			event.getChannel().sendMessage(buyRole.build()).queue();
		}
	    
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "shop")) {
			P.print("\n[shop] Shop query by: " + event.getMember().getUser().getName());
			
			//Initialization
			String requestby = event.getMember().getUser().getName();
			List<Role> roles = event.getGuild().getRoles();
			P.print("Getting list of blacklisted roles...");
	    	List<String> blacklist = null;
		    StringBuilder displayRoles = new StringBuilder();
		    try {blacklist = SQLconnector.getList("select * from lazyjavie.roleblacklist", "rolename", false);}
		    catch (LoginException e) {P.print("Error encountered: " + e.toString());}
		    catch (SQLException e) {P.print("Error encountered: " + e.toString());}

			//<SHOP: EMPTY LIST>Checks if there are no roles in the server.
			if (roles.isEmpty()) {
				EmbedBuilder shopRoles = new EmbedBuilder();
				shopRoles.setColor(0xffae00);
				shopRoles.setTitle("There are no roles in this guild!");
				event.getChannel().sendMessage(shopRoles.build()).queue();
				return;
		    }
			
			//-------------------------Blacklist Block starts here.-------------------------
		    //Checks for blacklisted roles; only includes non-blacklisted roles.
		    P.print("Removing blacklisted roles from output..."); 
		    for (Role r : roles) {
	    		int rolePrice = 2147483647;
		    	String roleName = r.getName().toString();
		    	
		    	//TODO (Optional) Move this to database
		    	boolean[] permsArray = {
	    			r.hasPermission(Permission.ADMINISTRATOR),
	    			r.hasPermission(Permission.KICK_MEMBERS),
	    			r.hasPermission(Permission.BAN_MEMBERS),
	    			r.hasPermission(Permission.MANAGE_SERVER),
	    			r.hasPermission(Permission.MANAGE_CHANNEL),
	    			r.hasPermission(Permission.MANAGE_PERMISSIONS),
	    			r.hasPermission(Permission.MESSAGE_MANAGE),
	    			r.hasPermission(Permission.MANAGE_EMOTES),
	    			r.hasPermission(Permission.MANAGE_WEBHOOKS)
			    };
		    	
		    	//Adds the array to a list.
			    ArrayList<Boolean> perms = new ArrayList<Boolean>();
			    for (boolean item : permsArray) {perms.add(item);}
		    	
			    //Checks if the player has the currently iterated role.
			    if (event.getMember().getRoles().contains(r)) {P.print("|Blacklisted " + r.getName() + " (Already claimed)"); continue;}
			    
		    	//Any role colored #696969 (Hex); filters out bots.
			    else if(r.getColorRaw() == 0x696969) {P.print("|Blacklisted " + r.getName()); continue;}
		    	
		    	//Any role with the permission
		    	else if (perms.contains(true)) {P.print("|Blacklisted " + r.getName()); continue;}
		    	
		    	//Any role part of the blacklist.
		    	else if (blacklist.contains(r.getName().toLowerCase())) {P.print("|Blacklisted " + r.getName()); continue;}
		    	
		    	else {
		    		//Sets the role's price.
		    		try {
			    		P.print("Getting role price...");
				    	rolePrice = Integer.parseInt(SQLconnector.get("SELECT * FROM lazyjavie.shop WHERE itemname='"+ r.getName() +"'", "price", false));
		    		}
		    		//Likely caused by NULL price from database.
		    		catch (NumberFormatException e) {
		    			P.print("Price of role '" +r+ "' cannot be parsed to integer; setting to max value.");
		    			rolePrice = 2147483647;
	    			}
		    		catch (LoginException e) {P.print("Error encountered: " + e.toString());}
		    		catch (SQLException e) {P.print("Error encountered: " + e.toString());}
		    		
			    	//Adds the role to the displayed list.
		    		displayRoles.append("• **" + r.getName() + ":** " + rolePrice + " points").append("\n");
					P.print("Price of " +r.getName()+ " is " +rolePrice);
		    		
		    		//Adds the role to the table.
					try {
						String x = SQLconnector.get("select * from lazyjavie.shop where itemname = '" +roleName+ "'", "itemname", false);
						boolean exists = !x.equals("Empty result.");
						
						if (exists == false) {
							P.print("Adding " + roleName + " to the database...");
							SQLconnector.update("INSERT INTO lazyjavie.shop (itemname, price) VALUES ('" + roleName + "', 0);", false);
						} else {P.print(roleName + " already exists in database; skipping...");}
					} catch (SQLException e) {}
					catch (Exception e) {e.printStackTrace();}
		    	}	
		    }	//-------------------------Blacklist block ends here.-------------------------
		    P.print("Blacklist check done.");
		    
		    //Displays the items available for purchase.
			if (args.length < 2 && args[0].equalsIgnoreCase(LazyJavie.prefix + "shop")){
				P.print("Missing arguments.");
				//Embed block
				EmbedBuilder shop = new EmbedBuilder();
				shop.setColor(0xffae00);
				shop.setTitle(":moneybag: Welcome to the LazyJavie shop! :moneybag:");
				shop.setDescription("To purchase a role, type `" + LazyJavie.prefix + "shop buy [role]`" + "\r\n" + "");
			    event.getGuild().getRolesByName("bots", true);
				shop.addField("List of Available Roles:","" + displayRoles , true);
	     		shop.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(shop.build()).queue();
				return;
			}
			
			//-------------------------[BUY] A successful attempt at purchasing.-------------------------
			else if (args.length > 2 && args[1].equalsIgnoreCase("buy")) {
				P.print("\n[shop] Buy request by: " + event.getMember().getUser().getName());
				try {
					P.print("Checking through roles...");
				    for (Role r : roles) {
						if(args[2].equals(r.getName())) {
							
							//Initialization. Gets the role's price.
							String memberId = event.getMessage().getMember().getId();
							int price = 0;
							int pts = 0;
							
							//Gets the # of points a member has.
							try {
								pts = Integer.parseInt(SQLconnector.get("select points from lazyjavie.members WHERE userid=" + memberId + ";", "points", false));
							}
							catch (NumberFormatException e) {
								//TODO Turn this to embed.
								event.getChannel().sendMessage("Type `" +LazyJavie.prefix+ "register <password>` to use the shop.").queue();
								P.print("Request cancelled: Member not registered.");
								return;
							}
							try {price = Integer.parseInt(SQLconnector.get("SELECT * FROM lazyjavie.shop WHERE itemname='" +r.getName()+ "'", "price", false));}
							catch (NumberFormatException e) {
								price = 2147483647;
								P.print("Price of role '" +r+ "' cannot be parsed to integer; setting to max value.");
								event.getChannel().sendMessage("Error ecountered: `" + e.toString() + "`\nPlease contact an admin to set a price.");
								return;
							}
							catch (Exception e) {
								P.print("Error encountered: " + e.toString());
								return;
							}
							//<BUY: NO PRICE> Checks if there is no set price.
							//REDUNDANT: TODO CONSIDER FOR REMOVAL
							if (price == 2147483647) {
								P.print("Purchase cancelled: No price set.");
								EmbedBuilder noPrice = new EmbedBuilder();
								noPrice.setColor(0xD82D42);
								noPrice.addField("There is no set price for that role!","Current points: `" + pts + "`",true);
								noPrice.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
							    event.getChannel().sendMessage(noPrice.build()).queue();
							    return;
					        }

					        //<BUY: SUCCESS> Successful purchase.
					        else if (price <= pts && args[2].equalsIgnoreCase(r.getName()) && !blacklist.contains(args[2].toLowerCase())) {
				        		
					        	P.print("Filter checked.");
								
								//Initialization
								Member member = event.getMember();
								Role role = event.getGuild().getRoleById(r.getId());
								
								P.print("Deducting points...");
								//Points deduction
								pts -= price;
								SQLconnector.update("UPDATE lazyjavie.members "+ "SET points = " + pts + " WHERE userid=" + memberId + ";", false);
								
								P.print("Applying role...");
								//Role application
								event.getGuild().addRoleToMember(member, role).queue();;
								event.getGuild().modifyMemberRoles(member, role).queue();;
								P.print("Purchase successful.");

					        	//Embed block
								EmbedBuilder purchaseComplete = new EmbedBuilder();
								purchaseComplete.setColor(0xD82D42);
								purchaseComplete.addField("You have purchased the role: ", "`" + r.getName() + "`", true);
								event.getChannel().sendMessage(purchaseComplete.build()).queue();
								return;
					        }	
							//<BUY: INSUFFICIENT FUNDS> Not enough funds.
					        else if (price > pts  && args[2].equalsIgnoreCase(r.getName()) && !blacklist.contains(args[2].toLowerCase())) {
					        	P.print("Purchase cancelled: Not enough points.");
					        	EmbedBuilder noMoney = new EmbedBuilder();
					        	noMoney.setColor(0xD82D42);
					        	noMoney.addField("You don't have enough points", "Current points: `" + pts + "`", true);
							    noMoney.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
								event.getChannel().sendMessage(noMoney.build()).queue();
								return;
					        }
				    	} 
				    }
				    

			    }
				catch (HierarchyException e) {
					P.print("Purchase cancelled: Role already applied.");
		        	EmbedBuilder roleApplied = new EmbedBuilder();
		        	roleApplied.setColor(0xD82D42);
		        	roleApplied.setDescription("You already have " +args[2]+ "!");
		        	roleApplied.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(roleApplied.build()).queue();
					return;
				}
				catch (Exception e) {
			    	P.print("Error encountered: " + e); e.printStackTrace();
			    	return;
			    }
				
			if (args.length > 2 && !roles.contains(args[2])) {
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