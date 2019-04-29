package com.github.hae902.gacha;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CustomItem implements Listener{
	NBT nbt = new NBT();
	String itemId = "CustomItemId";
	public static enum CUSTOMITEMID {
		NULL,//IDが埋め込まれてない時はこっちに流れる。
		ANGELSWING
	};
	@EventHandler
	void itemCalling(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		NBT nbt = new NBT();
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
		ItemStack itemMainHand = e.getItem();
		if (itemMainHand == null) return;//RIGHT_CLICK_BLOCKだとなぜか2回実行されるので、対策

		CUSTOMITEMID[] values = CUSTOMITEMID.values();
		int id = nbt.getNBTInt(itemMainHand, itemId);
		switch (values[id]) {
		case ANGELSWING:
			angelsWing(player);
			break;
		default:
			break;
		}
	}
	/**天使の翼*/
	void angelsWing(Player player) {
		if (!player.isOnGround()) return;
		Vector vel = player.getVelocity();
		vel.setY(5);
		player.setVelocity(vel);
	}

	/**特定のアイテムを持っていたら落下ダメージを無くす*/
	@EventHandler
	void landingDamage(EntityDamageEvent event) {
		if (event.getEntityType() != EntityType.PLAYER) return;
		boolean hasCancelEvent = false;//キャンセルフラグ
		Player player = (Player) event.getEntity();
		Inventory inventory = player.getInventory();
		HashMap<Integer, ?> feather = inventory.all(Material.FEATHER);
		for (Map.Entry<Integer, ?> item : feather.entrySet()) {
			ItemStack itemStack = (ItemStack)item.getValue();
			int key = item.getKey();
			if (CUSTOMITEMID.ANGELSWING.ordinal() != nbt.getNBTInt(itemStack, itemId)) continue;
			int amount = inventory.getItem(key).getAmount();
			player.sendMessage(ChatColor.BOLD + itemStack.getItemMeta().getDisplayName() + ChatColor.WHITE + " が身代わりになった...");
			if (amount == 1) {
				inventory.setItem(key, null);
				player.sendMessage(ChatColor.YELLOW + "[ 注意 ] もう身代わりになるアイテムがありません");
			}else {
				inventory.getItem(key).setAmount(amount - 1);
			}
			hasCancelEvent = true;
			break;
		}
		event.setCancelled(hasCancelEvent);
	}
}
