package com.tqqn.capturetheflag.listeners.player;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class PlayerInventoryInteractListener implements Listener {

    private final GameManager gameManager;

    public PlayerInventoryInteractListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (gameManager.getGameStates() == GameStates.ACTIVE) return;
        event.setCancelled(true);
    }
}
