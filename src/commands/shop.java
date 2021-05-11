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
		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "shop")) {
			if (args.length < 2) {
				// Usage
				EmbedBuilder shop = new EmbedBuilder();
				shop.setColor(0xffae00);
				shop.setTitle("💲 Welcome to the LazyJavie shop ! 💲");
				shop.setDescription("To purchase a role, type '>>shop buy [role]'" + "\r\n" + "");
				List<Role> roles = event.getGuild().getRoles();
			    StringBuilder sb = new StringBuilder();
			    
			    
			    for (Role r : roles) {
			      sb.append(r.getName()).append(")\n");
			    }
				shop.addField("List of Available Roles:",
						"" + sb , true);
				
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
	}
}