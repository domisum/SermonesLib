package de.domisum.lib.sermones.conversation.component.choice;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.math.MathUtil;
import de.domisum.lib.auxiliumspigot.data.container.VectorConverter;
import de.domisum.lib.hologram.hologram.TextHologram;
import de.domisum.lib.sermones.conversation.Conversation;
import de.domisum.lib.sermones.conversation.ConversationComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@APIUsage
public class ChoiceComponent extends ConversationComponent
{

	// CONSTANTS
	private static final double SIDEWARDS_OFFSET = 2.5;

	static final double LINE_DISTANCE = 0.35;
	static final double SYMBOL_OFFSET = 0.3;

	static final int LINE_FILL_LENGTH = 15;

	// PROPERTIES
	List<Choice> choices = new ArrayList<>();

	private int timeoutMs;
	private String timeoutComponentId;

	// STATUS
	private TextHologram timeoutDisplay;
	private ChoiceHologramMenu menu;

	private long startTime;


	// INIT
	@DeserializationNoArgsConstructor public ChoiceComponent()
	{
		super();
	}

	@APIUsage public ChoiceComponent(String id, List<Choice> choices)
	{
		super(id);

		this.choices = choices;
	}

	@Override public ChoiceComponent clone()
	{
		ChoiceComponent choiceComponent = new ChoiceComponent(this.id, this.choices);
		choiceComponent.setTimeout(this.timeoutMs, this.timeoutComponentId);

		return choiceComponent;
	}

	@Override public void initialize(Conversation conversation)
	{
		super.initialize(conversation);

		this.startTime = System.currentTimeMillis();
		createMenu();
	}

	@Override public void terminate()
	{
		super.terminate();

		if(this.menu != null)
			this.menu.terminate();

		if(this.timeoutDisplay != null)
		{
			this.timeoutDisplay.hideFrom(this.conversation.getPlayer());
			this.timeoutDisplay = null;
		}

		// reset
		this.startTime = 0;
	}


	// GETTERS
	@Override public String getId()
	{
		return this.id;
	}

	public boolean hasTimeout()
	{
		return this.timeoutMs != 0;
	}


	// SETTERS
	@APIUsage public ChoiceComponent setTimeout(int timeoutMs, String timeoutComponentId)
	{
		this.timeoutMs = timeoutMs;
		this.timeoutComponentId = timeoutComponentId;

		return this;
	}


	// UPDATING
	@Override public void update()
	{
		this.menu.setLocation(this.conversation.getOffsetLocation(SIDEWARDS_OFFSET));

		if(hasTimeout())
			if(getTimeoutMsLeft() <= 0)
			{
				startComponent(this.timeoutComponentId);
				return;
			}

		if(this.timeoutDisplay != null)
		{
			this.timeoutDisplay.setText(getTimeoutString());
			this.timeoutDisplay.setLocation(VectorConverter.toVector3D(getTimeoutDisplayLocation()));
		}
	}


	// MENU
	private void createMenu()
	{
		this.menu = new ChoiceHologramMenu(this.conversation.getPlayer(), this.conversation.getOffsetLocation(SIDEWARDS_OFFSET),
				this);

		if(hasTimeout())
		{
			this.timeoutDisplay = new TextHologram(getTimeoutDisplayLocation(), getTimeoutString());
			this.timeoutDisplay.showTo(this.conversation.getPlayer());
		}
	}


	// TIMEOUT
	private long getTimeoutMsLeft()
	{
		return this.timeoutMs-(System.currentTimeMillis()-this.startTime);
	}

	private Location getTimeoutDisplayLocation()
	{
		Location location = this.conversation.getOffsetLocation(SIDEWARDS_OFFSET);
		location.add(0, this.menu.getYOffset()+((this.choices.size()/2d)+1)*LINE_DISTANCE, 0);

		return location;
	}

	private String getTimeoutString()
	{
		double secondsLeft = MathUtil.round(getTimeoutMsLeft()/1000d, 1);
		String display = ChatColor.DARK_RED.toString()+secondsLeft;

		return display;
	}

}
