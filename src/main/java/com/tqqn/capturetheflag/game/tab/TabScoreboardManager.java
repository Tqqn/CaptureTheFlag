package com.tqqn.capturetheflag.game.tab;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.utils.NMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TabScoreboardManager {

    public static void setAllPlayersTabTeam(Collection<GamePlayer> gamePlayers) {
        for (GamePlayer gamePlayer : gamePlayers) {
            setPlayerTabTeam(gamePlayer, "");
        }
    }

    public static void setPlayerTabTeam(GamePlayer gamePlayer, String suffix) {
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            CaptureTheFlag.getReflectionLayer().sendNameTag(gamePlayer.getPlayer(), gamePlayer.getTeam().getTeamColor().getNMSColor(), gamePlayer.getTeam().getTeamTabPrefix().getPrefix(), suffix, allPlayers, gamePlayer.getTeam().getTabPriority());
        }
    }

    public static void setPlayerSpectatorTab(GamePlayer gamePlayer) {
        setPlayerTabTeam(gamePlayer, NMessages.TAB_DEATH_SUFFIX.getMessage());
    }
}
