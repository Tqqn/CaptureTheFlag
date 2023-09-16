package com.tqqn.capturetheflag.game.gamestates.active;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.flag.Flag;
import com.tqqn.capturetheflag.game.flag.FlagStatus;
import com.tqqn.capturetheflag.game.AbstractGameState;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.RespawnTask;
import com.tqqn.capturetheflag.game.gamestates.active.scoreboard.ActiveGameScoreboard;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.ActiveGameTask;
import com.tqqn.capturetheflag.game.tab.TabScoreboardManager;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ActiveGameState extends AbstractGameState {

    private final GameManager gameManager;
    private ActiveGameTask activeGameTask;

    public ActiveGameState(GameManager gameManager) {
        super("Active");
        this.gameManager = gameManager;
    }
    @Override
    public void register() {
        gameManager.balanceTeamPlayers();
        gameManager.getArena().clearAllPlayerInventories();
        gameManager.getArena().spawnPlayers();
        gameManager.getArena().spawnFlags();
        TabScoreboardManager.setAllPlayersTabTeam(gameManager.getArena().getGamePlayers());

        ActiveGameScoreboard activeGameScoreboard = new ActiveGameScoreboard(gameManager);
        activeGameScoreboard.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);

        this.activeGameTask = new ActiveGameTask(gameManager);
        this.activeGameTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);

        gameManager.getArena().spawnRandomPowerUp();
    }

    @Override
    public void unRegister() {
        if (!activeGameTask.isCancelled()) activeGameTask.cancel();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        if (GameManager.getGameStates() == GameStates.ACTIVE) {
            if (Arena.getGamePlayer(event.getEntity().getUniqueId()).isSpectator()) {
                event.setCancelled(true);
                return;
            }
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            if (gameManager.getArena().checkIfTeam(damager, damaged)) {
                event.setCancelled(true);
                return;
            }
            Arena.getGamePlayer(damaged.getUniqueId()).addPlayerToAssistList(Arena.getGamePlayer(damager.getUniqueId()));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = Arena.getGamePlayer(player.getUniqueId());
        event.setDeathMessage("");

        if (player.getKiller() == null || player.getKiller() == player) {
            GameUtils.broadcastMessage(SMessages.PLAYER_DEATH.getMessage(gamePlayer.getTeam().getTeamColor().getColor() + player.getName()));
        } else {
            GamePlayer killer = Arena.getGamePlayer(player.getKiller().getUniqueId());
            GameUtils.broadcastMessage(SMessages.PLAYER_DEATH_BY_PLAYER.getMessage(gamePlayer.getTeam().getTeamColor().getColor() + player.getName(), killer.getTeam().getTeamColor().getColor() + killer.getPlayer().getName()));
            killer.addKill();
        }

        for (GamePlayer assistDamager : gamePlayer.getAssistPlayersSet()) {
            if (assistDamager.getPlayer() == player.getKiller()) return;
            assistDamager.addAssist();
        }

        gamePlayer.getAssistPlayersSet().clear();

        event.getDrops().clear();

        if (Arena.getGamePlayer(player.getUniqueId()).hasFlag()) {
            gamePlayer.getFlag().getGameTeam().dropFlag(player);
            gamePlayer.removeFlag();
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureTheFlag.getInstance(), () -> player.spigot().respawn(),2L);
    }

    @EventHandler
    public void onPlayerFlagInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        if (event.getClickedBlock().getType() == Material.BLUE_BANNER || event.getClickedBlock().getType() == Material.RED_BANNER) {
            Player player = event.getPlayer();
            GamePlayer gamePlayer = Arena.getGamePlayer(player.getUniqueId());

            if (GameManager.isLocationSpawnedFlag(event.getClickedBlock().getLocation())) {
                Flag flag = GameManager.getFlag(event.getClickedBlock().getLocation());

                if (gamePlayer.getTeam() == flag.getGameTeam()) { //SAME TEAM
                    if (flag.getFlagStatus() == FlagStatus.DROPPED) { //OWN FLAG IS DROPPED, RETURN
                        gamePlayer.getTeam().returnFlag(player);
                        return;
                    }
                    if (!gamePlayer.hasFlag()) return; //IF PLAYER HAS ENEMY FLAG, CAPTURE
                    gamePlayer.getTeam().captureEnemyFlag(player, gamePlayer.getFlag());
                    return;
                }

                flag.getGameTeam().pickupFlag(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (GameManager.getGameStates() == GameStates.ACTIVE) {
            player.kickPlayer("Game has already started.");
        }


    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        GamePlayer gamePlayer = Arena.getGamePlayer(event.getPlayer().getUniqueId());
        RespawnTask respawnTask = new RespawnTask(gamePlayer);
        respawnTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);
        if (gamePlayer.getPlayer().getKiller() != null) {
            event.setRespawnLocation(gamePlayer.getPlayer().getKiller().getLocation());
            return;
        }
        event.setRespawnLocation(gamePlayer.getTeam().getSpawnLocation());

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureTheFlag.getInstance(), gamePlayer::setSpectator,2L);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (Arena.getGamePlayer(player.getUniqueId()).isSpectator()) event.setCancelled(true);
    }
}
