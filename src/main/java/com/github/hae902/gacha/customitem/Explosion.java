package com.github.hae902.gacha.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hae902.gacha.Main;
import com.github.hae902.gacha.Utility.Notification;

public class Explosion extends CustomItem {
	Player player;
	int interval = 10;
	int second = 20 / interval;
	int exp = 3;
	@Override
	public void run() {
		if (count == second * 1) {
			Notification.message(ChatColor.YELLOW + "爆発まで...", Sound.ENTITY_PLAYER_LEVELUP, 1, 0.95f);
		}else if (exp <= 0) {
			player.getWorld().createExplosion(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ(), 4, false, false);
			Notification.playSoundForPlayer(player, Sound.ENTITY_GENERIC_EXPLODE, 10, 1f);
			this.cancel();
			return;
		}else if (count >= second * 2){
			String message = null;
			if (exp == 3) {
				message = ChatColor.YELLOW + String.valueOf(exp) + "...！";
			}else if (exp == 2) {
				message = ChatColor.GOLD + String.valueOf(exp) + "...！！";
			}else if (exp == 1) {
				message = ChatColor.RED + String.valueOf(exp) + "...！！！";
			}
			Notification.message(message, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,  3f, 1.3f);
			exp--;
		}
		count++;
	}

	public void use(Player player, ItemStack item) {
		this.player = player;
		runTaskTimer(Main.getPlugin(), 0, interval);
		Notification.message(ChatColor.RED + "" + ChatColor.BOLD + player.getDisplayName() + "が 自爆スイッチを押した！", Sound.BLOCK_DISPENSER_DISPENSE, 1, 1.3f);
		decrementItem(item, 1);
	}
}