package commands;



import java.util.List;

import bot_init.LazyJavie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class shop extends ListenerAdapter {
	

	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
				
		String[] args = event.getMessage().getContentRaw().split("\\s+");	
		String requestby = event.getMember().getUser().getName();
		List<Role> roles = event.getGuild().getRoles();
		
	    StringBuilder displayRoles = new StringBuilder();
	    for (Role r : roles) {
	    	// Filters out role with:
	    	if(r.getColorRaw() == 0x696969) {
	    	} else if (r.getColorRaw() == 105) {
	    	} else if (r.getName() == "@everyone" || r.getName() == "everyone" || r.getPosition() == 0) {
	    	}
	    	else {
	    	    // Role gets appended in the stringbuilder (sb)
		    	displayRoles.append(r.getName()).append("\n");
	    	}	
	    }
		

		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "shop")) {
				// Usage
			   if (roles.isEmpty()) {
					EmbedBuilder shopRoles = new EmbedBuilder();
					shopRoles.setColor(0xffae00);
					shopRoles.setTitle("There are no roles in this guild!");
					event.getChannel().sendMessage(shopRoles.build()).queue();
					return;
				    }
			//>>shop [display role]
			if (args.length <= 1){
				EmbedBuilder shop = new EmbedBuilder();
				shop.setColor(0xffae00);
				shop.setTitle("💰 Welcome to the LazyJavie shop! 💰");
				shop.setDescription("To purchase a role, type '>>shop buy [role]'" + "\r\n" + "");
			    event.getGuild().getRolesByName("bots", true);
				shop.addField("List of Available Roles:",
						"" + displayRoles , true);
	     		shop.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(shop.build()).queue();
			}
			
			//>>shop buy (empty) [display roles]
			if(args.length == 2) {
				EmbedBuilder buyRole = new EmbedBuilder();
				buyRole.setColor(0xffae00);
				buyRole.setTitle("You didn't enter a role to buy");
				buyRole.setDescription("To purchase a role, type '>>shop buy [role]'" + "\r\n" + "");
				buyRole.addField("List of Available Roles:",
						"" + displayRoles , true);
				event.getChannel().sendMessage(buyRole.build()).queue();
		}
			//>>shop buy (role)
			try {
		    for (Role r : roles) {
		    	
		        if (args[2].equalsIgnoreCase(r.getName())) {
					EmbedBuilder purchaseComplete = new EmbedBuilder();
					purchaseComplete.setColor(0xD82D42);
					purchaseComplete.addField("You have purchased the role: ", "" + r.getName() + "", true);
					event.getChannel().sendMessage(purchaseComplete.build()).queue();
					//TODO: GIVE ROLE
					//making member and the role an object so I can use addRoleToMember
					Member member = event.getMember();
					Role role = event.getGuild().getRoleById(r.getId());
					event.getGuild().addRoleToMember(member, role).queue();;
					event.getGuild().modifyMemberRoles(member, role).queue();;
		
		        }
		    } 
		}	catch (Exception e) {
	    	P.print("Giving role: ERROR");
	    }

	}
  }
}