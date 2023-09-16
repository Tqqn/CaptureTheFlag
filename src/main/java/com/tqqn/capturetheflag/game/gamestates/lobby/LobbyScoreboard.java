package com.tqqn.capturetheflag.game.gamestates.lobby;

import com.tqqn.capturetheflag.utils.PluginScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyScoreboard extends BukkitRunnable implements PluginScoreboard {

    @Override
    public void createNewScoreboard(Player player) {

    }

    @Override
    public void updateScoreboard(Player player) {

    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard() != null && player.getScoreboard().getObjective("lobbyBoard") != null) {
                updateScoreboard(player);
            } else {
                createNewScoreboard(player);
            }
        }
    }
}
