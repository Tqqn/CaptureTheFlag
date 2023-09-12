package com.tqqn.capturetheflag.teams;

import com.tqqn.capturetheflag.arena.Arena;
import com.tqqn.capturetheflag.data.GamePlayer;
import com.tqqn.capturetheflag.data.GamePoints;
import com.tqqn.capturetheflag.flag.Flag;
import com.tqqn.capturetheflag.flag.FlagStatus;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class GameTeam {

    private final String displayName;
    private final TeamColor teamColor;
    private final TeamTabPrefix teamTabPrefix;
    private final TeamChatPrefix teamChatPrefix;
    private final int tabPriority;
    private final Location spawnLocation;
    private final Flag teamFlag;
    private final GameManager gameManager;

    private final Collection<GamePlayer> members = new ArrayList<>();
    private int points = 0;

    public GameTeam(String displayName, TeamColor teamColor, TeamTabPrefix teamTabPrefix, TeamChatPrefix teamChatPrefix, int tabPriority, Location spawnLocation, Flag teamFlag, GameManager gameManager) {
        this.displayName = displayName;
        this.teamColor = teamColor;
        this.teamTabPrefix = teamTabPrefix;
        this.teamChatPrefix = teamChatPrefix;
        this.tabPriority = tabPriority;
        this.spawnLocation = spawnLocation;
        this.teamFlag = teamFlag;
        this.gameManager = gameManager;
    }

    public void captureEnemyFlag(Player player, Flag flag) {
        GameUtils.broadcastMessage(SMessages.ENEMY_FLAG_CAPTURE.getMessage(teamColor.getColor(), player.getName(), flag.getGameTeam().teamChatPrefix.getPrefix()));
        player.getInventory().setHelmet(null);
        addPoints(GamePoints.FLAG_CAPTURE.getPoints());
        Arena.getGamePlayer(player.getUniqueId()).removeFlag();
        gameManager.getArena().resetFlags();
    }

    public void addPlayerToTeam(GamePlayer gamePlayer, GameTeam team) {
        if (members.contains(gamePlayer)) return;
        members.add(gamePlayer);
        gamePlayer.setTeam(team);
    }

    public void removePlayerFromTeam(GamePlayer gamePlayer) {
        members.remove(gamePlayer);
    }

    public Collection<GamePlayer> getTeamPlayers() {
        return members;
    }

    public void spawnTeamFlagOnSpawn() {
        teamFlag.spawnFlagOnSpawn();
    }

    public void removeFlag() {
        teamFlag.removeFlag();
    }

    public void pickupFlag(Player player) {
        if (Arena.getGamePlayer(player.getUniqueId()).getTeam() == this) return;
        player.getInventory().setHelmet(new ItemStack(teamFlag.getFlagMaterial()));
        teamFlag.removeFlag();
        teamFlag.setFlagStatus(FlagStatus.STOLEN);
        Arena.getGamePlayer(player.getUniqueId()).giveFlag(teamFlag);
    }

    public void returnFlag(Player player) {
        teamFlag.removeFlag();
        teamFlag.spawnFlagOnSpawn();
        GameUtils.broadcastMessage(SMessages.FLAG_RETURNED.getMessage(teamColor.getColor(), player.getName(), teamChatPrefix.getPrefix()));
    }

    public void dropFlag(Player player) {
        teamFlag.spawnFlagOnDrop(player.getLocation().getBlock().getLocation());
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public TeamTabPrefix getTeamTabPrefix() {
        return teamTabPrefix;
    }

    public TeamChatPrefix getTeamChatPrefix() {
        return teamChatPrefix;
    }

    public int getTabPriority() {
        return tabPriority;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Flag getTeamFlag() {
        return teamFlag;
    }

    public void addPoints(int points) {
        if ((this.points + points) > GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) {
            this.points = GamePoints.POINTS_NEEDED_TO_WIN.getPoints();
            return;
        }
        this.points = (this.points + points);
    }

    public int getPoints() {
        return this.points;
    }
}
