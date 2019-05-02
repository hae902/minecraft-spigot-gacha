package com.github.hae902.gacha.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.hae902.gacha.NBT;

public class CustomItemCalling implements Listener{
	NBT nbt = new NBT();
	public String itemNBTName = "CustomItemId";
	public static enum CUSTOMITEMID {
		NULL("エラー", Material.STONE),//IDが埋め込まれてない時はこっちに流れる。
		ANGELSWING(ChatColor.WHITE + "天使の翼", Material.FEATHER),
		EXPLOSION(ChatColor.RED + "自爆スイッチ", Material.CLOCK),
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
		int id = nbt.getNBTInt(itemMainHand, itemNBTName);
		switch (values[id]) {
		case ANGELSWING:
			new AngelsWing().use(player);
			break;
		case EXPLOSION:
			new Omniscient().use(player);
		default:
			break;
		}
	}

	/**特定のアイテムを持っていたら落下ダメージを軽減*/
}
