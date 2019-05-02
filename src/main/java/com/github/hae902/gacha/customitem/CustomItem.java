package com.github.hae902.gacha.customitem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.hae902.gacha.NBT;

public class CustomItem {
	NBT nbt = new NBT();
	String itemID = new CustomItemCalling().itemNBTName;
	public void use (Player player) {
		player.sendMessage(ChatColor.RED + "ERROR!!： " + ChatColor.WHITE + "このアイテムは使えません。");
	}
}
