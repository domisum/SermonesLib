package de.domisum.sermonesapi.conversation;

public interface ConversationComponent
{

	// -------
	// CONSTRUCTOR
	// -------
	void initialize(Conversation conversation);

	void terminate();


	// -------
	// GETTERS
	// -------
	String getId();


	// -------
	// UPDATING
	// -------
	void update();

}
