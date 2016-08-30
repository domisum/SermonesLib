package de.domisum.sermonesapi.conversation;

import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;

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


	// -------
	// PROCESS
	// -------
	public abstract void update();

	protected void startComponent(String id)
	{
		this.conversation.initializeComponent(id);
	}

}
