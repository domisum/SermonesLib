package de.domisum.lib.sermones.conversation;

import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.SetByDeserialization;

public abstract class ConversationComponent
{

	// PROPERTIES
	@SetByDeserialization
	protected String id;

	// REFERENCES
	protected transient Conversation conversation;


	// -------
	// CONSTRUCTOR
	// -------
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

	}


	// -------
	// GETTERS
	// -------
	public abstract String getId();

	public Conversation getConversation()
	{
		return this.conversation;
	}


	// -------
	// PROCESS
	// -------
	public abstract void update();

	public void startComponent(String id)
	{
		this.conversation.initializeComponent(id);
	}

}
