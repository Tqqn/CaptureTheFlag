package com.tqqn.capturetheflag.arena;

import com.tqqn.capturetheflag.data.GamePlayer;
import com.tqqn.capturetheflag.data.GamePoints;
import com.tqqn.capturetheflag.teams.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Arena {

    private final Location lobbyLocation;
    private final Team teamRed;
    private final Team teamBlue;

    private final int minimumPlayers;
    private final int maximumPlayers;

    private final HashMap<UUID, GamePlayer> inGamePlayers = new HashMap<>();

    public Arena(Location lobbyLocation, Team teamRed, Team teamBlue, int minimumPlayers, int maximumPlayers) {
        this.lobbyLocation = lobbyLocation;
        this.teamRed = teamRed;
        this.teamBlue = teamBlue;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
    }

    public void teleportPlayerToSpawn(Player player) {
        try {
            player.teleport(inGamePlayers.get(player.getUniqueId()).getTeam().getSpawnLocation());
        } catch (NullPointerException ignored) {
        }
    }

    public void teleportAllPlayersToSpawn() {
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            teleportPlayerToSpawn(gamePlayer.getPlayer());
        }
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
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

    public List<GamePlayer> getNoTeamPlayers() {
        List<GamePlayer> playerList = new ArrayList<>();
        for (GamePlayer gamePlayer : inGamePlayers.values()) {
            if (gamePlayer.getTeam() == null) playerList.add(gamePlayer);
        }
        return playerList;
    }

    public GamePlayer getGamePlayer(UUID uuid) {
        return inGamePlayers.get(uuid);
    }

    public void addNewPlayer(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player);
        inGamePlayers.put(gamePlayer.getPlayer().getUniqueId(), gamePlayer);
    }

    public void removePlayerFromGame(Player player) {
        inGamePlayers.remove(player.getUniqueId());
    }

    public Team isThereAWinner() {
        if (teamRed.getPoints() == GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) return teamRed;
        if (teamBlue.getPoints() == GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) return teamBlue;

        return null;
    }

    public void addTeamPoints(GamePoints gamePoints) {
        teamRed.addPoints(gamePoints.getPoints());
        teamBlue.addPoints(gamePoints.getPoints());
    }

    public boolean checkIfTeam(Player damager, Player damaged) {
        GamePlayer playerDamager = inGamePlayers.get(damager.getUniqueId());
        GamePlayer damagedPlayer = inGamePlayers.get(damaged.getUniqueId());

        return playerDamager.getTeam().equals(damagedPlayer.getTeam());
    }
}
