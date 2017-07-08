package de.domisum.lib.sermones.conversation.component.script;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.sermones.conversation.ConversationComponent;
import org.bukkit.Bukkit;

@APIUsage
public class PlayerCommandComponent extends ConsoleCommandComponent
{

	// INIT
	@DeserializationNoArgsConstructor public PlayerCommandComponent()
	{
		super();
	}

	@APIUsage public PlayerCommandComponent(String command, String successorId)
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
