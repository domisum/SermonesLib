package de.domisum.lib.sermones.conversation;

import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.InitByDeserialization;

public abstract class ConversationComponent
{

	// PROPERTIES
	@InitByDeserialization
	protected String id;

	// REFERENCES
	protected transient Conversation conversation;


	// INIT
	@DeserializationNoArgsConstructor
	public ConversationComponent()
	{

	}

	protected ConversationComponent(String id)
	{
		this.id = id;
	}


	@Override
	public abstract ConversationComponent clone();

	public void initialize(Conversation conversation)
	{
		this.conversation = conversation;
	}

	public void terminate()
	{
		// nothing to terminate
	}


	// GETTERS
	public abstract String getId();

	public Conversation getConversation()
	{
		return this.conversation;
	}


	// PROCESS
	public abstract void update();

	public void startComponent(String id)
	{
		this.conversation.initializeComponent(id);
	}

}
