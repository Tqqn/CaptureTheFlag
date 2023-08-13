package com.tqqn.capturetheflag.listeners.player;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    private final GameManager gameManager;

    public PlayerDamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (gameManager.getGameStates() == GameStates.ACTIVE) {
            if (!(event.getDamager() instanceof Player) && !(event.getEntity() instanceof Player)) event.setCancelled(true);
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();

            if (gameManager.getArena().checkIfTeam(damager, damaged)) event.setCancelled(true);
        }

        event.setCancelled(true);
    }
}
