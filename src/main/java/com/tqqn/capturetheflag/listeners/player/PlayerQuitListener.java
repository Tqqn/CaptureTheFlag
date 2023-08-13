package com.tqqn.capturetheflag.listeners.player;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final GameManager gameManager;

    public PlayerQuitListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        gameManager.getArena().removePlayerFromGame(player);
        GameUtils.broadcastMessage(SMessages.PLAYER_QUIT.getMessage(player.getDisplayName()));
    }
}
