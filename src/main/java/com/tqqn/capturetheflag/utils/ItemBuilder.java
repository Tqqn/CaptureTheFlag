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
        this.itemMeta.setDisplayName(GameUtils.translateColor(name));
        return this;
    }
    public ItemBuilder setLore(String... lines) {
        this.itemMeta.setLore(Arrays.asList(GameUtils.translateColor(lines)));
        return this;
    }

    public void addEnchantment(Enchantment enchantment, int level) {
        this.itemMeta.addEnchant(enchantment, level, true);
    }

    public void setGlow() {
        this.itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public ItemStack build() {
        this.updateItemMeta();
        return this.itemStack;
    }

}
