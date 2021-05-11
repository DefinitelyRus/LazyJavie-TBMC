package commands;



import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class pointSystem extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		

		//grabs the message and get the (discord id) of the member who sent the message
		Message message = event.getMessage();
		String memberId = message.getMember().getId();
		//gets the member's amount of points (database)
		String stringPoints = SQLconnector.get("select points from lazyjavie.members WHERE userid=" + memberId + ";", "points");	
		Integer integerPoints = Integer.parseInt(stringPoints);
		
		//update points to be added by 1 if there is a message detected
		try {
			integerPoints += 1;
			
			//updates the number of points to the new one
			SQLconnector.update("UPDATE lazyjavie.members "+ "SET points = " + integerPoints + " WHERE userid=" + memberId + ";");

		} catch (LoginException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
//		SELECT points
//		WHERE user == memberId
//		points += 1
//		
		//the shop
		String[] args = event.getMessage().getContentRaw().split("\\s+");	
		String requestby = event.getMember().getUser().getName();

		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "points") || args[0].equalsIgnoreCase(LazyJavie.prefix + "point")  ) {
			if (args.length < 2) {
				// Usage
				EmbedBuilder points = new EmbedBuilder();
				points.setColor(0xffae00);
				points.setTitle("💲 Your current points: 💲");
				points.setDescription("" + integerPoints + "");
				

				
				
				points.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(points.build()).queue();
			}
		}
	}


}