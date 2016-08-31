package de.domisum.sermonesapi.conversation.component.choice;

import de.domisum.auxiliumapi.data.container.math.Vector3D;
import de.domisum.hmapi.component.HologramMenuComponent;
import de.domisum.hmapi.menu.LocationBoundHologramMenu;
import de.domisum.hologramapi.hologram.TextHologram;
import de.domisum.hologramapi.hologram.item.ItemHologram;
import de.domisum.sermonesapi.conversation.Conversation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class ChoiceHologramMenu extends LocationBoundHologramMenu
{
	// CONSTANT
	private static final double Y_OFFSET = 0.0;
	private static final double LINE_DISTANCE = 0.35;
	private static final double SYMBOL_OFFSET = 0.3;

	private static final int LINE_FILL_LENGTH = 15;

	// REFERENCES
	private ChoiceComponent choiceComponent;


	// -------
	// CONSTRUCTOR
	// -------
	ChoiceHologramMenu(Player player, Location location, ChoiceComponent choiceComponent)
	{
		super(player, location.clone().add(0, Y_OFFSET, 0));
		this.choiceComponent = choiceComponent;

		addComponents();

		done();
	}

	private void addComponents()
	{
		double dY = LINE_DISTANCE*this.choiceComponent.choices.size()/2d;

		for(Choice choice : this.choiceComponent.choices)
		{
			TextHologram thg = new TextHologram(processText(choice.getText()));
			HologramMenuComponent hmc = new HologramMenuComponent(thg)
			{
				@Override
				public void onHover()
				{
					((TextHologram) this.hologram).setText(ChatColor.AQUA+processText(choice.getText()));
				}

				@Override
				public void onDehover()
				{
					((TextHologram) this.hologram).setText(processText(choice.getText()));
				}

				@Override
				public void onClick()
				{
					if(choice.getSuccesorId() != null)
						ChoiceHologramMenu.this.choiceComponent.startComponent(choice.getSuccesorId());
					else
						ChoiceHologramMenu.this.choiceComponent.getConversation().terminate();
				}
			};
			this.components.put(hmc, new Vector3D(0, dY, 0));

			addSymbol(thg, dY, -1, choice.getSymbolLeft());
			addSymbol(thg, dY, 1, choice.getSymbolRight());

			dY -= LINE_DISTANCE;
		}
	}

	private void addSymbol(TextHologram textHologram, double dY, int direction, ItemStack symbol)
	{
		if(symbol == null)
			return;

		ItemHologram ihg = new ItemHologram(symbol);
		HologramMenuComponent hmc = new HologramMenuComponent(ihg);
		this.components.put(hmc, new Vector3D((textHologram.getWidth()/2+SYMBOL_OFFSET)*direction, dY, 0));
	}

	@Override
	public void setLocation(Location location)
	{
		super.setLocation(location.clone().add(0, Y_OFFSET, 0));
	}


	// -------
	// UTIL
	// -------
	private static String processText(String text)
	{
		text = Conversation.fillUpText(text, LINE_FILL_LENGTH);
		return text;
	}

}
