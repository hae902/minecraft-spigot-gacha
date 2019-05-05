package com.github.hae902.gacha.customitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.github.hae902.gacha.Main;

public class Explosion extends CustomItem {
	Player player;
	int interval = 10;
	int second = 20 / interval;
	int exp = 3;
	@Override
	public void run() {
		if (count == second * 1) {
			Bukkit.broadcastMessage(ChatColor.YELLOW + "爆発まで...");
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0.95f);
		}else if (exp <= 0) {
			player.getWorld().createExplosion(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(), 4, false, false);
			decrementItem(player, 1);
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10, 1f);
			this.cancel();
			return;
		}else if (count >= second * 2){
			if (exp == 3) {
				player.sendMessage(ChatColor.YELLOW + String.valueOf(exp) + "...！");
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,  1f, 0.8f);
			}else if (exp == 2) {
				player.sendMessage(ChatColor.GOLD + String.valueOf(exp) + "...！！");
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,  2f, 1f);
			}else if (exp == 1) {
				player.sendMessage(ChatColor.RED + String.valueOf(exp) + "...！！！");
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,  3f, 1.3f);
			}



			exp--;
		}
		count++;
	}

	public void use(Player player) {
		this.player = player;
		runTaskTimer(Main.getPlugin(), 0, interval);
		Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + player.getDisplayName() + "さん が 自爆スイッチを押した！");
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 1.3f);

	}
}