package commands;

import java.util.List;
import bot_init.LazyJavie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class shop extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
				
		String[] args = event.getMessage().getContentRaw().split("\\s+");	
		String requestby = event.getMember().getUser().getName();
		List<Role> roles = event.getGuild().getRoles();
		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "shop")) {
			if (args.length < 2) {
				
				EmbedBuilder shop = new EmbedBuilder();
				shop.setColor(0xffae00);
				shop.setTitle("💲 Welcome to the LazyJavie shop ! 💲");
				shop.setDescription("To purchase a role, type '>>shop buy [role]'" + "\r\n" + "");
			    StringBuilder sb = new StringBuilder();
			    event.getGuild().getRolesByName("bots", true);

			    for (Role r : roles) {
			    	// Filters out role with:
			    	if(r.getColorRaw() == 0x696969) {
			    		System.out.println("Bot Found: not appending");
			    	} else if (r.getColorRaw() == 105) {
			    		System.out.println("Bot Found: not appending");
			    	} else if (r.getName() == "@everyone" || r.getName() == "everyone") {
			    		System.out.println("Bot Found: not appending");
			    	}
			    	else {
			    	    // Role gets appended in the stringbuilder (sb)
				    	sb.append(r.getName()).append("\n");
			    	}	
			    }
			    
				shop.addField("List of Available Roles:", "" + sb , true);
				
				   if (roles.isEmpty()) {
						EmbedBuilder shopRoles = new EmbedBuilder();
						shopRoles.setColor(0xffae00);
						shopRoles.setTitle("There are no roles in this guild!");
						event.getChannel().sendMessage(shopRoles.build()).queue();
						return;
					    }
				
	     		shop.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(shop.build()).queue();
			}
		}
		
		for (Role r : roles) {
	        if (args[0].equalsIgnoreCase(LazyJavie.prefix + "shop") && (args[1].equalsIgnoreCase("buy")) && r.equals(args[2].equals(roles))) {
				EmbedBuilder purchaseComplete = new EmbedBuilder();
				purchaseComplete.setColor(0xD82D42);
				purchaseComplete.setTitle("You have purchased the role " + r);
				event.getChannel().sendMessage(purchaseComplete.build()).queue();
	        }
	    }
	}
}