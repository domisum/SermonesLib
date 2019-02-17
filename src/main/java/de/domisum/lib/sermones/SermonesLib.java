package de.domisum.lib.sermones;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.hologrammenu.HologramMenuLib;
import de.domisum.lib.sermones.conversation.ConversationManager;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

@API
public class SermonesLib
{

	// REFERENCES
	private static SermonesLib instance;
	private final Plugin plugin;

	private ConversationManager conversationManager;


	// INIT
	private SermonesLib(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@API
	public static void enable(Plugin plugin)
	{
		if(instance != null)
			return;

		instance = new SermonesLib(plugin);
		instance.onEnable();
	}

	@API
	public static void disable()
	{
		if(instance == null)
			return;

		getInstance().onDisable();
		instance = null;
	}


	private void onEnable()
	{
		HologramMenuLib.enable(plugin);

		conversationManager = new ConversationManager();
		conversationManager.initialize();

		getLogger().info(getClass().getSimpleName()+" has been enabled");
	}

	private void onDisable()
	{
		conversationManager.terminate();

		HologramMenuLib.disable();

		getLogger().info(getClass().getSimpleName()+" has been disabled");
	}


	// GETTERS
	@API
	public static SermonesLib getInstance()
	{
		if(instance == null)
			throw new IllegalArgumentException(SermonesLib.class.getSimpleName()+" has to be initialized before usage");

		return instance;
	}

	@API
	public static Plugin getPlugin()
	{
		return getInstance().plugin;
	}

	@API
	public Logger getLogger()
	{
		return getInstance().plugin.getLogger();
	}


	public static ConversationManager getConversationManager()
	{
		return getInstance().conversationManager;
	}

}
