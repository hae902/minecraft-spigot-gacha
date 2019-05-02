package com.github.hae902.gacha;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hae902.gacha.customitem.AngelsWing;
import com.github.hae902.gacha.customitem.CustomItemCalling;

public class Main extends JavaPlugin implements Listener {
	private static  Main plugin;

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new CustomItemCalling(), this);
		getServer().getPluginManager().registerEvents(new AngelsWing(), this);
	}

	public static Main getPlugin() {
		return plugin;
	}

	/**引数のブロックが看板だったら、1行目の文字と引数のStringが一致しているか判定する*/
	public boolean isMatchSign(Block block, String name) {
		if(block.getType() == Material.WALL_SIGN) {
			Sign sign = (Sign) block.getState();
			if (sign.getLine(0).matches(name)) return true;
		}
		return false;
	}

	@EventHandler
	public void sign(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(isMatchSign(block, "運命のガチャ")) {
			new Gacha().rarityGacha(player);
		}
	}
}
