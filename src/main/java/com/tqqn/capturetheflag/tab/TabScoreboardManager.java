package com.tqqn.capturetheflag.tab;

import com.tqqn.capturetheflag.data.GamePlayer;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.nms.packets.NMSSendNameTagPacket;
import com.tqqn.capturetheflag.utils.NMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

public class TabScoreboardManager {

    private final GameManager gameManager;

    public TabScoreboardManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    public void setAllPlayersTabTeam(Collection<GamePlayer> gamePlayers) {
        for (GamePlayer gamePlayer : gamePlayers) {
            setPlayerTabTeam(gamePlayer, "");
        }
    }

    public void setPlayerTabTeam(GamePlayer gamePlayer, String suffix) {
        for (Player allPlayers : Bukkit.getOnlinePlayers()) {
            NMSSendNameTagPacket.sendNameTagPlayers(gamePlayer.getPlayer(), gamePlayer.getTeam().getTeamColor().getNMSColor(), gamePlayer.getTeam().getTeamTabPrefix().getPrefix(), suffix, allPlayers, gamePlayer.getTeam().getTabPriority());
        }
    }

    public void setPlayerSpectatorTab(GamePlayer gamePlayer) {
        setPlayerTabTeam(gamePlayer, NMessages.TAB_DEATH_SUFFIX.getMessage());
    }

}
