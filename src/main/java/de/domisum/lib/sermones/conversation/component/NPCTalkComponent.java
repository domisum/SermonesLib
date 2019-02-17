package de.domisum.lib.sermones.conversation.component;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.InitByDeserialization;
import de.domisum.lib.auxiliumspigot.data.container.VectorConverter;
import de.domisum.lib.auxiliumspigot.util.SpigotTextUtil;
import de.domisum.lib.hologram.hologram.Hologram;
import de.domisum.lib.hologram.hologram.TextHologram;
import de.domisum.lib.sermones.conversation.Conversation;
import de.domisum.lib.sermones.conversation.ConversationComponent;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class NPCTalkComponent extends ConversationComponent
{

	// CONSTANTS
	protected static final double SIDEWARDS_OFFSET = 2.5;
	protected static final double LINE_DISTANCE = 0.25;
	private static final int MAX_LINE_LENGTH = 30;
	private static final int NUMBER_OF_LINES = 3;

	// PROPERTIES
	@InitByDeserialization
	protected String text;
	@InitByDeserialization
	protected String successorId;

	// REFERENCES
	protected transient List<String> lines;
	protected transient List<TextHologram> holograms = new ArrayList<>();

	// STATUS
	protected transient int updatesToWait = 0;

	protected transient int currentLine = 0;
	private transient int currentWord = 0;
	private transient int hologramLineOffset = 0;


	// INIT
	@DeserializationNoArgsConstructor
	public NPCTalkComponent()
	{
	}

	@API
	public NPCTalkComponent(String id, String text, String successorId)
	{
		super(id);
		this.text = text;
		this.successorId = successorId;
	}

	@Override
	public NPCTalkComponent clone()
	{
		return new NPCTalkComponent(id, text, successorId);
	}

	@Override
	public void initialize(Conversation conversation)
	{
		super.initialize(conversation);

		lines = SpigotTextUtil.splitTextIntoLinesConsideringNewLines(text, MAX_LINE_LENGTH);
	}

	@Override
	public void terminate()
	{
		super.terminate();

		for(Hologram hg : holograms)
			hg.hideFrom(conversation.getPlayer());
		holograms.clear();

		currentLine = 0;
		currentWord = 0;
		hologramLineOffset = 0;
	}


	// GETTERS
	@Override
	public String getId()
	{
		return id;
	}

	private TextHologram getHologramForLine(int index)
	{
		int adjustedIndex = index-hologramLineOffset;

		if(adjustedIndex < 0)
			return null;

		if(adjustedIndex >= lines.size())
			return null;

		return holograms.get(adjustedIndex);
	}


	// UPDATING
	@Override
	public void update()
	{
		updateHologramLocations();
		updateText();
	}

	protected void updateHologramLocations()
	{
		Location offsetLocation = conversation.getOffsetLocation(SIDEWARDS_OFFSET);

		double yOffset = holograms.size()*LINE_DISTANCE/2d;
		for(TextHologram h : holograms)
		{
			h.setLocation(VectorConverter.toVector3D(offsetLocation).add(new Vector3D(0, yOffset, 0)));

			yOffset -= LINE_DISTANCE;
		}
	}

	private void finish()
	{
		if(successorId != null)
			startComponent(successorId);
		else
			conversation.terminate();
	}


	// TEXT
	private void updateText()
	{
		if(updatesToWait > 0)
		{
			updatesToWait--;
			return;
		}

		addWord();
	}

	protected void addWord()
	{
		// if all lines have been written, finish this component
		if(currentLine >= lines.size())
		{
			if(holograms.size() == 0)
				finish();
			else
			{
				holograms.remove(0).hideFrom(conversation.getPlayer());
				updatesToWait += 5;
			}

			return;
		}

		// determine the current line to write and the part of the line that should be displayed
		String line = lines.get(currentLine);
		String[] splitLine = line.split("\\s+");

		String displayLine = recombineFirstWords(splitLine, currentWord+1);
		// displayLine += ChatColor.MAGIC+line.substring(displayLine.length());
		displayLine = Conversation.fillUpText(displayLine, line.length());

		// create new hologram or update text for existing one
		if(currentWord == 0)
		{
			createNewHologram(displayLine);

			if(holograms.size() > NUMBER_OF_LINES)
				removeHologram();
		}
		else
			getHologramForLine(currentLine).setText(displayLine);

		// calculate waiting time for next word
		String word = splitLine[currentWord];
		updatesToWait = (int) Math.round(word.length()*0.4)+3;
		if(word.endsWith(".") || word.endsWith("?") || word.endsWith("!"))
			updatesToWait += 20;
		else if(word.endsWith(",") || word.endsWith(":"))
			updatesToWait += 7;

		// check if end of line is reached, if so, goto next line
		currentWord++;
		if(currentWord >= splitLine.length)
		{
			currentLine++;
			currentWord = 0;
		}

		// waiting at the end of the speech
		if(currentLine >= lines.size())
			updatesToWait += 20;
	}

	private void createNewHologram(String text)
	{
		TextHologram lineHologram = new TextHologram(conversation.getBaseLocation().getWorld(), text);
		holograms.add(lineHologram);
		updateHologramLocations();

		lineHologram.showTo(conversation.getPlayer());
	}

	private void removeHologram()
	{
		holograms.remove(0).hideFrom(conversation.getPlayer());
		hologramLineOffset++;
	}


	// UTIL
	private String recombineFirstWords(String[] words, int numberOfWords)
	{
		StringBuilder string = new StringBuilder();
		for(int i = 0; i < words.length && i < numberOfWords; i++)
			string.append(words[i]).append(" ");

		return string.toString().trim();
	}

}
