package de.domisum.sermonesapi.conversation.component;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.hmapi.menu.LocationBoundHologramMenu;
import de.domisum.sermonesapi.conversation.Conversation;
import de.domisum.sermonesapi.conversation.ConversationComponent;

import java.util.ArrayList;
import java.util.List;

@APIUsage
public class ChoiceComponent extends ConversationComponent
{

	// CONSTANTS
	private static final double SIDEWARDS_OFFSET = 2;

	// PROPERTIES
	List<Choice> choices = new ArrayList<>();

	// STATUS
	private LocationBoundHologramMenu menu;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public ChoiceComponent()
	{
		super();
	}

	@APIUsage
	public ChoiceComponent(String id, List<Choice> choices)
	{
		super(id);

		this.choices = choices;
	}

	@Override
	public ChoiceComponent clone()
	{
		return new ChoiceComponent(this.id, this.choices);
	}

	@Override
	public void initialize(Conversation conversation)
	{
		super.initialize(conversation);

		createMenu();
	}


	// -------
	// GETTERS
	// -------
	@Override
	public String getId()
	{
		return this.id;
	}


	// -------
	// UPDATING
	// -------
	@Override
	public void update()
	{
		this.menu.setLocation(this.conversation.getOffsetLocation(SIDEWARDS_OFFSET));
	}


	// -------
	// MENU
	// -------
	private void createMenu()
	{
		this.menu = new ChoiceHologramMenu(this.conversation.getPlayer(), this.conversation.getOffsetLocation(SIDEWARDS_OFFSET),
				this);
	}


	// -------
	// CHOICE
	// -------
	@APIUsage
	public static class Choice
	{

		// PROPERTIES
		private String text;


		// -------
		// CONSTRUCTOR
		// -------
		@DeserializationNoArgsConstructor
		public Choice()
		{

		}

		@APIUsage
		public Choice(String text)
		{
			this.text = text;
		}


		// -------
		// GETTERS
		// -------
		public String getText()
		{
			return this.text;
		}
	}

}
