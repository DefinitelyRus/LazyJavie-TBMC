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
		P.print("setprice cmd detected;");

		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "setprice")) {
			P.print("setprice cmd detected;");
			for (Role r : roles) {
				//actual start of the set price role commands
				if (r.hasPermission(Permission.ADMINISTRATOR) || r.hasPermission(Permission.MANAGE_PERMISSIONS)) {
					if(args[0].equalsIgnoreCase(LazyJavie.prefix + "setprice")) {
						EmbedBuilder setPrice = new EmbedBuilder();
						setPrice.setColor(0xffae00);
						setPrice.setTitle("To set a price for a role:");
						setPrice.setDescription(LazyJavie.prefix + "setprice [role] [price]");
						setPrice.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
						event.getChannel().sendMessage(setPrice.build()).queue();
					}
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "setprice") && args[1].equalsIgnoreCase(r.getName())) {
			EmbedBuilder setPrice = new EmbedBuilder();
			setPrice.setColor(0xffae00);
			setPrice.setTitle("To set a price for a role:");
			setPrice.setDescription(LazyJavie.prefix + "setprice [role] [price]");
		    setPrice.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
		    event.getChannel().sendMessage(setPrice.build()).queue();
		}
		//TODO fix this (might break due to some roles having spaces):
			else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "setprice") && args[1].equals(r.getName()) && args.length >= 2) {
				try {
					P.print("setprice cmd detected(UPDATE);");
					SQLconnector.update("UPDATE `lazyjavie`.`sellroles` SET `rolePrice` = '" + args[2] + "' WHERE (roleName = '" + args[1] + " '); ");
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
}
