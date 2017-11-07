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
	@DeserializationNoArgsConstructor public PlayerTalkComponent()
	{
		super();
	}

	@API public PlayerTalkComponent(String id, String text, String successorId)
	{
		super(id, text, successorId);
	}

	@Override public PlayerTalkComponent clone()
	{
		return new PlayerTalkComponent(this.id, this.text, this.successorId);
	}

	@Override public void terminate()
	{
		super.terminate();

		if(this.playerSpeechIndicator != null)
		{
			this.playerSpeechIndicator.hideFrom(this.conversation.getPlayer());
			this.playerSpeechIndicator = null;
		}
	}


	// UPDATING
	@Override protected void updateHologramLocations()
	{
		super.updateHologramLocations();

		if(this.playerSpeechIndicator == null)
			return;

		Location offsetLocation = this.conversation.getOffsetLocation(SIDEWARDS_OFFSET);
		double offsetY = (this.holograms.size()/2d+1)*LINE_DISTANCE;
		this.playerSpeechIndicator.setLocation(VectorConverter.toVector3D(offsetLocation).add(new Vector3D(0, offsetY, 0)));
	}

	@Override protected void addWord()
	{
		if(this.currentLine >= this.lines.size() && this.playerSpeechIndicator != null)
		{
			this.playerSpeechIndicator.hideFrom(this.conversation.getPlayer());
			this.playerSpeechIndicator = null;

			this.updatesToWait += 5;
			return;
		}

		if(this.currentLine == 0 && this.playerSpeechIndicator == null)
		{
			this.playerSpeechIndicator = new TextHologram(this.conversation.getBaseLocation().getWorld(),
					ChatColor.GOLD.toString()+ChatColor.BOLD+"You:");
			updateHologramLocations();
			this.playerSpeechIndicator.showTo(this.conversation.getPlayer());

			this.updatesToWait += 10;
			return;
		}

		super.addWord();
	}

}
