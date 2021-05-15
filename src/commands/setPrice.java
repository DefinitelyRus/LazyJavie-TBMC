package commands;

import java.sql.SQLException;
import java.util.List;

import javax.security.auth.login.LoginException;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class setPrice extends ListenerAdapter{
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		String[] args = event.getMessage().getContentRaw().split("\\s+");

		String requestby = event.getMember().getUser().getName();
		List<Role> roles = event.getGuild().getRoles();
		
		if(args.length == 0 && args[0].equalsIgnoreCase(LazyJavie.prefix + "setprice")) {
			EmbedBuilder setPrice1 = new EmbedBuilder();
			setPrice1.setColor(0x77B255);
			setPrice1.setTitle("To set a price for a role:");
			setPrice1.setDescription(LazyJavie.prefix + "setprice [role] [price]");
			setPrice1.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
			event.getChannel().sendMessage(setPrice1.build()).queue();
		}

		
		for (Role r : roles) {
			//actual start of the set price role commands
			if (event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
				//TODO fix this (might break due to some roles having spaces):
				//actual setting price
				if (args[0].equalsIgnoreCase(LazyJavie.prefix + "setprice") && args[1].equals(r.getName()) && args[2].length() > 0) {				
					try {
						P.print("setprice cmd detected(FINAL);");
						SQLconnector.update("UPDATE `lazyjavie`.`sellroles` SET `rolePrice` = '" + args[2] +"' WHERE (`roleName` = '"+ args[1] +"');\r\n");
						
						EmbedBuilder setPrice3 = new EmbedBuilder();
						setPrice3.setColor(0x77B255);
						setPrice3.setDescription("Price set for `"+ args[1] + "`: `"+ args[2] + " points`");
						setPrice3.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
						event.getChannel().sendMessage(setPrice3.build()).queue();
	    
					} catch (LoginException e) {
						P.print("[setprice] Failed to execute setprice");
						e.printStackTrace();
					} catch (SQLException e) {
						P.print("[setprice] Failed to execute setprice");
						e.printStackTrace();
					}
				}
			}
		}

	} 
}
