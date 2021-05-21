/*
 * ---------------!!! ADD TO README !!!---------------
 * This is where all return commands go. Commands that only returns a message and does nothing else.
 * 
 * Available commands are as follows:
 * -	$bottoken
 * 		Rick rolls you.
 * 
 * -	$ping
 * 		Returns the latency from the server to the bot's host.
 * 
 * -	$test
 * 		Confirms that the bot is still connected. 
 * 
 * -	$points
 * 		Returns how many points a member has accumulated.
 * 
 * -	$help
 * 		Returns a list of available commands, their syntax, and description.
 */
package commands;

import java.sql.SQLException;
import java.util.LinkedList;
import javax.security.auth.login.LoginException;
import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Returns extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		//Initialization
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		String requestby = null;
		if (args[0].startsWith(LazyJavie.prefix)) {requestby = event.getMember().getUser().getName();}
		
		//[BOT TOKEN] Returns the bot's token... not really.----------------------------------------------------
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "bottoken")) {
			P.print("\n[Returns] Requesting bot token: " + event.getMember().getUser().getName());
			event.getChannel().sendMessage("Bot token: ||Never gonna give you up~ Never gonna let you down~||").queue();
		}
		
		//[PING] Returns the latency.---------------------------------------------------------------------------
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "ping")) {
			P.print("\n[Returns] Requesting ping: " + event.getMember().getUser().getName());
			long ping = event.getJDA().getGatewayPing();
			P.print("Latency gathered.");
			
			//Embed block
			P.print("Ping: " + ping + "ms");
			EmbedBuilder pingEmbed = new EmbedBuilder();
			pingEmbed.setColor(0x77B255);
			pingEmbed.setDescription("Pong: **" +ping+ "ms**");
			pingEmbed.setFooter("Requested by " + requestby , event.getMember(	).getUser().getAvatarUrl());
			event.getChannel().sendMessage(pingEmbed.build()).queue();
		}
		
		//[TEST] Just returns a confirmation message to see if the bot works.-----------------------------------
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "test")) {
			//Embed block
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
			P.print("\n[Returns] Requesting help list: " + event.getMember().getUser().getName());
			
			//Initialization
			String memberName = event.getMember().getUser().getName();
			boolean isAdmin = event.getMember().hasPermission(Permission.ADMINISTRATOR);
			LinkedList<String> cmdlist = null;
			LinkedList<String> dsclist = null;
			LinkedList<String> foradminlist = null;
			String[] cmdarray = {};
			String[] dscarray = {};
			String[] foradminarray = {};
			String output = "";
			
			try {
				P.print("Getting lists from database...");
				cmdlist = SQLconnector.getList("select * from lazyjavie.helplist", "cmd", true);
				dsclist = SQLconnector.getList("select * from lazyjavie.helplist", "dsc", true);
				foradminlist = SQLconnector.getList("select * from lazyjavie.helplist", "adminonly", true);
				
				P.print("Converting to arrays...");
				cmdlist.toArray(cmdarray);
				dsclist.toArray(dscarray);
				foradminlist.toArray(foradminarray);
			}
	    	catch (LoginException e) {P.print("Error encountered: " + e.toString()); return;}
			catch (SQLException e) {P.print("Error encountered: " + e.toString()); return;}
			catch (Exception e) {P.print("Error encountered: " + e.toString()); e.printStackTrace(); return;}
			
			//<HELP: MISSING INFO>
			P.print("Checking for missing info...");
			if (cmdarray.length != dscarray.length) {
				//TODO Send error message
				P.print("Error raised: Missing commands or descriptions.");
				P.print("CMDs: " + cmdarray.length + "; DSCs: " + dscarray.length);
				return;
			
			//<HELP: SUCCESS>
			} else {
				P.print("Listing available commands...");
				int cmdcount = cmdlist.size();
				P.print("# of available commands: " + cmdcount);
				P.print(String.join(", ", cmdlist));
				for (int i = 1; i < cmdcount; i++) {
					int foradmin = Integer.parseInt(foradminlist.get(i));
					if (isAdmin == true && foradmin == 1) {
						P.print("|Added " + cmdlist.get(i) + ".");
						output = output + "\n\n> [ADMIN] " + LazyJavie.prefix + cmdlist.get(i) + " :\n" + dsclist.get(i);
					}
					else if (isAdmin == false && foradmin == 1) {
						P.print("|Skipped " + cmdlist.get(i) + ".");
						continue;
						}
					else {
						P.print("|Added " + cmdlist.get(i) + ".");
						output = output + "\n\n> " + LazyJavie.prefix + cmdlist.get(i) + " :\n" + dsclist.get(i);
					}
				}
				output = output + "```";
				P.print("Finalizing output...");
				//P.print(output);
				//Embed block
				EmbedBuilder help = new EmbedBuilder();
				help.setColor(0xffae00);
				//TODO Put all commands in a list, then use a FOR loop to append each command to this output message.
				//Ps. put the list in another class or right above the BOT TOKEN code block.
				help.addField("     Prefix: "+ LazyJavie.prefix +"\nCurrent commands: ", output, true);
				help.setFooter("Requested by " + memberName , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(help.build()).queue();
				P.print("Done!");
				return;
			}
		}
	}
}
