package de.domisum.lib.sermones.conversation.component.script;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.sermones.conversation.Conversation;
import de.domisum.lib.sermones.conversation.ConversationComponent;
import org.bukkit.Bukkit;

@API
public class ConsoleCommandComponent extends ConversationComponent
{

	// PROPERTIES
	protected String command;
	protected String successorId;


	// INIT
	@DeserializationNoArgsConstructor public ConsoleCommandComponent()
	{
		super();
	}

	@API public ConsoleCommandComponent(String command, String successorId)
	{
		this.command = command;
		this.successorId = successorId;
	}


	@Override public ConversationComponent clone()
	{
		return new ConsoleCommandComponent(this.command, this.successorId);
	}

	@Override public void initialize(Conversation conversation)
	{
		super.initialize(conversation);

		executeCommand();
		startComponent(this.successorId);
	}


	// GETTERS
	@Override public String getId()
	{
		return null;
	}


	// UPDATING
	@Override public void update()
	{
		// nothing to update
	}


	// COMMAND
	protected void executeCommand()
	{
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), this.command);
	}

}
