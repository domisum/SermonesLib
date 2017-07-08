package de.domisum.lib.sermones.conversation.component.choice;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.hologram.hologram.TextHologram;
import de.domisum.lib.hologram.hologram.item.ItemHologram;
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
	private ChoiceComponent choiceComponent;


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
		double dY = ChoiceComponent.LINE_DISTANCE*this.choiceComponent.choices.size()/2d;

		for(Choice choice : this.choiceComponent.choices)
		{
			TextHologram thg = new TextHologram(processText(choice.getText()));
			HologramMenuComponent hmc = new HologramMenuComponent(thg)
			{
				@Override public void onHover()
				{
					((TextHologram) this.hologram).setText(ChatColor.AQUA+processText(choice.getText()));
				}

				@Override public void onDehover()
				{
					((TextHologram) this.hologram).setText(processText(choice.getText()));
				}

				@Override public void onClick()
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

			dY -= ChoiceComponent.LINE_DISTANCE;
		}
	}

	private void addSymbol(TextHologram textHologram, double dY, int direction, ItemStack symbol)
	{
		if(symbol == null)
			return;

		ItemHologram ihg = new ItemHologram(symbol);
		HologramMenuComponent hmc = new HologramMenuComponent(ihg);
		this.components.put(hmc, new Vector3D((textHologram.getWidth()/2+ChoiceComponent.SYMBOL_OFFSET)*direction, dY, 0));
	}


	// GETTERS
	protected double getYOffset()
	{
		// move upwards if number of choices > 3
		return Math.max(0, this.choiceComponent.choices.size()-3)*ChoiceComponent.LINE_DISTANCE;
	}


	// SETTERS
	@Override public void setLocation(Location location)
	{
		super.setLocation(location.clone().add(0, getYOffset(), 0));
	}


	// UTIL
	private static String processText(String text)
	{
		text = Conversation.fillUpText(text, ChoiceComponent.LINE_FILL_LENGTH);
		return text;
	}

}
