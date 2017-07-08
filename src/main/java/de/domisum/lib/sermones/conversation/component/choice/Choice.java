package de.domisum.lib.sermones.conversation.component.choice;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@APIUsage
public class Choice
{

	// PROPERTIES
	@Getter private String text;
	@Getter private ItemStack symbolLeft;
	@Getter private ItemStack symbolRight;

	@Getter private String succesorId;


	// INIT
	@DeserializationNoArgsConstructor public Choice()
	{

	}

	@APIUsage public Choice(String text, String succesorId)
	{
		this.text = text;
		this.succesorId = succesorId;
	}


	// SETTERS
	@APIUsage public Choice setSymbolLeft(ItemStack symbolLeft)
	{
		this.symbolLeft = symbolLeft;
		return this;
	}

	@APIUsage public Choice setSymbolRight(ItemStack symbolRight)
	{
		this.symbolRight = symbolRight;
		return this;
	}

}
