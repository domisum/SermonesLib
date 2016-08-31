package de.domisum.sermonesapi.conversation.component;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.sermonesapi.conversation.Conversation;
import de.domisum.sermonesapi.conversation.ConversationComponent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@APIUsage
public class ChoiceComponent extends ConversationComponent
{

	// CONSTANTS
	private static final double SIDEWARDS_OFFSET = 2.5;

	// PROPERTIES
	List<Choice> choices = new ArrayList<>();

	// STATUS
	private ChoiceHologramMenu menu;


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

	@Override
	public void terminate()
	{
		super.terminate();

		if(this.menu != null)
			this.menu.terminate();
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
		private ItemStack symbolLeft;
		private ItemStack symbolRight;

		private String succesorId;


		// -------
		// CONSTRUCTOR
		// -------
		@DeserializationNoArgsConstructor
		public Choice()
		{

		}

		@APIUsage
		public Choice(String text, String succesorId)
		{
			this.text = text;
			this.succesorId = succesorId;
		}


		// -------
		// GETTERS
		// -------
		String getText()
		{
			return this.text;
		}

		String getSuccesorId()
		{
			return this.succesorId;
		}

		ItemStack getSymbolLeft()
		{
			return this.symbolLeft;
		}

		ItemStack getSymbolRight()
		{
			return this.symbolRight;
		}


		// -------
		// SETTERS
		// -------
		@APIUsage
		public Choice setSymbolLeft(ItemStack symbolLeft)
		{
			this.symbolLeft = symbolLeft;
			return this;
		}

		@APIUsage
		public Choice setSymbolRight(ItemStack symbolRight)
		{
			this.symbolRight = symbolRight;
			return this;
		}

	}

}
