package com.github.hae902.gacha.customitem;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.github.hae902.gacha.Main;

public class GodsEye extends CustomItem implements Listener {
	Player player;
	int count = 5;
	@Override
	public void run() {
		switch (count) {
		case 5:
			player.sendMessage(ChatColor.YELLOW + "神と契約し全てを見る力を入手した...。");
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1, 1f);
			break;
		case 4:
			player.sendMessage(ChatColor.YELLOW + "契約終了まで...。");
			player.getWorld().playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 0.8f);
			break;
		case 3:
		case 2:
		case 1:
			player.sendMessage(ChatColor.YELLOW + String.valueOf(count) + "...");
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
			break;
		case 0:
			player.setGameMode(GameMode.SURVIVAL);
			Main.deathMessage = player.getDisplayName() + " は 知りすぎてしまった...";
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.8f);
			player.setHealth(0);
			cancel();
			break;
		default:
			break;
		}
		count--;
	}
	@Override
	public void use(Player player, ItemStack item) {
		this.player = player;
		player.setGameMode(GameMode.SPECTATOR);
		runTaskTimer(Main.getPlugin(), 0, 20);
		decrementItem(item, 1);
	}
}
