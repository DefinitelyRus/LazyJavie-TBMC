/*
 * 
 */
package commands;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Returns extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		//[BOT TOKEN] Returns the bot's token... not really.----------------------------------------------------
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "bottoken")) {
			event.getChannel().sendMessage("Bot token: ||Never gonna give you up~ Never gonna let you down~||").queue();
		}
		
		//[PING] Returns the latency.---------------------------------------------------------------------------
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "ping")) {
			P.print("\n[Returns] Requesting ping: " + event.getMember().getUser().getName());
			long ping = event.getJDA().getGatewayPing();
			P.print("Latency gathered.");
			event.getChannel().sendMessage(ping + "ms").queue();
			P.print("Ping: " + ping + "ms");
		}
		
		//[TEST] Just returns a confirmation message to see if the bot works.-----------------------------------
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "test")) {
			String requestby = event.getMember().getUser().getName();
			EmbedBuilder test = new EmbedBuilder();
			test.setColor(0x77B255);
			test.setTitle(":white_check_mark: Test success!");
			test.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
			event.getChannel().sendMessage(test.build()).queue();
			P.print("\n[Returns] Pong! Sender: " + event.getMember().getUser().getName());
		}
		
		//[POINTS] Displays the points of the current user.-----------------------------------------------------
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "points") || args[0].equalsIgnoreCase(LazyJavie.prefix + "point")) {
			P.print("\n[Returns] Requesting point query: " + event.getMember().getUser().getName());
			
			//Initialization
			String memberName = event.getMember().getUser().getName();
			String memberId = event.getMessage().getMember().getId();
			P.print("Getting query...");
			int pts = 0;
			
			//Checks if the member is registered.
			try {
				String x;
				x = SQLconnector.get("select points from lazyjavie.members WHERE userid=" + memberId + ";", "points", true);
				if (x == "Empty result.") {
					//TODO Turn this to embed.
					event.getChannel().sendMessage("Type `" +LazyJavie.prefix+ "register <password>` to have your points recorded.").queue();
					P.print("Request cancelled: Member not registered.");
					return;
				} else {pts = Integer.parseInt(x);}
			}
			catch (Exception e) {P.print(e.toString());}
			
			P.print("Displaying points...");
			//Embed block
			EmbedBuilder points = new EmbedBuilder();
			points.setColor(0xffae00);
			points.addField(":moneybag: Your current points: :moneybag:", "`"+pts+"`", true);
			points.setFooter("Requested by " + memberName , event.getMember().getUser().getAvatarUrl());
			event.getChannel().sendMessage(points.build()).queue();
		}
		
		//[HELP] Displays a list of available commands and their usage.-----------------------------------------
		if(args[0].equalsIgnoreCase(LazyJavie.prefix + "help")) {
			String memberName = event.getMember().getUser().getName();
			
			//Embed block
			EmbedBuilder help = new EmbedBuilder();
			help.setColor(0xffae00);
			//TODO Put all commands in a list, then use a FOR loop to append each command to this output message.
			//Ps. put the list in another class or right above the BOT TOKEN code block.
			help.addField("     Prefix: "+ LazyJavie.prefix +"\nCurrent commands: ","```register``` ```shop```  ```points``` ```unregister```  ```clear```  ```ping```  ```test```", true);
			help.setFooter("Requested by " + memberName , event.getMember().getUser().getAvatarUrl());
			event.getChannel().sendMessage(help.build()).queue();
		}
	}
}
