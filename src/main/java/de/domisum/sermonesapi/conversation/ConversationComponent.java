package de.domisum.sermonesapi.conversation;

public interface ConversationComponent
{

	// -------
	// CONSTRUCTOR
	// -------
	ConversationComponent clone();

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
