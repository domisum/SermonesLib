package de.domisum.lib.sermones.conversation;

import de.domisum.lib.auxilium.data.structure.pds.PlayerKeyMap;
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
	private Map<Player, Conversation> conversations = new PlayerKeyMap<>();


	// INIT
	public ConversationManager()
	{

	}

	public void initialize()
	{
		startUpdateTask();
	}

	public void terminate()
	{
		stopUpdateTask();

		for(Conversation c : this.conversations.values())
			c.terminate();
	}


	// CHANGERS
	public void startConversation(Conversation conversation, Player player, Location location)
	{
		if(this.conversations.containsKey(player))
			this.conversations.get(player).terminate();

		this.conversations.put(player, conversation);
		conversation.initialize(player, location);
	}


	// UPDATING
	private void startUpdateTask()
	{
		if(this.updateTask != null)
			return;

		this.updateTask = Bukkit.getScheduler().runTaskTimer(SermonesLib.getPlugin(), this::update, 1, 1);
	}

	private void stopUpdateTask()
	{
		if(this.updateTask == null)
			return;

		this.updateTask.cancel();
		this.updateTask = null;
	}

	private void update()
	{
		Iterator<Conversation> iterator = this.conversations.values().iterator();
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
