package de.domisum.sermonesapi.conversation;

import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Conversation
{

	// PROPERTIES
	@SetByDeserialization
	private String startComponentId;
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	@SetByDeserialization
	private List<ConversationComponent> components = new ArrayList<>();

	// REFERENCES
	private transient Player player;
	private transient Location baseLocation;

	// STATUS
	private transient ConversationComponent activeComponent;
	private transient boolean terminated = false;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public Conversation()
	{

	}

	public Conversation(String startComponentId, List<ConversationComponent> components)
	{
		this.startComponentId = startComponentId;
		this.components = components;
	}

	void initialize(Player player, Location baseLocation)
	{
		this.player = player;
		this.baseLocation = baseLocation;

		initializeComponent(this.startComponentId);
	}

	public void terminate()
	{
		this.terminated = true;

		this.activeComponent.terminate();

		// TODO
	}


	// -------
	// GETTERS
	// -------
	public Player getPlayer()
	{
		return this.player;
	}

	public Location getBaseLocation()
	{
		return this.baseLocation.clone();
	}


	boolean isTerminated()
	{
		return this.terminated;
	}


	private ConversationComponent getComponent(String id)
	{
		for(ConversationComponent c : this.components)
			if(id.equals(c.getId()))
				return c;

		return null;
	}


	// -------
	// UPDATING
	// -------
	void update()
	{
		this.activeComponent.update();
	}

	private void initializeComponent(String id)
	{
		this.activeComponent = getComponent(id);
		if(this.activeComponent == null)
			throw new IllegalArgumentException("The conversation component with the id '"+id+"' does not exist!");
		else
			this.activeComponent.initialize(this);
	}

}
