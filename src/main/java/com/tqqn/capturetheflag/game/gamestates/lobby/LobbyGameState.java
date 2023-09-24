package com.tqqn.capturetheflag.game.gamestates.lobby;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.AbstractGameState;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.gamestates.lobby.tasks.LobbyWaitingGameTask;
import com.tqqn.capturetheflag.items.PluginItems;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyGameState extends AbstractGameState {

    private final GameManager gameManager;
    private LobbyWaitingGameTask lobbyWaitingGameTask;
    private LobbyScoreboard lobbyScoreboard;

    public LobbyGameState(GameManager gameManager) {
        super("Lobby");
        this.gameManager = gameManager;
    }
    @Override
    public void register() {
        this.lobbyWaitingGameTask = new LobbyWaitingGameTask(gameManager);
        lobbyWaitingGameTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);

        this.lobbyScoreboard = new LobbyScoreboard(gameManager);
        lobbyScoreboard.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);
    }

    @Override
    public void unRegister() {
        lobbyWaitingGameTask.cancel();
        lobbyScoreboard.cancel();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Arena.getGamePlayer(player.getUniqueId());

        if (GameManager.getGameStates() == GameStates.LOBBY) {
            if (PluginItems.CHOOSE_RED_TEAM.getItemStack().isSimilar(event.getItem())) {
                if (Arena.getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamRed()) {
                    player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));
                    return;
                }

                if (gameManager.whichTeamIsSmaller() == gameManager.getTeamRed()) {
                    if (gamePlayer.getTeam() != null) {
                        gamePlayer.getTeam().removePlayerFromTeam(gamePlayer);
                    }

                    gameManager.getTeamRed().addPlayerToTeam(gamePlayer, gameManager.getTeamRed());
                    player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));
                    return;
                }

            } else if (PluginItems.CHOOSE_BLUE_TEAM.getItemStack().isSimilar(event.getItem())) {
                if (Arena.getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamBlue()) {
                    player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
                    return;
                }

                if (gameManager.whichTeamIsSmaller() == gameManager.getTeamBlue()) {
                    if (gamePlayer.getTeam() != null) {
                        gamePlayer.getTeam().removePlayerFromTeam(gamePlayer);
                    }
                    gameManager.getTeamBlue().addPlayerToTeam(gamePlayer, gameManager.getTeamBlue());
                    player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
                    return;
                }
            }
            player.sendMessage(NMessages.TEAM_IS_FULL.getMessage());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage("");

        player.teleport(gameManager.getArena().getLobbyLocation());
        gameManager.getArena().addNewPlayer(player);
        GameUtils.broadcastMessage(SMessages.PLAYER_JOIN.getMessage(player.getDisplayName(), String.valueOf(gameManager.getArena().getInGamePlayers().size()), String.valueOf(gameManager.getArena().getMaximumPlayers())));
        gameManager.giveLobbyItems(player);


    }
}
