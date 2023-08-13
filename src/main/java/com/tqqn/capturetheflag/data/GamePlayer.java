package com.tqqn.capturetheflag.data;

import com.tqqn.capturetheflag.teams.Team;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;

    private final Player player;
    private final String playerName;
    private Team team = null;

    public GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.playerName = player.getName();
    }

    public void setTeam(Team team) {
        if (this.team != team) this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void giveKitItems() {

    }
}
