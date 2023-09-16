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

    public void giveKit(Player player) {
        player.getInventory().clear();

        for (Map.Entry<Integer, ItemStack> set : getInventoryItems().entrySet()) {
            player.getInventory().setItem(set.getKey(), set.getValue());
        }
        player.getInventory().setArmorContents(getArmorItems());

    }

    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    public ItemStack getIcon() {
        return icon;
    }

    public abstract Map<Integer, ItemStack> getInventoryItems();
    public abstract ItemStack[] getArmorItems();

}
