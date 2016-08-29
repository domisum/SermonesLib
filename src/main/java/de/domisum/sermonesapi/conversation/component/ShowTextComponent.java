package de.domisum.sermonesapi.conversation.component;

import de.domisum.auxiliumapi.data.container.math.Vector3D;
import de.domisum.auxiliumapi.util.TextUtil;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;
import de.domisum.hologramapi.hologram.Hologram;
import de.domisum.hologramapi.hologram.TextHologram;
import de.domisum.sermonesapi.conversation.Conversation;
import de.domisum.sermonesapi.conversation.ConversationComponent;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ShowTextComponent implements ConversationComponent
{

	// CONSTANTS
	private static final double LINE_DISTANCE = 0.25;
	private static final int MAX_LINE_LENGTH = 30;
	private static final int NUMBER_OF_LINES = 3;

	// PROPERTIES
	@SetByDeserialization
	private String id;
	@SetByDeserialization
	private String text;

	// REFERENCES
	private Conversation conversation;
	private transient List<TextHologram> holograms = new ArrayList<>();

	private transient List<String> lines;

	// STATUS
	private transient int updatesToWait = 0;

	private transient int currentLine = 0;
	private transient int currentWord = 0;
	private transient int hologramLineOffset = 0;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public ShowTextComponent()
	{

	}

	public ShowTextComponent(String id, String text)
	{
		this.id = id;
		this.text = text;
	}

	@Override
	public void initialize(Conversation conversation)
	{
		this.conversation = conversation;

		this.lines = TextUtil.splitTextIntoLinesConsideringNewLines(this.text, MAX_LINE_LENGTH);
	}

	@Override
	public void terminate()
	{
		for(Hologram hg : this.holograms)
			hg.hideFrom(this.conversation.getPlayer());
	}


	// -------
	// GETTERS
	// -------
	@Override
	public String getId()
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


	// -------
	// UPDATING
	// -------
	@Override
	public void update()
	{
		updateHologramLocations();
		updateText();
	}

	private void updateHologramLocations()
	{
		Location centerLocation = this.conversation.getBaseLocation();

		double offset = this.holograms.size()*LINE_DISTANCE/2d;
		for(TextHologram h : this.holograms)
		{
			h.setLocation(new Vector3D(centerLocation).add(new Vector3D(0, offset, 0)));

			offset -= LINE_DISTANCE;
		}
	}


	// -------
	// TEXT
	// -------
	private void updateText()
	{
		if(this.updatesToWait > 0)
		{
			this.updatesToWait--;
			return;
		}

		addWord();
	}

	private void addWord()
	{
		// if all lines have been written, terminate the conversation
		if(this.currentLine >= this.lines.size())
		{
			if(this.holograms.size() == 0)
				this.conversation.terminate();
			else
			{
				this.holograms.remove(0).hideFrom(this.conversation.getPlayer());
				this.updatesToWait += 10;
			}

			return;
		}

		// determine the current line to write and the part of the line that should be displayed
		String line = this.lines.get(this.currentLine);
		String[] splitLine = line.split("\\s+");

		String displayLine = recombineFirstWords(splitLine, this.currentWord+1);
		// displayLine += ChatColor.MAGIC+line.substring(displayLine.length());
		displayLine += TextUtil.repeat(" ", (int) Math.round((line.length()-displayLine.length())*1.15));

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
			this.updatesToWait = 60;
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


	private String recombineFirstWords(String[] words, int numberOfWords)
	{
		String string = "";
		for(int i = 0; i < words.length && i < numberOfWords; i++)
			string += words[i]+" ";

		return string.trim();
	}

}
