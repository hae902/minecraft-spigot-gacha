package com.github.hae902.gacha.Utility;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Notification {
	public void playSoundForPlayer(Player player, Sound sound, float volume, float pitch) {
		player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
	}
	public void playSoundForPlayer(Sound sound, float volume, float pitch) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
		}
	}
}