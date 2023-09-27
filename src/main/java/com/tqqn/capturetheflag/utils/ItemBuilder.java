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

    /**
     * Creates a new ItemBuilder Object.
     * @param material Material
     * @param amount int
     */
    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    /**
     * Void Method that will update the ItemMeta of the ItemStack.
     */
    private void updateItemMeta() {
        this.itemStack.setItemMeta(this.itemMeta);
    }

    /**
     * Sets the displayname of the item.
     * @param name String
     */
    public ItemBuilder setDisplayName(String name) {
        itemMeta.setDisplayName(GameUtils.translateColor(name));
        return this;
    }

    /**
     * Sets the lore of the item.
     * @param lines String...
     */
    public ItemBuilder setLore(String... lines) {
        itemMeta.setLore(Arrays.asList(GameUtils.translateColor(lines)));
        return this;
    }

    /**
     * Adds a enchantment to the item.
     * @param enchantment Enchantment
     * @param level int
     */
    public void addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
    }

    /**
     * Sets the glow of the item.
     */
    public void setGlow() {
        itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    /**
     * Sets the item unbreakable.
     */
    public void setUnbreakable() {
        itemMeta.setUnbreakable(true);
    }

    /**
     * Returns the build itemStack.
     */
    public ItemStack build() {
        this.updateItemMeta();
        return this.itemStack;
    }

}
