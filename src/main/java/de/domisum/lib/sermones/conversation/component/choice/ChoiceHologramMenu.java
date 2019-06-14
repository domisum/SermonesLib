package de.domisum.lib.sermones.conversation.component.choice;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.hologram.hologram.TextHologram;
import de.domisum.lib.hologram.hologram.item.ItemDropHologram;
import de.domisum.lib.hologrammenu.component.HologramMenuComponent;
import de.domisum.lib.hologrammenu.menu.LocationBoundHologramMenu;
import de.domisum.lib.sermones.conversation.Conversation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class ChoiceHologramMenu extends LocationBoundHologramMenu
{

	// REFERENCES
	private final ChoiceComponent choiceComponent;


	// INIT
	protected ChoiceHologramMenu(Player player, Location location, ChoiceComponent choiceComponent)
	{
		super(player, location);
		this.choiceComponent = choiceComponent;
		setLocation(location); // this is needed so the yOffset is applied from the start

		addComponents();

		done();
	}

	private void addComponents()
	{
		double dY = (ChoiceComponent.LINE_DISTANCE*choiceComponent.choices.size())/2d;

		for(Choice choice : choiceComponent.choices)
		{
			TextHologram thg = new TextHologram(processText(choice.getText()));
			HologramMenuComponent<TextHologram> hmc = new HologramMenuComponent<TextHologram>(thg)
			{
				@Override
				public void onHover()
				{
					hologram.setText(ChatColor.AQUA+processText(choice.getText()));
				}

				@Override
				public void onDehover()
				{
					hologram.setText(processText(choice.getText()));
				}

				@Override
				public void onClick()
				{
					if(choice.getSuccesorId() != null)
						choiceComponent.startComponent(choice.getSuccesorId());
					else
						choiceComponent.getConversation().terminate();
				}
			};
			components.put(hmc, new Vector3D(0, dY, 0));

			addSymbol(thg, dY, -1, choice.getSymbolLeft());
			addSymbol(thg, dY, 1, choice.getSymbolRight());

			dY -= ChoiceComponent.LINE_DISTANCE;
		}
	}

	private void addSymbol(TextHologram textHologram, double dY, int direction, ItemStack symbol)
	{
		if(symbol == null)
			return;

		ItemDropHologram ihg = new ItemDropHologram(symbol);
		HologramMenuComponent<ItemDropHologram> hmc = new HologramMenuComponent<>(ihg);
		components.put(hmc, new Vector3D(((textHologram.getWidth()/2)+ChoiceComponent.SYMBOL_OFFSET)*direction, dY, 0));
	}


	// GETTERS
	protected double getYOffset()
	{
		// move upwards if number of choices > 3
		return Math.max(0, choiceComponent.choices.size()-3)*ChoiceComponent.LINE_DISTANCE;
	}


	// SETTERS
	@Override
	public void setLocation(Location location)
	{
		super.setLocation(location.clone().add(0, getYOffset(), 0));
	}


	// UTIL
	private static String processText(String text)
	{
		return Conversation.fillUpText(text, ChoiceComponent.LINE_FILL_LENGTH);
	}

}
