package de.domisum.lib.sermones.conversation.component;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxiliumspigot.data.container.VectorConverter;
import de.domisum.lib.hologram.hologram.TextHologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;

@API
public class PlayerTalkComponent extends NPCTalkComponent
{

	// REFERENCES
	private TextHologram playerSpeechIndicator;

	// INIT
	@DeserializationNoArgsConstructor
	public PlayerTalkComponent()
	{
	}

	@API
	public PlayerTalkComponent(String id, String text, String successorId)
	{
		super(id, text, successorId);
	}

	@Override
	public PlayerTalkComponent clone()
	{
		return new PlayerTalkComponent(id, text, successorId);
	}

	@Override
	public void terminate()
	{
		super.terminate();

		if(playerSpeechIndicator != null)
		{
			playerSpeechIndicator.hideFrom(conversation.getPlayer());
			playerSpeechIndicator = null;
		}
	}


	// UPDATING
	@Override
	protected void updateHologramLocations()
	{
		super.updateHologramLocations();

		if(playerSpeechIndicator == null)
			return;

		Location offsetLocation = conversation.getOffsetLocation(SIDEWARDS_OFFSET);
		double offsetY = (holograms.size()/2d+1)*LINE_DISTANCE;
		playerSpeechIndicator.setLocation(VectorConverter.toVector3D(offsetLocation).add(new Vector3D(0, offsetY, 0)));
	}

	@Override
	protected void addWord()
	{
		if(currentLine >= lines.size() && playerSpeechIndicator != null)
		{
			playerSpeechIndicator.hideFrom(conversation.getPlayer());
			playerSpeechIndicator = null;

			updatesToWait += 5;
			return;
		}

		if(currentLine == 0 && playerSpeechIndicator == null)
		{
			playerSpeechIndicator = new TextHologram(conversation.getBaseLocation().getWorld(),
					ChatColor.GOLD.toString()+ChatColor.BOLD+"You:"
			);
			updateHologramLocations();
			playerSpeechIndicator.showTo(conversation.getPlayer());

			updatesToWait += 10;
			return;
		}

		super.addWord();
	}

}
