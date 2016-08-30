package de.domisum.sermonesapi.conversation.component;

import de.domisum.auxiliumapi.data.container.math.Vector3D;
import de.domisum.hmapi.component.HologramMenuComponent;
import de.domisum.hmapi.menu.LocationBoundHologramMenu;
import de.domisum.hologramapi.hologram.TextHologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class ChoiceHologramMenu extends LocationBoundHologramMenu
{
	// CONSTANT
	private static final double Y_OFFSET = 0.2;
	private static final double LINE_DISTANCE = 0.35;

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

		for(ChoiceComponent.Choice choice : this.choiceComponent.choices)
		{
			TextHologram thg = new TextHologram(choice.getText());
			HologramMenuComponent hmc = new HologramMenuComponent(thg)
			{
				@Override
				public void onHover()
				{
					((TextHologram) this.hologram).setText(ChatColor.GRAY+choice.getText());
				}

				@Override
				public void onDehover()
				{
					((TextHologram) this.hologram).setText(choice.getText());
				}

				@Override
				public void onClick()
				{
					this.player.sendMessage(choice.getText());
				}
			};
			this.components.put(hmc, new Vector3D(0, dY, 0));

			dY -= LINE_DISTANCE;
		}
	}

	@Override
	public void setLocation(Location location)
	{
		super.setLocation(location.clone().add(0, Y_OFFSET, 0));
	}
}
