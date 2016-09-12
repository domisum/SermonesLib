package de.domisum.lib.sermones.conversation.component.script;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.sermones.conversation.Conversation;
import de.domisum.lib.sermones.conversation.ConversationComponent;
import org.bukkit.Bukkit;

@APIUsage
public class ConsoleCommandComponent extends ConversationComponent
{

	// PROPERTIES
	String command;
	String successorId;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public ConsoleCommandComponent()
	{
		super();
	}

	@APIUsage
	public ConsoleCommandComponent(String command, String successorId)
	{
		this.command = command;
		this.successorId = successorId;
	}


	@Override
	public ConversationComponent clone()
	{
		return new ConsoleCommandComponent(this.command, this.successorId);
	}

	@Override
	public void initialize(Conversation conversation)
	{
		super.initialize(conversation);

		executeCommand();
		startComponent(this.successorId);
	}


	// -------
	// GETTERS
	// -------
	@Override
	public String getId()
	{
		return null;
	}


	// -------
	// UPDATING
	// -------
	@Override
	public void update()
	{

	}


	// -------
	// COMMAND
	// -------
	void executeCommand()
	{
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), this.command);
	}

}
