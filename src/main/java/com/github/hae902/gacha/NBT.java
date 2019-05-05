package com.github.hae902.gacha;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
/**MinecraftのVer変わるとほぼ確実に動かなくなる部分なので注意*/
public class NBT {
	net.minecraft.server.v1_14_R1.ItemStack customitem;
	NBTTagCompound NBT;
	void setNBT(ItemStack item) {
		//NBTに何かしら書いてないとエラー吐くので対策（条件はとりあえず名前と説明文だけ・・・）
		if (item.getItemMeta().getDisplayName() == null && item.getItemMeta().getLore() == null) return;
		customitem = CraftItemStack.asNMSCopy(item);
		NBT = customitem.getTag();
	}
	/**int型のタグをセット*/
	public ItemStack setNBTInt(ItemStack item, String name, int value) {
		setNBT(item);
		if (NBT == null) return item;
		NBT.setInt(name, value);;
		customitem.setTag(NBT);
		return CraftItemStack.asBukkitCopy(customitem);
	}
	/**int型のタグをゲット*/
	public int getNBTInt(ItemStack item, String name) {
		setNBT(item);
		if (NBT == null) return 0;
		return NBT.getInt(name);
	}
}