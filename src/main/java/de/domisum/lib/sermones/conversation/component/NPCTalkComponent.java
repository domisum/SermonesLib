package de.domisum.lib.sermones.conversation.component;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.TextUtil;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.SetByDeserialization;
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
	static final double SIDEWARDS_OFFSET = 2.5;
	static final double LINE_DISTANCE = 0.25;
	private static final int MAX_LINE_LENGTH = 30;
	private static final int NUMBER_OF_LINES = 3;

	// PROPERTIES
	@SetByDeserialization protected String text;

	@SetByDeserialization protected String successorId;

	// REFERENCES
	transient List<String> lines;
	transient List<TextHologram> holograms = new ArrayList<>();

	// STATUS
	transient int updatesToWait = 0;

	transient int currentLine = 0;
	private transient int currentWord = 0;
	private transient int hologramLineOffset = 0;


	// INIT
	@DeserializationNoArgsConstructor public NPCTalkComponent()
	{
		super();
	}

	@APIUsage public NPCTalkComponent(String id, String text, String successorId)
	{
		super(id);
		this.text = text;
		this.successorId = successorId;
	}

	@Override public NPCTalkComponent clone()
	{
		return new NPCTalkComponent(this.id, this.text, this.successorId);
	}

	@Override public void initialize(Conversation conversation)
	{
		super.initialize(conversation);

		this.lines = TextUtil.splitTextIntoLinesConsideringNewLines(this.text, MAX_LINE_LENGTH);
	}

	@Override public void terminate()
	{
		super.terminate();

		for(Hologram hg : this.holograms)
			hg.hideFrom(this.conversation.getPlayer());
		this.holograms.clear();

		this.currentLine = 0;
		this.currentWord = 0;
		this.hologramLineOffset = 0;
	}


	// GETTERS
	@Override public String getId()
	{
		return this.id;
	}

	private TextHologram getHologramForLine(int index)
	{
		int adjustedIndex = index-this.hologramLineOffset;

		if(adjustedIndex < 0)
			return null;

		if(adjustedIndex >= this.lines.size())
			return null;

		return this.holograms.get(adjustedIndex);
	}


	// UPDATING
	@Override public void update()
	{
		updateHologramLocations();
		updateText();
	}

	protected void updateHologramLocations()
	{
		Location offsetLocation = this.conversation.getOffsetLocation(SIDEWARDS_OFFSET);

		double yOffset = this.holograms.size()*LINE_DISTANCE/2d;
		for(TextHologram h : this.holograms)
		{
			h.setLocation(new Vector3D(offsetLocation).add(new Vector3D(0, yOffset, 0)));

			yOffset -= LINE_DISTANCE;
		}
	}

	private void finish()
	{
		if(this.successorId != null)
			startComponent(this.successorId);
		else
			this.conversation.terminate();
	}


	// TEXT
	private void updateText()
	{
		if(this.updatesToWait > 0)
		{
			this.updatesToWait--;
			return;
		}

		addWord();
	}

	protected void addWord()
	{
		// if all lines have been written, finish this component
		if(this.currentLine >= this.lines.size())
		{
			if(this.holograms.size() == 0)
				finish();
			else
			{
				this.holograms.remove(0).hideFrom(this.conversation.getPlayer());
				this.updatesToWait += 5;
			}

			return;
		}

		// determine the current line to write and the part of the line that should be displayed
		String line = this.lines.get(this.currentLine);
		String[] splitLine = line.split("\\s+");

		String displayLine = recombineFirstWords(splitLine, this.currentWord+1);
		// displayLine += ChatColor.MAGIC+line.substring(displayLine.length());
		displayLine = Conversation.fillUpText(displayLine, line.length());

		// create new hologram or update text for existing one
		if(this.currentWord == 0)
		{
			createNewHologram(displayLine);

			if(this.holograms.size() > NUMBER_OF_LINES)
				removeHologram();
		}
		else
			getHologramForLine(this.currentLine).setText(displayLine);

		// calculate waiting time for next word
		String word = splitLine[this.currentWord];
		this.updatesToWait = (int) Math.round(word.length()*0.4)+3;
		if(word.endsWith(".") || word.endsWith("?") || word.endsWith("!"))
			this.updatesToWait += 20;
		else if(word.endsWith(",") || word.endsWith(":"))
			this.updatesToWait += 7;

		// check if end of line is reached, if so, goto next line
		this.currentWord++;
		if(this.currentWord >= splitLine.length)
		{
			this.currentLine++;
			this.currentWord = 0;
		}

		// waiting at the end of the speech
		if(this.currentLine >= this.lines.size())
			this.updatesToWait += 20;
	}

	private void createNewHologram(String text)
	{
		TextHologram lineHologram = new TextHologram(this.conversation.getBaseLocation().getWorld(), text);
		this.holograms.add(lineHologram);
		updateHologramLocations();

		lineHologram.showTo(this.conversation.getPlayer());
	}

	private void removeHologram()
	{
		this.holograms.remove(0).hideFrom(this.conversation.getPlayer());
		this.hologramLineOffset++;
	}


	// UTIL
	private String recombineFirstWords(String[] words, int numberOfWords)
	{
		String string = "";
		for(int i = 0; i < words.length && i < numberOfWords; i++)
			string += words[i]+" ";

		return string.trim();
	}

}
