package de.domisum.lib.sermones.conversation;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxiliumspigot.data.structure.pds.PlayerKeyMap;
import de.domisum.lib.sermones.SermonesLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.Map;

public class ConversationManager
{

	// REFERENCES
	private BukkitTask updateTask;

	// STATUS
	private final Map<Player, Conversation> conversations = new PlayerKeyMap<>();


	// INIT
	public void initialize()
	{
		startUpdateTask();
	}

	public void terminate()
	{
		stopUpdateTask();

		for(Conversation c : conversations.values())
			c.terminate();
	}


	// CHANGERS
	@API
	public void startConversation(Conversation conversation, Player player, Location location)
	{
		if(conversations.containsKey(player))
			conversations.get(player).terminate();

		conversations.put(player, conversation);
		conversation.initialize(player, location);
	}


	// UPDATING
	private void startUpdateTask()
	{
		if(updateTask != null)
			return;

		updateTask = Bukkit.getScheduler().runTaskTimer(SermonesLib.getPlugin(), this::update, 1, 1);
	}

	private void stopUpdateTask()
	{
		if(updateTask == null)
			return;

		updateTask.cancel();
		updateTask = null;
	}

	private void update()
	{
		Iterator<Conversation> iterator = conversations.values().iterator();
		while(iterator.hasNext())
		{
			Conversation conversation = iterator.next();
			if(conversation.isTerminated())
			{
				iterator.remove();
				continue;
			}

			conversation.update();
		}
	}

}
