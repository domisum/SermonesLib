package de.domisum.sermonesapi.conversation;

import de.domisum.auxiliumapi.data.container.math.Vector3D;
import de.domisum.auxiliumapi.util.bukkit.LocationUtil;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;
import de.domisum.auxiliumapi.util.math.VectorUtil;
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

	@Override
	public Conversation clone()
	{
		List<ConversationComponent> componentsClone = new ArrayList<>();
		for(ConversationComponent component : this.components)
			componentsClone.add(component.clone());

		return new Conversation(this.startComponentId, componentsClone);
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

		if(this.activeComponent != null)
			this.activeComponent.terminate();
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

	public Location getOffsetLocation(double distance)
	{
		Location centerLocation = getBaseLocation();

		Location lookLocation = LocationUtil.lookAt(this.player.getLocation(), centerLocation);
		float lookYaw = lookLocation.getYaw();

		Vector3D offset = new Vector3D(distance, 0, 0);
		offset = VectorUtil.convertOffsetToMinecraftCoordinates(offset);
		Vector3D rotatedOffset = VectorUtil.rotateOnXZPlane(offset, -lookYaw);

		return centerLocation.add(rotatedOffset.x, rotatedOffset.y, rotatedOffset.z);
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
	// SETTERS
	// -------
	@APIUsage
	public void setBaseLocation(Location baseLocation)
	{
		this.baseLocation = baseLocation;
	}


	// -------
	// UPDATING
	// -------
	void update()
	{
		if(this.activeComponent == null)
		{
			terminate();
			return;
		}

		this.activeComponent.update();
	}

	void initializeComponent(String id)
	{
		if(this.activeComponent != null)
			this.activeComponent.terminate();

		this.activeComponent = getComponent(id);
		if(this.activeComponent == null)
			throw new IllegalArgumentException("The conversation component with the id '"+id+"' does not exist!");
		else
			this.activeComponent.initialize(this);
	}

}