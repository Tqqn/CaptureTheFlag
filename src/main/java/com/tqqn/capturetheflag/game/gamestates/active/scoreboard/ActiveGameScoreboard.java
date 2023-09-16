package com.tqqn.capturetheflag.game.gamestates.active.scoreboard;

import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.data.GamePoints;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.PluginScoreboard;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ActiveGameScoreboard extends BukkitRunnable implements PluginScoreboard {

    private final GameManager gameManager;
    private final String teamBluePointsTeam = "teamBluePoints";
    private final String teamBluePointsKey = ChatColor.BLUE + "";

    private final String teamRedPointsTeam = "teamRedPoints";
    private final String teamRedPointsKey = ChatColor.RED + "";

    private final String timeLeftTeam = "timeLeft";
    private final String timeLeftKey = ChatColor.DARK_AQUA + "";

    private final String blueFlagStatusTeam = "blueFlagStatus";
    private final String blueFlagStatusKey = ChatColor.DARK_BLUE + "";

    private final String redFlagStatusTeam = "redFlagStatus";
    private final String redFlagStatusKey = ChatColor.DARK_GRAY + "";

    private final String killsAssistsTeam = "killsAssists";
    private final String killsAssistsKey = ChatColor.DARK_PURPLE + "";

    public ActiveGameScoreboard(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard() != null && player.getScoreboard().getObjective("activeBoard") != null) {
                updateScoreboard(player);
            } else {
                createNewScoreboard(player);
            }
        }
    }
    @Override
    public void createNewScoreboard(Player player) {
        GamePlayer gamePlayer = gameManager.getArena().getGamePlayer(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("activeBoard", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(NMessages.ACTIVE_SCOREBOARD_TITLE.getMessage());

        Team teamBluePoints = scoreboard.registerNewTeam(teamBluePointsTeam);

        teamBluePoints.addEntry(teamBluePointsKey);
        teamBluePoints.setPrefix(GameUtils.translateColor(gameManager.getTeamBlue().getDisplayName() + ": "));
        teamBluePoints.setSuffix(GameUtils.translateColor("&b" + gameManager.getTeamBlue().getPoints() + "&6/" + GamePoints.POINTS_NEEDED_TO_WIN.getPoints()));

        Team teamRedPoints = scoreboard.registerNewTeam(teamRedPointsTeam);
        teamRedPoints.addEntry(teamRedPointsKey);
        teamRedPoints.setPrefix(GameUtils.translateColor(gameManager.getTeamRed().getDisplayName() + ": "));
        teamRedPoints.setSuffix(GameUtils.translateColor("&b" + gameManager.getTeamRed().getPoints() + "&6/" + GamePoints.POINTS_NEEDED_TO_WIN.getPoints()));

        Team timeLeft = scoreboard.registerNewTeam(timeLeftTeam);
        timeLeft.addEntry(timeLeftKey);
        timeLeft.setPrefix(GameUtils.translateColor("&fTime Left: "));
        timeLeft.setSuffix(GameUtils.translateColor("&a" + GameUtils.convertSecondsToHMmSs(gameManager.getActiveGameTaskTime())));

        Team blueFlagStatus = scoreboard.registerNewTeam(blueFlagStatusTeam);

        blueFlagStatus.addEntry(blueFlagStatusKey);
        blueFlagStatus.setPrefix(GameUtils.translateColor(gameManager.getTeamBlue().getTeamFlag().getDisplayName() + ": "));
        blueFlagStatus.setSuffix(GameUtils.translateColor(gameManager.getTeamBlue().getTeamFlag().getFlagStatus().getStatusName()));

        Team redFlagStatus = scoreboard.registerNewTeam(redFlagStatusTeam);

        redFlagStatus.addEntry(redFlagStatusKey);
        redFlagStatus.setPrefix(GameUtils.translateColor(gameManager.getTeamRed().getTeamFlag().getDisplayName() + ": "));
        redFlagStatus.setSuffix(GameUtils.translateColor(gameManager.getTeamRed().getTeamFlag().getFlagStatus().getStatusName()));

        Team killsAssists = scoreboard.registerNewTeam(killsAssistsTeam);

        killsAssists.addEntry(killsAssistsKey);
        killsAssists.setPrefix("");
        killsAssists.setSuffix(SMessages.SCOREBOARD_KILLS_ASSISTS.getMessage(String.valueOf(gamePlayer.getKills()), String.valueOf(gamePlayer.getAssists())));

        objective.getScore(ChatColor.WHITE + " ").setScore(10);
        objective.getScore(teamBluePointsKey).setScore(9);
        objective.getScore(teamRedPointsKey).setScore(8);
        objective.getScore(ChatColor.DARK_BLUE + " ").setScore(7);
        objective.getScore(timeLeftKey).setScore(6);
        objective.getScore(ChatColor.DARK_AQUA + " ").setScore(5);
        objective.getScore(blueFlagStatusKey).setScore(4);
        objective.getScore(redFlagStatusKey).setScore(3);
        objective.getScore(ChatColor.AQUA + " ").setScore(2);
        objective.getScore(killsAssistsKey).setScore(1);
        objective.getScore(ChatColor.GOLD + " ").setScore(0);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        GamePlayer gamePlayer = gameManager.getArena().getGamePlayer(player.getUniqueId());

        Team teamBluePoints = scoreboard.getTeam(teamBluePointsTeam);
        teamBluePoints.setSuffix(GameUtils.translateColor("&b" + gameManager.getTeamBlue().getPoints() + "&6/" + GamePoints.POINTS_NEEDED_TO_WIN.getPoints()));

        Team teamRedPoints = scoreboard.getTeam(teamRedPointsTeam);
        teamRedPoints.setSuffix(GameUtils.translateColor("&b" + gameManager.getTeamRed().getPoints() + "&6/" + GamePoints.POINTS_NEEDED_TO_WIN.getPoints()));

        Team timeLeft = scoreboard.getTeam(timeLeftTeam);
        if (GameManager.getGameStates() != GameStates.ACTIVE) {
            timeLeft.setSuffix(GameUtils.translateColor("&7&lEnded!"));
        } else {
            timeLeft.setSuffix(GameUtils.translateColor("&a" + GameUtils.convertSecondsToHMmSs(gameManager.getActiveGameTaskTime())));
        }


        Team blueFlagStatus = scoreboard.getTeam(blueFlagStatusTeam);
        blueFlagStatus.setSuffix(GameUtils.translateColor(gameManager.getTeamBlue().getTeamFlag().getFlagStatus().getStatusName()));

        Team redFlagStatus = scoreboard.getTeam(redFlagStatusTeam);
        redFlagStatus.setSuffix(GameUtils.translateColor(gameManager.getTeamRed().getTeamFlag().getFlagStatus().getStatusName()));

        Team killsAssists = scoreboard.getTeam(killsAssistsTeam);
        killsAssists.setSuffix(SMessages.SCOREBOARD_KILLS_ASSISTS.getMessage(String.valueOf(gamePlayer.getKills()), String.valueOf(gamePlayer.getAssists())));
    }
}
