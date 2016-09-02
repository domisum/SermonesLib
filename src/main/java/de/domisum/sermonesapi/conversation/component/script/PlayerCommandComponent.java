package de.domisum.sermonesapi.conversation.component.script;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.sermonesapi.conversation.ConversationComponent;
import org.bukkit.Bukkit;

@APIUsage
public class PlayerCommandComponent extends ConsoleCommandComponent
{

	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public PlayerCommandComponent()
	{
		super();
	}

	@APIUsage
	public PlayerCommandComponent(String command, String successorId)
	{
		super(command, successorId);
	}


	@Override
	public ConversationComponent clone()
	{
		return new PlayerCommandComponent(this.command, this.successorId);
	}


	// -------
	// COMMAND
	// -------
	@Override
	void executeCommand()
	{
		Bukkit.dispatchCommand(this.conversation.getPlayer(), this.command);
	}

}
