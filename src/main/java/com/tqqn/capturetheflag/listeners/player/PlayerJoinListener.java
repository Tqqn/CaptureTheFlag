package com.tqqn.capturetheflag.listeners.player;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final GameManager gameManager;

    public PlayerJoinListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (gameManager.getGameStates() == GameStates.ACTIVE) {
            player.kickPlayer("Game has already started.");
            return;
        }

        gameManager.getArena().addNewPlayer(player);
        event.setJoinMessage("");
        GameUtils.broadcastMessage(SMessages.PLAYER_JOIN.getMessage(player.getDisplayName(), String.valueOf(gameManager.getArena().getInGamePlayers().size()), String.valueOf(gameManager.getArena().getMaximumPlayers())));
        player.teleport(gameManager.getArena().getLobbyLocation());
        gameManager.giveLobbyItems(player);
    }
}
