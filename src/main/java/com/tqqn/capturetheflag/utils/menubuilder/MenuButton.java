package com.tqqn.capturetheflag.utils.menubuilder;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuButton {

    private final ItemStack itemStack;
    private Consumer<Player> whoClicked;

    /**
     * Creates a new MenuButton Object.
     * @param itemStack ItemStack
     */
    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Returns the Consumer (Player/whoClicked).
     */
    public Consumer<Player> getWhoClicked() {
        return whoClicked;
    }

    /**
     * Sets the Consumer (Player) and returns this MenuButton.
     * @param whoClicked Consumer(Player)
     */
    public MenuButton setWhoClicked(Consumer<Player> whoClicked) {
        this.whoClicked = whoClicked;
        return this;
    }

    /**
     * Returns the ItemStack.
     */
    public ItemStack getItemStack() {
        return itemStack;
    }
}
