package com.tqqn.capturetheflag.game.arena;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.data.GamePoints;
import com.tqqn.capturetheflag.game.flag.FlagStatus;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.powerups.PowerUp;
import com.tqqn.capturetheflag.game.powerups.tasks.ActivePowerUpTask;
import com.tqqn.capturetheflag.game.powerups.types.JumpPowerUp;
import com.tqqn.capturetheflag.game.powerups.types.SpeedPowerUp;
import com.tqqn.capturetheflag.game.powerups.types.StrenghtPowerUp;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Arena {

    private final Location lobbyLocation;
    private final GameTeam gameTeamRed;
    private final GameTeam gameTeamBlue;

    private final int minimumPlayers;
    private final int maximumPlayers;

    private final int gameStartCountdown;
    private static final Map<UUID, GamePlayer> inGamePlayers = new HashMap<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final List<Location> powerUpsSpawnLocation = CaptureTheFlag.getInstance().getPluginConfig().getPowerUpSpawnLocations();

    public Arena(Location lobbyLocation, GameTeam gameTeamRed, GameTeam gameTeamBlue, int minimumPlayers, int maximumPlayers, int gameStartCountdown) {
        this.lobbyLocation = lobbyLocation;
        this.gameTeamRed = gameTeamRed;
        this.gameTeamBlue = gameTeamBlue;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
        this.gameStartCountdown = gameStartCountdown;
        powerUps.add(new JumpPowerUp(null));
        powerUps.add(new SpeedPowerUp(null));
        powerUps.add(new StrenghtPowerUp(null));
    }

    public void teleportPlayerToSpawn(Player player) {
            player.teleport(inGamePlayers.get(player.getUniqueId()).getTeam().getSpawnLocation());
    }

    public void spawnPlayers() {
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            gamePlayer.spawn(gamePlayer.getTeam().getSpawnLocation());
        }
    }

    public void clearAllPlayerInventories() {
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            gamePlayer.getPlayer().getInventory().clear();
        }
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public int getGameStartCountdown() {
        return gameStartCountdown;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public List<Player> getInGamePlayers() {
        List<Player> playerList = new ArrayList<>();
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            playerList.add(gamePlayer.getPlayer());
        }
        return playerList;
    }

    public Collection<GamePlayer> getGamePlayers() {
        return inGamePlayers.values();
    }

    public List<GamePlayer> getNoTeamPlayers() {
        List<GamePlayer> playerList = new ArrayList<>();
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            if (gamePlayer.getTeam() == null) playerList.add(gamePlayer);
        }
        return playerList;
    }

    public static GamePlayer getGamePlayer(UUID uuid) {
        return inGamePlayers.get(uuid);
    }

    public void addNewPlayer(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player);
        inGamePlayers.put(gamePlayer.getPlayer().getUniqueId(), gamePlayer);
    }

    public void setSpectator(Player player) {

    }

    public void removePlayerFromGame(Player player) {
        inGamePlayers.remove(player.getUniqueId());
    }

    public GameTeam isThereAWinner() {
        if (gameTeamRed.getPoints() >= GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) return gameTeamRed;
        if (gameTeamBlue.getPoints() >= GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) return gameTeamBlue;

        return null;
    }
    public GameTeam whoIsWinner() {
        if (gameTeamBlue.getPoints() > gameTeamRed.getPoints()) {
            return gameTeamBlue;
        } else if (gameTeamRed.getPoints() > gameTeamBlue.getPoints()) {
            return gameTeamRed;
        } else {
            return null;
        }
    }

    public void addTeamPoints(GamePoints gamePoints) {
        gameTeamRed.addPoints(gamePoints.getPoints());
        gameTeamBlue.addPoints(gamePoints.getPoints());
    }

    public boolean checkIfTeam(Player damager, Player damaged) {
        GamePlayer playerDamager = inGamePlayers.get(damager.getUniqueId());
        GamePlayer damagedPlayer = inGamePlayers.get(damaged.getUniqueId());

        return playerDamager.getTeam().equals(damagedPlayer.getTeam());
    }

    public void spawnFlags() {
        gameTeamBlue.spawnTeamFlagOnSpawn();
        gameTeamRed.spawnTeamFlagOnSpawn();
    }

    public void resetFlags() {
        gameTeamBlue.removeFlag();
        gameTeamBlue.getTeamFlag().setFlagStatus(FlagStatus.RESPAWNING);

        gameTeamRed.removeFlag();
        gameTeamRed.getTeamFlag().setFlagStatus(FlagStatus.RESPAWNING);

        new BukkitRunnable() {
            private int countdown = 10;
            @Override
            public void run() {
                if (GameManager.getGameStates() != GameStates.ACTIVE) {
                    cancel();
                    return;
                }
                if (countdown == 0) {
                    cancel();
                    gameTeamBlue.spawnTeamFlagOnSpawn();
                    gameTeamRed.spawnTeamFlagOnSpawn();
                    return;
                }
                GameUtils.broadcastMessage(SMessages.FLAG_RESET_COUNTDOWN.getMessage(String.valueOf(countdown)));
                countdown--;
            }
        }.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);
    }

    public void spawnRandomPowerUp() {
        PowerUp powerUp = getRandomPowerUp();
        powerUp.setLocation(getRandomPowerUpSpawnLocation());
        powerUp.setPowerUp();
        ActivePowerUpTask activePowerUpTask = new ActivePowerUpTask(powerUp);
        activePowerUpTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 10);
    }

    public PowerUp getRandomPowerUp() {
        Random random = new Random();
        return powerUps.get(random.nextInt(powerUps.size()));
    }

    public Location getRandomPowerUpSpawnLocation() {
        Random random = new Random();
        return powerUpsSpawnLocation.get(random.nextInt(powerUpsSpawnLocation.size()));
    }
}
