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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CustomItem implements Listener{
	NBT nbt = new NBT();
	String itemId = "CustomItemId";
	public static enum CUSTOMITEMID {
		NULL("エラー", Material.STONE),//IDが埋め込まれてない時はこっちに流れる。
		ANGELSWING(ChatColor.WHITE + "天使の翼", Material.FEATHER),
		;
		private final String name;
		private final Material type;
		private CUSTOMITEMID(final String name, final Material type) {
			this.name = name;
			this.type = type;
		}
		public String getName() {
			return this.name;
		}
		public Material getType() {
			return this.type;
		}
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
		vel.setY(2.5);
		player.setVelocity(vel);
	}

	/**特定のアイテムを持っていたら落下ダメージを軽減*/
	@EventHandler
	void landingDamage(EntityDamageEvent event) {
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (event.getCause() != DamageCause.FALL) return;

		Player player = (Player) event.getEntity();
		Inventory inventory = player.getInventory();
		HashMap<Integer, ?> items = inventory.all(CUSTOMITEMID.ANGELSWING.getType());
		HashMap<Integer, ItemStack> targetItems = new HashMap<Integer, ItemStack>();
		int targetItemsCount = 0;//インベントリ内にある全ての落下ダメ軽減アイテムの個数
		final int ABATEMENT = 20;//アイテム一個あたりの軽減数（ダメージ）
		int requiredCount = (int) Math.ceil(event.getDamage() / ABATEMENT);//アイテムの必要数、落下する高さによって個数を変えてる
		//デバッグ
		/*player.sendMessage(String.valueOf(player.getHealth()));
		player.sendMessage(String.valueOf(event.getDamage()));*/

		//落下ダメージで死ぬようだったり、5ダメージ以上じゃなかったらダメ軽減しない（おせっかい防止）
		if (!(player.getHealth() - event.getDamage() <= 0 || event.getDamage() >= 5)) return;

		//該当アイテムをの場所と個数をゲット
		for (Map.Entry<Integer, ?> item : items.entrySet())  {
			ItemStack itemStack = (ItemStack)item.getValue();
			int key = item.getKey();
			if (CUSTOMITEMID.ANGELSWING.ordinal() != nbt.getNBTInt(itemStack, itemId)) continue;
			targetItemsCount += itemStack.getAmount();
			targetItems.put(key, itemStack);
		}

		int currentConsumption = requiredCount;//今必要な個数
		//必要個数分、inv内のアイテムを消費させる。
		for (Map.Entry<Integer, ?> item : targetItems.entrySet()) {
			int key = item.getKey();
			int amount = inventory.getItem(key).getAmount();//アイテムの個数

			if (amount - currentConsumption >= 0) {
				//必要個数が足りる時
				if (amount - currentConsumption == 0) {
					inventory.setItem(key, null);
				}else {
					inventory.getItem(key).setAmount(amount - currentConsumption);
				}
				currentConsumption = 0;
				break;
			}else {
				//足りない時
				inventory.setItem(key, null);
				//足りなかったらアイテムの個数分、必要個数を引いて次のloopへ・・・
				currentConsumption -= amount;
			}

		}

		//通知＆ダメージ
		String itemName = CUSTOMITEMID.ANGELSWING.getName();
		if (currentConsumption > 0) {
			//inv内のアイテムを全て消費しても足りなかった場合
			player.damage(event.getDamage() - (targetItemsCount * ABATEMENT));
			player.sendMessage(ChatColor.RED + "[注意] " + itemName + ChatColor.RED + "が足りなくなりました");
		}if ((targetItemsCount - requiredCount) == 0) {
			//アイテム消費後、予備がない場合
			player.sendMessage(ChatColor.YELLOW + "[注意] " + itemName + ChatColor.YELLOW + " が無くなりました");
		}else if ((targetItemsCount - requiredCount) > 0){
			//アイテム消費後、予備がある場合
			player.sendMessage(itemName + ChatColor.WHITE + " は、残り" + String.valueOf(targetItemsCount - requiredCount) + " 個です");
		}
		event.setCancelled(true);//ダメージ処理はこっちでしたので、キャンセル
	}
}
