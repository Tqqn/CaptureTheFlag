package com.tqqn.capturetheflag.game.gamestates.lobby;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.gamestates.lobby.tasks.LobbyWaitingGameTask;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.PluginScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class LobbyScoreboard extends BukkitRunnable implements PluginScoreboard {

    private final GameManager gameManager;

    public LobbyScoreboard(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private final String teamPlayerCountTeam = "teamPlayerCount";
    private final String teamPlayerCountKey = ChatColor.BLUE + "";
    private final String teamStartingTimeTeam = "teamStartingTime";
    private final String teamStartingTimeKey = ChatColor.RED + "";

    private final String teamPlayersNeededTeam = "teamPlayersNeed";
    private final String teamPlayersNeededKey = ChatColor.AQUA + "";

    private final String teamKitSelectedTeam = "teamKitSelected";
    private final String teamKitSelectedKey = ChatColor.BLACK + "";

    @Override
    public void run() {
        if (GameManager.getGameStates() != GameStates.LOBBY) {
            cancel();
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard().getObjective("lobbyBoard") != null) {
                updateScoreboard(player);
            } else {
                createNewScoreboard(player);
            }
        }
    }

    @Override
    public void createNewScoreboard(Player player) {
        GamePlayer gamePlayer = Arena.getGamePlayer(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("lobbyBoard", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(NMessages.GAME_SCOREBOARD_TITLE.getMessage());

        Team playerCount = scoreboard.registerNewTeam(teamPlayerCountTeam);

        playerCount.addEntry(teamPlayerCountKey);
        playerCount.setPrefix(GameUtils.translateColor("&fPlayers: "));
        playerCount.setSuffix(GameUtils.translateColor(gameManager.getArena().getInGamePlayers().size() + "/" + gameManager.getArena().getMaximumPlayers()));

        Team startingTime = scoreboard.registerNewTeam(teamStartingTimeTeam);
        startingTime.addEntry(teamStartingTimeKey);
        startingTime.setPrefix(GameUtils.translateColor("Starting in "));
        startingTime.setSuffix(GameUtils.translateColor("&b" + GameUtils.convertSecondsToHMmSs(LobbyWaitingGameTask.getWaitingTime()) + " &rif"));

        Team playersNeeded = scoreboard.registerNewTeam(teamPlayersNeededTeam);
        playersNeeded.addEntry(teamPlayersNeededKey);
        playersNeeded.setPrefix(GameUtils.translateColor("&b" + gameManager.getArena().getPlayersToStart()));
        playersNeeded.setSuffix(GameUtils.translateColor(" &rmore players join"));

        Team kitSelected = scoreboard.registerNewTeam(teamKitSelectedTeam);
        kitSelected.addEntry(teamKitSelectedKey);
        kitSelected.setPrefix(GameUtils.translateColor(gamePlayer.getKit().getName()));

        objective.getScore(ChatColor.WHITE + " ").setScore(9);
        objective.getScore(teamPlayerCountKey).setScore(8);
        objective.getScore(ChatColor.DARK_RED + " ").setScore(7);
        objective.getScore(teamStartingTimeKey).setScore(6);
        objective.getScore(teamPlayersNeededKey).setScore(5);
        objective.getScore(ChatColor.DARK_AQUA + " ").setScore(4);
        objective.getScore(GameUtils.translateColor("&fSelected Kit:")).setScore(3);
        objective.getScore(teamKitSelectedKey).setScore(2);
        objective.getScore(ChatColor.DARK_RED + " ").setScore(1);

        player.setScoreboard(scoreboard);
    }

    @Override
    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        GamePlayer gamePlayer = Arena.getGamePlayer(player.getUniqueId());

        Team playerCount = scoreboard.getTeam(teamPlayerCountTeam);
        playerCount.setSuffix(GameUtils.translateColor(gameManager.getArena().getInGamePlayers().size() + "/" + gameManager.getArena().getMaximumPlayers()));

        Team startingTime = scoreboard.getTeam(teamStartingTimeTeam);
        startingTime.setSuffix(GameUtils.translateColor("&b" + GameUtils.convertSecondsToHMmSs(LobbyWaitingGameTask.getWaitingTime()) + " &rif"));

        Team playersNeeded = scoreboard.getTeam(teamPlayersNeededTeam);
        playersNeeded.setPrefix(GameUtils.translateColor("&b" + gameManager.getArena().getPlayersToStart()));

        Team kitSelected = scoreboard.getTeam(teamKitSelectedTeam);
        kitSelected.setPrefix(GameUtils.translateColor(gamePlayer.getKit().getName()));
    }
}
