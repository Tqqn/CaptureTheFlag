package com.tqqn.capturetheflag.game.gamestates.active;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.data.GamePoints;
import com.tqqn.capturetheflag.game.flag.Flag;
import com.tqqn.capturetheflag.game.flag.FlagStatus;
import com.tqqn.capturetheflag.game.AbstractGameState;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.CompassLocatorTask;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.RespawnTask;
import com.tqqn.capturetheflag.game.gamestates.active.scoreboard.ActiveGameScoreboard;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.ActiveGameTask;
import com.tqqn.capturetheflag.game.tab.TabScoreboardManager;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.PluginSounds;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ActiveGameState extends AbstractGameState {

    private final GameManager gameManager;
    private ActiveGameTask activeGameTask;
    private CompassLocatorTask compassLocatorTask;

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

        activeGameTask = new ActiveGameTask(gameManager);
        activeGameTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);

        compassLocatorTask = new CompassLocatorTask(gameManager);
        compassLocatorTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 10L);

        gameManager.getArena().spawnRandomPowerUp();
        PluginSounds.START_GAME.playSoundForAll();
    }

    @Override
    public void unRegister() {
        if (!activeGameTask.isCancelled()) activeGameTask.cancel();
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        if (GameManager.getGameStates() == GameStates.ACTIVE) {
            if (Arena.getGamePlayer(event.getEntity().getUniqueId()).isSpectator() || Arena.getGamePlayer(event.getDamager().getUniqueId()).isSpectator()) {
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

        event.getDrops().clear();
        event.setDroppedExp(0);

        GamePlayer gamePlayer = Arena.getGamePlayer(player.getUniqueId());
        event.setDeathMessage("");
        PluginSounds.PLAYER_DEATH.playSound(player);

        if (player.getKiller() == null || player.getKiller() == player) {
            GameUtils.broadcastMessage(SMessages.PLAYER_DEATH.getMessage(gamePlayer.getTeam().getTeamColor().getColor() + player.getName()));
        } else {
            GamePlayer killer;
            if (player.getKiller() instanceof Arrow) {
                Arrow arrow = (Arrow) player.getKiller();
                if (!(arrow.getShooter() instanceof Player)) return;

                Player shooter = (Player) arrow.getShooter();
                killer = Arena.getGamePlayer(shooter.getUniqueId());
            } else {
                killer = Arena.getGamePlayer(player.getKiller().getUniqueId());
            }

            GameUtils.broadcastMessage(SMessages.PLAYER_DEATH_BY_PLAYER.getMessage(gamePlayer.getTeam().getTeamColor().getColor() + player.getName(), killer.getTeam().getTeamColor().getColor() + killer.getPlayer().getName()));
            killer.addKill();
            killer.getTeam().addPoints(GamePoints.PLAYER_KILL.getPoints());
        }

        for (GamePlayer assistDamager : gamePlayer.getAssistPlayersSet()) {
            if (!(assistDamager.getPlayer() == player.getKiller())) assistDamager.addAssist();
        }

        gamePlayer.getAssistPlayersSet().clear();

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
                        PluginSounds.OWN_FLAG_RETURNED.playSoundToTeam(gamePlayer.getTeam());
                        PluginSounds.ENEMY_FLAG_RETURNED.playSoundToTeam(gameManager.getEnemyTeam(gamePlayer));
                        return;
                    }
                    if (!gamePlayer.hasFlag()) return; //IF PLAYER HAS ENEMY FLAG, CAPTURE
                    gamePlayer.getTeam().captureEnemyFlag(player, gamePlayer.getFlag());
                    PluginSounds.ENEMY_FLAG_CAPTURED.playSoundToTeam(gamePlayer.getTeam());
                    PluginSounds.OWN_FLAG_CAPTURED.playSoundToTeam(gameManager.getEnemyTeam(gamePlayer));
                    return;
                }

                flag.getGameTeam().pickupFlag(player);
                PluginSounds.ENEMY_FLAG_STOLEN.playSoundToTeam(gamePlayer.getTeam());
                PluginSounds.OWN_FLAG_STOLEN.playSoundToTeam(gameManager.getEnemyTeam(gamePlayer));
            }
        }
    }

    @EventHandler
    public void onPlayerCompassInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getItem() != null) {
                if (event.getItem().getType() == Material.COMPASS) {
                    String targetName = "";
                    ItemStack compass = event.getItem();
                    switch (CaptureTheFlag.getReflectionLayer().getNBTTag(compass, "flagSelected").replace("\"", "")) {
                        case "red":
                        case "none":
                            compass.setItemMeta(CaptureTheFlag.getReflectionLayer().applyNBTTag(compass, "flagSelected", "blue").getItemMeta());
                            targetName = GameUtils.translateColor("&bTargeting: &9Blue Flag");
                            break;
                        case "blue":
                            compass.setItemMeta(CaptureTheFlag.getReflectionLayer().applyNBTTag(compass, "flagSelected", "red").getItemMeta());
                            targetName = GameUtils.translateColor("&bTargeting: &cRed Flag");
                            break;
                    }
                    ItemMeta itemMeta = compass.getItemMeta();
                    itemMeta.setDisplayName(targetName);
                    compass.setItemMeta(itemMeta);
                }
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
        } else {
            event.setRespawnLocation(gamePlayer.getTeam().getSpawnLocation());
        }

        gamePlayer.setSpectator();
        Bukkit.getScheduler().runTaskLater(CaptureTheFlag.getInstance(), gamePlayer::setSpectator, 1L);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (Arena.getGamePlayer(player.getUniqueId()).isSpectator()) event.setCancelled(true);
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getEntity();
        arrow.remove();
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        GamePlayer gamePlayer = Arena.getGamePlayer(event.getPlayer().getUniqueId());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (gamePlayer.getTeam() == Arena.getGamePlayer(player.getUniqueId()).getTeam()) {
                player.sendMessage(SMessages.TEAM_CHAT_MESSAGE_FORMAT.getMessage(gamePlayer.getTeam().getTeamTabPrefix().getPrefix(), gamePlayer.getPlayer().getName(), event.getMessage()));
            }
        }
    }
}
