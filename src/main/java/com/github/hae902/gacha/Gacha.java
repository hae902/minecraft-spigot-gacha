package com.github.hae902.gacha;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.hae902.gacha.Utility.Notification;
import com.github.hae902.gacha.customitem.CustomItemCalling;
import com.github.hae902.gacha.customitem.CustomItemCalling.CUSTOMITEMID;

public class Gacha {
	double[] probability = new double[] {1};
	int id;
	//item
	ItemStack item = new ItemStack(Material.DIRT);
	ItemMeta itemMeta = item.getItemMeta();
	String name;
	NBT nbt = new NBT();
	CustomItemCalling customItem = new CustomItemCalling();
	public int weightingGacha(double[] probability, Player player) {
		int result = -1;
		double random_n = Math.random();//0.0~1.0

		double totalProbability = 0;
		for (int i = 0; i < probability.length; i++) {
			totalProbability += probability[i];
		}
		random_n *= totalProbability;//0.0~totalrare_probability

		//player.sendMessage(ChatColor.GRAY + "確率：" + String.valueOf(random_n));

		//ガチャ処理
		//最初と最後以外はforで ぐるぐる。
		double probability_hurdle = probability[0];
		if (0.0 < random_n && random_n < probability_hurdle) {
			result = 0;
		}else{
			probability_hurdle = probability[0];
			for (int i = 1; i < probability.length - 1; i++) {
				if (probability_hurdle < random_n && random_n < probability_hurdle + probability[i]) {
					result = i;
					break;
				}else {
					probability_hurdle += probability[i];
				}
			}
		}
		if (result == -1) {
			result = probability.length - 1;
		}
		return result;
	}

	public void rarityGacha(Player player) {
		probability = new double[] {85, 15};
		id = weightingGacha(probability, player);
		switch (id) {
		case 0:
			gacha1(player);
			break;
		case 1:
			gacha2(player);
			break;
		default:
			player.sendMessage("まだアイテムを つくってません。");
			break;
		}
	}

	/**アイテムの名前と説明文を入れます。nullだと入れません。*/
	void setItemNameAndLore(String name, String ...lore) {
		//アイテム名変更
		String displayName = null;
		if (name != null) displayName = ChatColor.RESET + name;
		itemMeta.setDisplayName(displayName);
		//説明文変更
		if (lore[0] != null) {
			List<String> lores = new ArrayList<>();
			for(String item : lore) {
				lores.add(item);
			}
			itemMeta.setLore(lores);
		}
		item.setItemMeta(itemMeta);
	}

	/**使わないであろうエンチャを付けます。表示は隠します*/
	void addEnchant () {
		itemMeta.addEnchant(Enchantment.LURE, 1, false);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(itemMeta);
	}

	/**プレイヤーにアイテムを付与します。*/
	void giveItem (Player player, Material itemtype, int count, String name, String ...lore) {
		item = new ItemStack(itemtype, count);
		itemMeta = item.getItemMeta();
		setItemNameAndLore(name, lore);
		player.getInventory().addItem(item);
	}
	/**プレイヤーにカスタムアイテムを与える*/
	void giveCustomItem (Player player, CUSTOMITEMID customItem, int count, String ...lore) {
		this.name = customItem.getName();
		this.item = new ItemStack(customItem.getType(), count);
		this.itemMeta = item.getItemMeta();
		addEnchant();
		setItemNameAndLore(this.name, lore);
		item = nbt.setNBTInt(this.item, this.customItem.itemNBTName, customItem.ordinal());
		player.getInventory().addItem(this.item);
	}


	void gacha1(Player player) {
		probability = new double[] {1, 1};
		id = weightingGacha(probability, player);
		switch (id) {
		case 0:
			giveCustomItem(player, CUSTOMITEMID.ANGELSWING, 2, ChatColor.LIGHT_PURPLE + "アイテムを消費する代わりに", ChatColor.LIGHT_PURPLE + "落下ダメージを無効化してくれる", ChatColor.DARK_GRAY + "(右クリックで空高く跳ぶことも出来る)");
			break;
		case 1:
			giveCustomItem(player, CUSTOMITEMID.WARP, 1, ChatColor.LIGHT_PURPLE + "任意のプレイヤーにテレポートできる", ChatColor.LIGHT_PURPLE + "ワープできても出来なくても消費される使い捨て❤");
			break;
		default:
			break;
		}
		Notification.systemMessage(player, name + ChatColor.YELLOW + "が当たりました", Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) (id * 0.1 + 0.9));
	}
	void gacha2(Player player) {
		probability = new double[] {0.5, 1.5};
		id = weightingGacha(probability, player);
		switch (id) {
		case 0:
			giveCustomItem(player, CUSTOMITEMID.EXPLOSION, 1, ChatColor.RED + "自爆したいときや、周りを巻き込みたい時に...", ChatColor.GRAY + "ブロックは破壊しませんが、", ChatColor.GRAY + "爆発する場所には十分注意してご使用ください");
			break;
		case 1:
			giveCustomItem(player, CUSTOMITEMID.GODSEYE, 1, "神と契約して世界のすべてを見れる", "代償はあなたの命です。");
			break;
		default:
			break;
		}
		Notification.systemMessage(player, name + ChatColor.YELLOW + "が当たりました", Sound.ENTITY_PLAYER_LEVELUP, 1, (float) (id * 0.1 + 0.9));
	}

}
