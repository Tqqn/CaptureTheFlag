package com.tqqn.capturetheflag.game.kits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class AbstractKit {

    private final String name;
    private final ItemStack icon;

    public AbstractKit(String name, ItemStack icon) {
        this.name = name;
        this.icon = icon;
    }

    /**
     * Gives the kit to the given player.
     * @param player Player
     */
    public void giveKit(Player player) {
        player.getInventory().clear();

        for (Map.Entry<Integer, ItemStack> set : getInventoryItems().entrySet()) {
            player.getInventory().setItem(set.getKey(), set.getValue());
        }
        player.getInventory().setArmorContents(getArmorItems());

    }

    /**
     * Returns the name.
     */
    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * Returns the ItemStack icon of the kit.
     */
    public ItemStack getIcon() {
        return icon;
    }

    /**
     * Abstract method that will return a Map with ItemStacks.
     */
    public abstract Map<Integer, ItemStack> getInventoryItems();

    /**
     * Abstract Method that will return an array of ItemStacks.
     */
    public abstract ItemStack[] getArmorItems();

}
