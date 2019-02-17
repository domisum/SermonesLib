package de.domisum.lib.sermones.conversation.component.choice;

import de.domisum.lib.auxilium.util.java.annotations.API;
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

@API
public class ChoiceComponent extends ConversationComponent
{

	// CONSTANTS
	private static final double SIDEWARDS_OFFSET = 2.5;

	protected static final double LINE_DISTANCE = 0.35;
	protected static final double SYMBOL_OFFSET = 0.3;

	protected static final int LINE_FILL_LENGTH = 15;

	// PROPERTIES
	protected List<Choice> choices = new ArrayList<>();

	private int timeoutMs;
	private String timeoutComponentId;

	// STATUS
	private TextHologram timeoutDisplay;
	private ChoiceHologramMenu menu;

	private long startTime;


	// INIT
	@DeserializationNoArgsConstructor
	public ChoiceComponent()
	{

	}

	@API
	public ChoiceComponent(String id, List<Choice> choices)
	{
		super(id);

		this.choices = choices;
	}

	@Override
	public ChoiceComponent clone()
	{
		ChoiceComponent choiceComponent = new ChoiceComponent(id, choices);
		choiceComponent.setTimeout(timeoutMs, timeoutComponentId);

		return choiceComponent;
	}

	@Override
	public void initialize(Conversation conversation)
	{
		super.initialize(conversation);

		startTime = System.currentTimeMillis();
		createMenu();
	}

	@Override
	public void terminate()
	{
		super.terminate();

		if(menu != null)
			menu.terminate();

		if(timeoutDisplay != null)
		{
			timeoutDisplay.hideFrom(conversation.getPlayer());
			timeoutDisplay = null;
		}

		// reset
		startTime = 0;
	}


	// GETTERS
	@Override
	public String getId()
	{
		return id;
	}

	@API
	public boolean hasTimeout()
	{
		return timeoutMs != 0;
	}


	// SETTERS
	@API
	public ChoiceComponent setTimeout(int timeoutMs, String timeoutComponentId)
	{
		this.timeoutMs = timeoutMs;
		this.timeoutComponentId = timeoutComponentId;

		return this;
	}


	// UPDATING
	@Override
	public void update()
	{
		menu.setLocation(conversation.getOffsetLocation(SIDEWARDS_OFFSET));

		if(hasTimeout() && getTimeoutMsLeft() <= 0)
		{
			startComponent(timeoutComponentId);
			return;
		}

		if(timeoutDisplay != null)
		{
			timeoutDisplay.setText(getTimeoutString());
			timeoutDisplay.setLocation(VectorConverter.toVector3D(getTimeoutDisplayLocation()));
		}
	}


	// MENU
	private void createMenu()
	{
		menu = new ChoiceHologramMenu(conversation.getPlayer(), conversation.getOffsetLocation(SIDEWARDS_OFFSET), this);

		if(hasTimeout())
		{
			timeoutDisplay = new TextHologram(getTimeoutDisplayLocation(), getTimeoutString());
			timeoutDisplay.showTo(conversation.getPlayer());
		}
	}


	// TIMEOUT
	private long getTimeoutMsLeft()
	{
		return timeoutMs-(System.currentTimeMillis()-startTime);
	}

	private Location getTimeoutDisplayLocation()
	{
		Location location = conversation.getOffsetLocation(SIDEWARDS_OFFSET);
		location.add(0, menu.getYOffset()+((choices.size()/2d)+1)*LINE_DISTANCE, 0);

		return location;
	}

	private String getTimeoutString()
	{
		double secondsLeft = MathUtil.round(getTimeoutMsLeft()/1000d, 1);
		String display = ChatColor.DARK_RED.toString()+secondsLeft;

		return display;
	}

}
