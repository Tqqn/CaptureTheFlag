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
import com.tqqn.capturetheflag.game.powerups.types.RegenPowerUp;
import com.tqqn.capturetheflag.game.powerups.types.SpeedPowerUp;
import com.tqqn.capturetheflag.game.powerups.types.StrenghtPowerUp;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Arena {

    private final Location lobbyLocation;
    private final GameTeam gameTeamRed;
    private final GameTeam gameTeamBlue;

    private final int minimumPlayers;
    private final int maximumPlayers;

    private static final Map<UUID, GamePlayer> inGamePlayers = new HashMap<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final List<Location> powerUpsSpawnLocation = CaptureTheFlag.getInstance().getPluginConfig().getPowerUpSpawnLocations();

    /**
     * Arena Constructor - Creates Arena Object.
     * @param lobbyLocation - Location of the lobby
     * @param gameTeamRed - GameTeam RED
     * @param gameTeamBlue - GameTeam BLUE
     * @param minimumPlayers Integer of the minimum players required
     * @param maximumPlayers Intereger of the maximum players required
     */
    public Arena(Location lobbyLocation, GameTeam gameTeamRed, GameTeam gameTeamBlue, int minimumPlayers, int maximumPlayers) {
        this.lobbyLocation = lobbyLocation;
        this.gameTeamRed = gameTeamRed;
        this.gameTeamBlue = gameTeamBlue;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
        powerUps.add(new JumpPowerUp(null));
        powerUps.add(new SpeedPowerUp(null));
        powerUps.add(new StrenghtPowerUp(null));
        powerUps.add(new RegenPowerUp(null));
    }

    /**
     * Spawns/teleports the ingamePlayers to their spawn location.
     */
    public void spawnPlayers() {
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            gamePlayer.spawn(gamePlayer.getTeam().getSpawnLocation());
        }
    }

    /**
     * Clears the inventories of ingamePlayers.
     */
    public void clearAllPlayerInventories() {
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            gamePlayer.getPlayer().getInventory().clear();
        }
    }

    /**
     * Returns the maximum of allowed players.
     */
    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    /**
     * Returns the location of the lobby.
     */
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    /**
     * Returns a List with players that are registered ingamePlayers.
     */
    public List<Player> getInGamePlayers() {
        List<Player> playerList = new ArrayList<>();
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            playerList.add(gamePlayer.getPlayer());
        }
        return playerList;
    }

    /**
     * Calculates how many players needed to start the game.
     * If there are already enough players return 0.
     */
    public int getPlayersToStart() {
        if (minimumPlayers >= inGamePlayers.size()) {
            return minimumPlayers - inGamePlayers.size();
        }
        return 0;
    }

    /**
     * Returns Collection of inGamePlayers.
     */
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

    /**
     * Static Method that returns a GamePlayer based on the giving Player UUID.
     * @param uuid - Player UUID
     */
    public static GamePlayer getGamePlayer(UUID uuid) {
        return inGamePlayers.get(uuid);
    }

    /**
     * Adds a new GamePlayer.
     * @param player Player
     */
    public void addNewPlayer(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player);
        inGamePlayers.put(gamePlayer.getPlayer().getUniqueId(), gamePlayer);
    }

    /**
     * Checks if the game can start based on the joined players.
     */
    public boolean canStart() {
        return (inGamePlayers.size() >= minimumPlayers);
    }

    /**
     * Removes a player from the game.
     * @param player Player
     */
    public void removePlayerFromGame(Player player) {
        inGamePlayers.remove(player.getUniqueId());
    }

    /**
     * Calculates if there is a winner/if a team has reached the maximum points. If not return null.
     */
    public GameTeam isThereAWinner() {
        if (gameTeamRed.getPoints() >= GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) return gameTeamRed;
        if (gameTeamBlue.getPoints() >= GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) return gameTeamBlue;

        return null;
    }

    /**
     * Returns the team with the most points. If both teams have the same points it will return null.
     * @return
     */
    public GameTeam whoIsWinner() {
        if (gameTeamBlue.getPoints() > gameTeamRed.getPoints()) {
            return gameTeamBlue;
        } else if (gameTeamRed.getPoints() > gameTeamBlue.getPoints()) {
            return gameTeamRed;
        } else {
            return null;
        }
    }

    /**
     * Increases the points of both teams.
     * @param gamePoints Integer
     */
    public void addTeamPoints(GamePoints gamePoints) {
        gameTeamRed.addPoints(gamePoints.getPoints());
        gameTeamBlue.addPoints(gamePoints.getPoints());
    }

    /**
     * Checks if the given players are both in the same team.
     * @param damager Player
     * @param damaged Player
     */
    public boolean checkIfTeam(Player damager, Player damaged) {
        GamePlayer playerDamager = inGamePlayers.get(damager.getUniqueId());
        GamePlayer damagedPlayer = inGamePlayers.get(damaged.getUniqueId());

        return playerDamager.getTeam().equals(damagedPlayer.getTeam());
    }

    /**
     * Spawns the flags of both team at their spawn location.
     */
    public void spawnFlags() {
        gameTeamBlue.spawnTeamFlagOnSpawn();
        gameTeamRed.spawnTeamFlagOnSpawn();
    }

    /**
     * Resets/Respawns the flags of both teams after 10 seconds.
     */
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

    /**
     * Will spawn a random PowerUp on a random PowerUp location.
     */
    public void spawnRandomPowerUp() {
        PowerUp powerUp = getRandomPowerUp();
        powerUp.setLocation(getRandomPowerUpSpawnLocation());
        powerUp.setPowerUp();
        ActivePowerUpTask activePowerUpTask = new ActivePowerUpTask(powerUp);
        activePowerUpTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 2L);
    }

    /**
     * Returns a random PowerUp.
     */
    public PowerUp getRandomPowerUp() {
        Random random = new Random();
        return powerUps.get(random.nextInt(powerUps.size()));
    }

    /**
     * Returns a random PowerUp spawn location.
     */
    public Location getRandomPowerUpSpawnLocation() {
        Random random = new Random();
        return powerUpsSpawnLocation.get(random.nextInt(powerUpsSpawnLocation.size()));
    }
}
