package com.tqqn.capturetheflag.utils.menubuilder;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Menu {

    private final Inventory inventory;
    private final Map<Integer, MenuButton> buttonMap;
    private Consumer<Player> inventoryClosed;
    private Consumer<Player> inventoryOpened;

    /**
     * Creates a new Menu Object.
     * @param title String
     * @param rows int
     */
    public Menu(String title, int rows) {
        if (rows > 6 || rows < 1 || title.length() > 32) {
            throw new IllegalArgumentException("Invalid arguments passed to menu constructor.");
        }
        this.inventory = Bukkit.createInventory(null, rows * 9, GameUtils.translateColor(title));
        this.buttonMap = new HashMap<>();
    }

    /**
     * Void Method that will register a new MenuButton.
     * @param button MenuButton
     * @param slot int
     */
    public void registerButton(MenuButton button, int slot) {
        buttonMap.put(slot, button);
    }

    /**
     * Void Method that will handle the inventory close.
     * @param player Player
     */
    public void handleClose(Player player) {
        if (inventoryClosed == null) return;

        inventoryClosed.accept(player);
    }

    /**
     * Void Method that will handle the inventory open.
     * @param player Player
     */
    public void handleOpen(Player player) {
        if (inventoryOpened == null) return;

        inventoryOpened.accept(player);
    }

    /**
     * Void Method that will handle the button click.
     * @param event InventoryClickEvent
     */
    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null) return;
        if (!buttonMap.containsKey(event.getRawSlot())) return;

        Consumer<Player> consumer = buttonMap.get(event.getRawSlot()).getWhoClicked();

        if (consumer == null) return;

        consumer.accept((Player) event.getWhoClicked());
    }

    /**
     * Void Method that will handle/create the inventory.
     * @param player Player
     */
    public void open(Player player) {
        MenuManager menuManager = CaptureTheFlag.getInstance().getMenuManager();

        buttonMap.forEach((slot, button) -> inventory.setItem(slot, button.getItemStack()));

        player.openInventory(inventory);
        menuManager.registerMenu(player.getUniqueId(), this);

        handleOpen(player);
    }
}
