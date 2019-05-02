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
		probability = new double[] {1,85,2,10,2};
		id = weightingGacha(probability, player);
		player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD +  "レアリティ ： " + String.valueOf(id + 1));
		switch (id) {
		case 1:
			gacha1(player);
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
		if (name != null) displayName = ChatColor.WHITE + name;
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
	/**プレイヤーにアイテムを付与します。*/
	void giveItem (Player player, Material itemtype, int count, String name, String ...lore) {
		item = new ItemStack(itemtype, count);
		itemMeta = item.getItemMeta();
		setItemNameAndLore(name, lore);
		player.getInventory().addItem(item);
	}

	void gacha1(Player player) {
		probability = new double[] {1, 1, 1, 1};
		id = weightingGacha(probability, player);
		switch (id) {
		case 0:
			name = "参加賞の" + ChatColor.RED + "リンゴ";
			giveItem(player, Material.APPLE, 1, name, ChatColor.GOLD + "旅のお供に・・・");
			break;
		case 1:
			name = "参加賞の" + ChatColor.GOLD + "クッキー";
			giveItem(player, Material.COOKIE, 1, name, "旅のお供に・・・");
			break;
		case 2:
			name = "参加賞の" + ChatColor.YELLOW + "たいまつ";
			giveItem(player, Material.TORCH, 1, null, (String)null);
			break;
		case 3:
			name =CUSTOMITEMID.ANGELSWING.getName();
			item = new ItemStack(CUSTOMITEMID.ANGELSWING.getType(), 2);
			itemMeta = item.getItemMeta();
			itemMeta.addEnchant(Enchantment.LURE, 1, false);
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			setItemNameAndLore(name, ChatColor.LIGHT_PURPLE + "高いところから落下すると、", ChatColor.LIGHT_PURPLE + "ダメージを軽減してくれる。", ChatColor.DARK_GRAY + "(右クリックで空高く跳ぶことも出来る！)");
			item = nbt.setNBTInt(item, customItem.itemNBTName, CUSTOMITEMID.ANGELSWING.ordinal());
			player.getInventory().addItem(item);
			break;
		default:
			player.sendMessage("このメッセージは でないはずだよ");
			break;
		}
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) (id * 0.1 + 0.9));
	}
}
