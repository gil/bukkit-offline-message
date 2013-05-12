package net.somepixels;

import java.security.MessageDigest;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitOfflineMessagePlugin extends JavaPlugin {

	public void onEnable() {
		new LoginListener(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (command.getName().equalsIgnoreCase("offmsg")) {

			if (sender instanceof Player) {

				storePlayerMessage((Player) sender, args);
				return true;

			} else {
				sender.sendMessage("This command can only be run by a player.");
			}
		}

		return false;
	}

	private void storePlayerMessage(Player player, String[] args) {

		String playerName = player.getName();
		String playerHash = generatePlayerHash(playerName);
		String message = StringUtils.join(args, " ").trim();
		String confirmationMessage = ChatColor.GOLD
				+ "Offline message deleted!";

		if (message.length() > 0) {
			message = "[" + playerName + "] " + message;
			confirmationMessage = ChatColor.GOLD
					+ "Offline message updated! Type /offmsg without any message to delete it.";
		}

		this.getConfig().set("messages." + playerHash, message);
		this.saveConfig();

		player.sendMessage(confirmationMessage);
	}

	private String generatePlayerHash(String playerName) {

		try {
			return new HexBinaryAdapter().marshal(MessageDigest.getInstance(
					"md5").digest(playerName.getBytes("UTF-8")));
		} catch (Exception e) {
			return playerName;
		}
	}
}
