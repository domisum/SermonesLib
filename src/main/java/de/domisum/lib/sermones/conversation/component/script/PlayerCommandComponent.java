package de.domisum.lib.sermones.conversation.component.script;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.sermones.conversation.ConversationComponent;
import org.bukkit.Bukkit;

@API
public class PlayerCommandComponent extends ConsoleCommandComponent
{

	// INIT
	@DeserializationNoArgsConstructor public PlayerCommandComponent()
	{
		super();
	}

	@API public PlayerCommandComponent(String command, String successorId)
	{
		super(command, successorId);
	}


	@Override public ConversationComponent clone()
	{
		return new PlayerCommandComponent(this.command, this.successorId);
	}


	// COMMAND
	@Override protected void executeCommand()
	{
		Bukkit.dispatchCommand(this.conversation.getPlayer(), this.command);
	}

}
