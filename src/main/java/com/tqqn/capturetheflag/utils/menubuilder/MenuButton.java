package com.tqqn.capturetheflag.utils.menubuilder;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuButton {

    private final ItemStack itemStack;
    private Consumer<Player> whoClicked;

    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Consumer<Player> getWhoClicked() {
        return whoClicked;
    }

    public MenuButton setWhoClicked(Consumer<Player> whoClicked) {
        this.whoClicked = whoClicked;
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
