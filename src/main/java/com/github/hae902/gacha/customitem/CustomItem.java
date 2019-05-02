package com.github.hae902.gacha.customitem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.hae902.gacha.NBT;

public class CustomItem extends BukkitRunnable{
	NBT nbt = new NBT();
	String itemID = new CustomItemCalling().itemNBTName;
	//Timer
	int count = 0;

	/**指定した数だけアイテムを減らす*/
	public void decrementItem(Player player, int count) {
		PlayerInventory inv = player.getInventory();
		ItemStack item = inv.getItemInMainHand();
		int amount = item.getAmount();
		if (amount - count < 0) {
			inv.setItemInMainHand(null);
		}else {
			item.setAmount(amount - count);
		}
	}
	public void decrementItem(Player player, int key, int count) {
		Inventory inv = player.getInventory();
		ItemStack item = inv.getItem(key);
		int amount = item.getAmount();
		if (amount - count < 0) {
			inv.setItem(key, null);
		}else {
			item.setAmount(amount - count);
		}
	}

	public void use (Player player) {
		player.sendMessage(ChatColor.RED + "ERROR!!： " + ChatColor.WHITE + "このアイテムは使えません。");
	}
	@Override
	public void run() {

	}
}
