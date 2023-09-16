package com.tqqn.capturetheflag.game.listeners.global;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;

public class GlobalPlayerInventoryListener implements Listener {

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (GameManager.getGameStates() == GameStates.ACTIVE) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
            event.setCancelled(true);
        }
    }
}
