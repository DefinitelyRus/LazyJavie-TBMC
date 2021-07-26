package commands;

import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TextChannelCreated extends ListenerAdapter {
	public void onTextChannelCreate (TextChannelCreateEvent event) {
		P.print("new channel " + event.getChannel().getName());
	}
}
