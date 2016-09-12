package de.domisum.lib.sermones;

import de.domisum.lib.auxilium.AuxiliumLib;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.hologrammenu.HologramMenuLib;
import de.domisum.lib.sermones.conversation.ConversationManager;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

@APIUsage
public class SermonesLib
{

	// REFERENCES
	private static SermonesLib instance;
	private Plugin plugin;

	private ConversationManager conversationManager;


	// -------
	// CONSTRUCTOR
	// -------
	private SermonesLib(Plugin plugin)
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

		new SermonesLib(plugin);
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
		AuxiliumLib.enable(this.plugin);
		HologramMenuLib.enable(this.plugin);

		this.conversationManager = new ConversationManager();
		this.conversationManager.initialize();

		getLogger().info(this.getClass().getSimpleName()+" has been enabled");
	}

	private void onDisable()
	{
		this.conversationManager.terminate();

		HologramMenuLib.disable();
		AuxiliumLib.disable();

		getLogger().info(this.getClass().getSimpleName()+" has been disabled");
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public static SermonesLib getInstance()
	{
		if(instance == null)
			throw new IllegalArgumentException(SermonesLib.class.getSimpleName()+" has to be initialized before usage");

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
