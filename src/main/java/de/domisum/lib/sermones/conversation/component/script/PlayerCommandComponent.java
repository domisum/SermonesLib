package de.domisum.lib.sermones.conversation.component.script;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.sermones.conversation.ConversationComponent;
import org.bukkit.Bukkit;

@API
public class PlayerCommandComponent extends ConsoleCommandComponent
{

	// INIT
	@DeserializationNoArgsConstructor
	public PlayerCommandComponent()
	{

	}

	@API
	public PlayerCommandComponent(String command, String successorId)
	{
		super(command, successorId);
	}


	@Override
	public ConversationComponent clone()
	{
		return new PlayerCommandComponent(command, successorId);
	}


	// COMMAND
	@Override
	protected void executeCommand()
	{
		Bukkit.dispatchCommand(conversation.getPlayer(), command);
	}

}
