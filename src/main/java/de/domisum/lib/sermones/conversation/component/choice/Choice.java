package de.domisum.lib.sermones.conversation.component.choice;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import org.bukkit.inventory.ItemStack;

@APIUsage
public class Choice
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
