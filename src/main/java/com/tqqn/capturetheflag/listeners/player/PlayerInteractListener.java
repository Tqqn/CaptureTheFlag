package com.tqqn.capturetheflag.listeners.player;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.items.GameItems;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final GameManager gameManager;

    public PlayerInteractListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (gameManager.getGameStates() == GameStates.ACTIVE) return;

        if (GameItems.CHOOSE_RED_TEAM.getItemStack().isSimilar(event.getItem())) {
            if (gameManager.getArena().getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamRed()) {
                player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));
                return;
            }
            gameManager.getArena().getGamePlayer(player.getUniqueId()).setTeam(gameManager.getTeamRed());
            player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));

            } else if (GameItems.CHOOSE_BLUE_TEAM.getItemStack().isSimilar(event.getItem())) {
                if (gameManager.getArena().getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamBlue()) {
                    player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
                    return;
                }
                gameManager.getArena().getGamePlayer(player.getUniqueId()).setTeam(gameManager.getTeamBlue());
                player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
            }
    }
}
