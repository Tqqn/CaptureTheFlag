package com.tqqn.capturetheflag.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    private void updateItemMeta() {
        this.itemStack.setItemMeta(this.itemMeta);
    }

    public ItemBuilder setDisplayName(String name) {
        itemMeta.setDisplayName(GameUtils.translateColor(name));
        return this;
    }
    public ItemBuilder setLore(String... lines) {
        itemMeta.setLore(Arrays.asList(GameUtils.translateColor(lines)));
        return this;
    }

    public void addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
    }

    public void setGlow() {
        itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public void setUnbreakable() {
        itemMeta.setUnbreakable(true);
    }

    public ItemStack build() {
        this.updateItemMeta();
        return this.itemStack;
    }

}
