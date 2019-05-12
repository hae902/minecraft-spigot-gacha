package com.github.hae902.gacha.Utility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Notification {
	public static void playSoundForPlayer(Player player, Sound sound, float volume, float pitch) {
		player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
	}
	public static void playSoundForPlayer(Sound sound, float volume, float pitch) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
		}
	}

	public static void message(Player player, String message) {
		player.sendMessage(message);
		playSoundForPlayer(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
	}
	public static void message(String message, Sound sound) {
		Bukkit.broadcastMessage(message);
		playSoundForPlayer(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
	}
	public static void message(Player player, String message, Sound sound, float volume, float pitch) {
		player.sendMessage(message);
		playSoundForPlayer(player, sound, volume, pitch);
	}
	public static void message(String message, Sound sound, float volume, float pitch) {
		Bukkit.broadcastMessage(message);
		playSoundForPlayer(sound, volume, pitch);
	}

	public static ChatColor systemColor = ChatColor.YELLOW;

	public static void  systemMessage(Player player, String message) {
		message(player, "[ｼｽﾃﾑ] " + systemColor + message);
	}
	public static void  systemMessage(Player player, String message, Sound sound, float volume, float pitch) {
		message(player, "[ｼｽﾃﾑ] " + systemColor + message, sound, volume, pitch);
	}

	public static void systemMessageError(Player player, String message) {
		systemMessage(player, message, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
	}
}