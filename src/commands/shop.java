package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class shop extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
	    
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "shop")) {
			P.print("[shop] Shop query by: " + event.getMember().getUser().getName());
			
			//Initialization
			String requestby = event.getMember().getUser().getName();
			List<Role> roles = event.getGuild().getRoles();
			P.print("Getting list of blacklisted roles...");
	    	List<String> blacklist = SQLconnector.getList("select * from lazyjavie.roleblacklist", "rolename");
		    StringBuilder displayRoles = new StringBuilder();
		    
		    
		    //Checks for blacklisted roles; only includes non-blacklisted roles.
		    P.print("Removing blacklisted roles from output...");
		    for (Role r : roles) {

		    	//TODO Move this to database
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
		    	
		    	//Any role colored #696969 (Hex)
		    	if(r.getColorRaw() == 0x696969) {}
		    	
		    	//Any role with the permission [ADMINISTRATOR,KICK_MEMBERS,BAN_MEMBERS]
		    	else if (perms.contains(true)) {P.print("HAHA GoTCHA BITCH");}
		    	
		    	//Any role colored 105 (?)
		    	else if (r.getColorRaw() == 105) {}
		    	
		    	//Any role part of the blacklist.
		    	else if (blacklist.contains(r.getName().toLowerCase())) {P.print("BLACKLISTED " + r.getName());}
		    	
		    	//CONSIDER FOR REMOVAL. @everyone is in the blacklist.
		    	else if (r.getName() == "@everyone" || r.getName() == "everyone" || r.getPosition() == 0) {}
		    	
		    	//Role gets added to the displayed list.
		    	else {displayRoles.append("• " + r.getName()).append("\n");}	
		    }

			//Checks if there are no roles in the server.
			if (roles.isEmpty()) {
				EmbedBuilder shopRoles = new EmbedBuilder();
				shopRoles.setColor(0xffae00);
				shopRoles.setTitle("There are no roles in this guild!");
				event.getChannel().sendMessage(shopRoles.build()).queue();
				return;
		    }
			
		    //Displays the items available for purchase.
			if (args.length == 1){
				EmbedBuilder shop = new EmbedBuilder();
				shop.setColor(0xffae00);
				shop.setTitle(":moneybag: Welcome to the LazyJavie shop! :moneybag:");
				shop.setDescription("To purchase a role, type '>>shop buy [role]'" + "\r\n" + "");
			    event.getGuild().getRolesByName("bots", true);
				shop.addField("List of Available Roles:","" + displayRoles , true);
	     		shop.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(shop.build()).queue();
			}
		    
			//Check if there are no additional arguments.
		    if (args.length > 1) {
		    	
				//[BLACKLIST] Blacklists a role from the shop.
				if (args[1].equalsIgnoreCase("blacklist")) {
			    	try {
			    		//Gets a list of already blacklisted roles.
			    		LinkedList<String> dbblacklist = SQLconnector.getList("select * from lazyjavie.roleblacklist", "rolename");
			    		
			    		//Checks if the role has already been blacklisted before.
			    		if (dbblacklist.contains(args[1].toLowerCase())) {event.getChannel().sendMessage(args[1] + " is already blacklisted.");}
			    		else {SQLconnector.update("insert into lazyjavie.roleblacklist (rolename) values (" + args[1].toLowerCase() + ")");}
			    		
			    	} catch (Exception e) {
			    		P.print("\n[shop] Error encountered: " + e);
			    		e.printStackTrace();
			    	}
			    }
				
				//[BUY] A successful attempt at purchasing.
				if (args[1].equalsIgnoreCase("buy") && args.length > 2) {
					try {
					    for (Role r : roles) {
					    	//TODO FIX THIS CONDITION.
					        if (args[2].equalsIgnoreCase(r.getName()) && !blacklist.contains(args[2].toLowerCase())) {
								EmbedBuilder purchaseComplete = new EmbedBuilder();
								purchaseComplete.setColor(0xD82D42);
								purchaseComplete.addField("You have purchased the role: ", "" + r.getName() + "", true);
								event.getChannel().sendMessage(purchaseComplete.build()).queue();
								
								Member member = event.getMember();
								Role role = event.getGuild().getRoleById(r.getId());
								
								event.getGuild().addRoleToMember(member, role).queue();;
								event.getGuild().modifyMemberRoles(member, role).queue();;
					        }
					    } 
				    } catch (Exception e) {P.print("Error encountered: " + e); e.printStackTrace();}
				} else if(args.length == 2 && args[1] == "buy") {
					//For when a member attempts to buy a role but doesn't enter a name.
					EmbedBuilder buyRole = new EmbedBuilder();
					buyRole.setColor(0xffae00);
					buyRole.setTitle("You didn't enter a role to buy");
					buyRole.setDescription("To purchase a role, type '>>shop buy [role]'" + "\r\n" + "");
					buyRole.addField("List of Available Roles:", "" + displayRoles , true);
					event.getChannel().sendMessage(buyRole.build()).queue();
				}
			}
		}
	}
}