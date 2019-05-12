package com.github.hae902.gacha.customitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.hae902.gacha.Utility.Notification;

public class Warp extends CustomItem implements Listener {
	private static int slot = (int) Math.ceil((double) Bukkit.getOnlinePlayers().size() / 9);
	private static Inventory inv = Bukkit.createInventory(null, slot * 9, "プレイヤーを選択");

	void openGUI(Player player) {
		int i = 0;
		inv.clear();
		for (Player allplayer : Bukkit.getOnlinePlayers()) {
			ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
			meta.setDisplayName(ChatColor.RESET + allplayer.getDisplayName() + ChatColor.YELLOW + " にテレポート");
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(allplayer.getUniqueId()));
			itemStack.setItemMeta(meta);
			inv.setItem(i, itemStack);
			i++;
		}
		player.openInventory(inv);
	}

	@Override
	public void use(Player player, ItemStack item) {
		decrementItem(item, 1);
		openGUI(player);
	}
	void result(Player player, boolean result) {
	}

	@EventHandler
	void click(InventoryClickEvent event) {
		Player startPlayer = (Player)event.getWhoClicked();
		if (event.getInventory().equals(inv)) {
			ItemStack currentItem = event.getCurrentItem();
			if (currentItem == null || currentItem.getType() != (Material.PLAYER_HEAD)) return;

			SkullMeta meta = (SkullMeta)currentItem.getItemMeta();
			Player goalPlayer = meta.getOwningPlayer().getPlayer();
			if (startPlayer == goalPlayer) {
				Notification.systemMessageError(startPlayer, "正気ですか？");
				result(startPlayer, false);
			}else {
				if (!meta.getOwningPlayer().isOnline()) {
					Notification.systemMessageError(startPlayer, "現在オフラインです。");
					result(startPlayer, false);
				}else {
					startPlayer.teleport(goalPlayer);
					Notification.systemMessage(startPlayer, goalPlayer.getDisplayName() + " に テレポートしました");
					Notification.systemMessage(goalPlayer, startPlayer.getDisplayName() + " が テレポートしてきました");
					result(startPlayer, true);
				}
			}
			event.setCancelled(true);
			startPlayer.closeInventory();
		}
	}
}