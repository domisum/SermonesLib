package de.domisum.sermonesapi.conversation;

public abstract class ConversationComponent
{

	// REFERENCES
	protected transient Conversation conversation;


	// -------
	// CONSTRUCTOR
	// -------
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
