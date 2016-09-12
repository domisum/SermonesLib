package de.domisum.sermonesapi;

import de.domisum.auxiliumapi.AuxiliumAPI;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.lib.hologrammenu.HologramMenuLib;
import de.domisum.sermonesapi.conversation.ConversationManager;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

@APIUsage
public class SermonesAPI
{

	// REFERENCES
	private static SermonesAPI instance;
	private Plugin plugin;

	private ConversationManager conversationManager;


	// -------
	// CONSTRUCTOR
	// -------
	private SermonesAPI(Plugin plugin)
	{
		instance = this;
		this.plugin = plugin;

		onEnable();
	}

	@APIUsage
	public static void enable(Plugin plugin)
	{
		if(instance != null)
			return;

		new SermonesAPI(plugin);
	}

	@APIUsage
	public static void disable()
	{
		if(instance == null)
			return;

		getInstance().onDisable();
		instance = null;
	}


	private void onEnable()
	{
		AuxiliumAPI.enable(this.plugin);
		HologramMenuLib.enable(this.plugin);

		this.conversationManager = new ConversationManager();
		this.conversationManager.initialize();

		getLogger().info(this.getClass().getSimpleName()+" has been enabled");
	}

	private void onDisable()
	{
		this.conversationManager.terminate();

		HologramMenuLib.disable();
		AuxiliumAPI.disable();

		getLogger().info(this.getClass().getSimpleName()+" has been disabled");
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public static SermonesAPI getInstance()
	{
		if(instance == null)
			throw new IllegalArgumentException(SermonesAPI.class.getSimpleName()+" has to be initialized before usage");

		return instance;
	}

	@APIUsage
	public static Plugin getPlugin()
	{
		return getInstance().plugin;
	}

	@APIUsage
	public Logger getLogger()
	{
		return getInstance().plugin.getLogger();
	}


	public static ConversationManager getConversationManager()
	{
		return getInstance().conversationManager;
	}

}
