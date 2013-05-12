package net.somepixels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

	private BukkitOfflineMessagePlugin plugin = null;

	public LoginListener(BukkitOfflineMessagePlugin plugin) {

		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {

		final Player player = event.getPlayer();

		// Wait before sending offline messages
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {

				// Try to get messages
				ConfigurationSection configurationSection = plugin.getConfig()
						.getConfigurationSection("messages");

				if (configurationSection != null) {

					Iterator<Map.Entry<String, Object>> it = configurationSection
							.getValues(false).entrySet().iterator();

					// Read each message
					List<String> messages = new ArrayList<String>();

					while (it.hasNext()) {

						String message = it.next().getValue().toString();

						if (message.length() > 0) {
							messages.add(ChatColor.GOLD + message);
						}
					}

					// Send messages, when any
					if (messages.size() > 0) {

						messages.add(0, "");
						messages.add(1, ChatColor.GREEN + "Offline messages:");
						player.sendMessage(messages.toArray(new String[messages
								.size()]));
					}
				}

			}
		}, 60L);

	}

}
