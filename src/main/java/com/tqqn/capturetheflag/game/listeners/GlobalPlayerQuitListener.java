package com.tqqn.capturetheflag.game.listeners;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class GlobalPlayerQuitListener implements Listener {

    private final GameManager gameManager;

    public GlobalPlayerQuitListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Player player = event.getPlayer();
        if (GameManager.getGameStates() == GameStates.LOBBY) {
            GameUtils.broadcastMessage(SMessages.PLAYER_QUIT.getMessage(player.getDisplayName()));
            gameManager.getArena().removePlayerFromGame(player);
        }
    }
}
