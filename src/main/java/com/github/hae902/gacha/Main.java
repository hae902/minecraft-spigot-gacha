package com.github.hae902.gacha;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hae902.gacha.Utility.Notification;
import com.github.hae902.gacha.customitem.AngelsWing;
import com.github.hae902.gacha.customitem.CustomItemCalling;
import com.github.hae902.gacha.customitem.GodsEye;

public class Main extends JavaPlugin implements Listener {
	private static  Main plugin;
	public static String deathMessage = null;
	/*public static ArrayList<Player> players = new ArrayList<Player>();*/
	Notification not = new Notification();


	@Override
	public void onEnable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new CustomItemCalling(), this);
		getServer().getPluginManager().registerEvents(new AngelsWing(), this);
		getServer().getPluginManager().registerEvents(new GodsEye(), this);
	}
	@EventHandler
	public void login(PlayerJoinEvent event) {
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		not.playSoundForPlayer(Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
	}
	@EventHandler
	public void logout(PlayerQuitEvent event) {
	}

	public static Main getPlugin() {
		return plugin;
	}

	/**引数のブロックが看板だったら、1行目の文字と引数のStringが一致しているか判定する*/
	public boolean isMatchSign(Block block, String name) {
		if(block.getType() == Material.OAK_WALL_SIGN) {
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
		if(!isMatchSign(block, "運命のガチャ")) return;
		if (player.getLevel() >= 3 || player.getGameMode() == GameMode.CREATIVE) {
			new Gacha().rarityGacha(player);
			if (player.getGameMode() != GameMode.CREATIVE) {
				player.setLevel(player.getLevel() - 3);
			}
		}else {
			player.sendMessage("レベルが足りません。");
		}
	}
	@EventHandler
	public void kill(PlayerDeathEvent event) {
		if (deathMessage == null) return;
		event.setDeathMessage(deathMessage);
		deathMessage = null;
	}
}
