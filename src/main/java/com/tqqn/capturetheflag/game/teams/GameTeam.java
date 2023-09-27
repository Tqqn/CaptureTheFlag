package com.tqqn.capturetheflag.game.teams;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.data.GamePoints;
import com.tqqn.capturetheflag.game.flag.Flag;
import com.tqqn.capturetheflag.game.flag.FlagStatus;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.FlagCarrierTask;
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

    /**
     * Creates a GameTeam Object.
     * @param displayName String
     * @param teamColor TeamColor
     * @param teamTabPrefix TeamTabPrefix
     * @param teamChatPrefix TeamChatPrefix
     * @param tabPriority int
     * @param spawnLocation Location
     * @param teamFlag Flag
     * @param gameManager GameManager
     */
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

    /**
     * Captures the enemy flag, gives the team points and resets the flags.
     * @param player Player
     * @param flag Flag
     */
    public void captureEnemyFlag(Player player, Flag flag) {
        GameUtils.broadcastMessage(SMessages.ENEMY_FLAG_CAPTURE.getMessage(teamColor.getColor(), player.getName(), flag.getGameTeam().teamChatPrefix.getPrefix()));
        player.getInventory().setHelmet(null);
        addPoints(GamePoints.FLAG_CAPTURE.getPoints());
        Arena.getGamePlayer(player.getUniqueId()).removeFlag();
        gameManager.getArena().resetFlags();
    }

    /**
     * Adds player to this team.
     * @param gamePlayer GamePlayer
     */
    public void addPlayerToTeam(GamePlayer gamePlayer) {
        if (members.contains(gamePlayer)) return;
        members.add(gamePlayer);
        gamePlayer.setTeam(this);
    }

    /**
     * Removes player from this team.
     * @param gamePlayer GamePlayer
     */
    public void removePlayerFromTeam(GamePlayer gamePlayer) {
        members.remove(gamePlayer);
    }

    /**
     * Returns all players in this team.
     */
    public Collection<GamePlayer> getTeamPlayers() {
        return members;
    }

    /**
     * Spawns the flag on this teams flag spawn point.
     */
    public void spawnTeamFlagOnSpawn() {
        teamFlag.spawnFlagOnSpawn();
    }

    /**
     * Removes the flag.
     */
    public void removeFlag() {
        teamFlag.removeFlag();
    }

    /**
     * Removes the flag and gives it to the given player.
     * @param player Player
     */
    public void pickupFlag(Player player) {
        if (Arena.getGamePlayer(player.getUniqueId()).getTeam() == this) return;
        player.getInventory().setHelmet(new ItemStack(teamFlag.getFlagMaterial()));
        teamFlag.removeFlag();
        teamFlag.setFlagStatus(FlagStatus.STOLEN);
        Arena.getGamePlayer(player.getUniqueId()).giveFlag(teamFlag);
        GameUtils.broadcastMessage(SMessages.FLAG_STOLE.getMessage(Arena.getGamePlayer(player.getUniqueId()).getTeam().teamColor.getColor(), player.getName(), teamChatPrefix.getPrefix()));
        FlagCarrierTask flagCarrierTask = new FlagCarrierTask(teamFlag, Arena.getGamePlayer(player.getUniqueId()));
        flagCarrierTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 10L);
    }

    /**
     * Removes the flag and spawns the flag at its spawn.
     * @param player Player
     */
    public void returnFlag(Player player) {
        teamFlag.removeFlag();
        teamFlag.spawnFlagOnSpawn();
        GameUtils.broadcastMessage(SMessages.FLAG_RETURNED.getMessage(teamColor.getColor(), player.getName(), teamChatPrefix.getPrefix()));
    }

    /**
     * Drops the flag on the location of the player.
     * @param player Player
     */
    public void dropFlag(Player player) {
        GameUtils.broadcastMessage(SMessages.FLAG_DROPPED.getMessage(teamColor.getColor(), player.getName(), teamChatPrefix.getPrefix()));
        teamFlag.spawnFlagOnDrop(player.getLocation().getBlock().getLocation(), Arena.getGamePlayer(player.getUniqueId()));
    }

    /**
     * Returns the DisplayName of the team.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Returns the TeamColor of the team.
     */
    public TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * Returns the TeamTabPrefix of the team.
     */
    public TeamTabPrefix getTeamTabPrefix() {
        return teamTabPrefix;
    }

    /**
     * Returns the tab priority int of the team.
     */
    public int getTabPriority() {
        return tabPriority;
    }

    /**
     * Returns the spawnLocation of this team.
     */
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    /**
     * Returns the flag of this team.
     */
    public Flag getTeamFlag() {
        return teamFlag;
    }

    /**
     * Increases the points of this team.
     * @param points Integer
     */
    public void addPoints(int points) {
        if ((this.points + points) > GamePoints.POINTS_NEEDED_TO_WIN.getPoints()) {
            this.points = GamePoints.POINTS_NEEDED_TO_WIN.getPoints();
            return;
        }
        this.points = (this.points + points);
    }

    /**
     * Returns the current points of this team.
     */
    public int getPoints() {
        return this.points;
    }
}
