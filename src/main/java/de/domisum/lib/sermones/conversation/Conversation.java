package de.domisum.lib.sermones.conversation;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.TextUtil;
import de.domisum.lib.auxilium.util.bukkit.LocationUtil;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.SetByDeserialization;
import de.domisum.lib.auxilium.util.math.VectorUtil;
import org.bukkit.ChatColor;
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

	public Location getOffsetLocation(double offsetDistance)
	{
		Location centerLocation = getBaseLocation();
		Vector3D centerLocationVector = new Vector3D(centerLocation);
		Vector3D playerLocationVector = new Vector3D(this.player.getLocation());

		Location lookLocation = LocationUtil.lookAt(this.player.getLocation(), centerLocation);
		float lookYaw = lookLocation.getYaw();

		float rotationYaw = -lookYaw;

		Vector3D distanceVector = playerLocationVector.subtract(centerLocationVector);
		double distance = distanceVector.xzLength();
		if(distance <= offsetDistance)
			distance = offsetDistance+0.1;
		float additionalRotation = (float) Math.toDegrees(Math.asin(offsetDistance/1.5/distance));
		rotationYaw -= additionalRotation;

		Vector3D offset = new Vector3D(offsetDistance, 0, 0);
		offset = VectorUtil.convertOffsetToMinecraftCoordinates(offset);
		Vector3D rotatedOffset = VectorUtil.rotateOnXZPlane(offset, rotationYaw);
		Vector3D orthogonalPosition = centerLocationVector.add(rotatedOffset);

		return orthogonalPosition.toLocation(centerLocation.getWorld());
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


	// -------
	// UTIL
	// -------
	public static String fillUpText(String text, int desiredLength)
	{
		double currentLength = ChatColor.stripColor(text).length();
		text += TextUtil.repeat(" ", (int) Math.round((desiredLength-currentLength)*1.15));

		return text;
	}

}
