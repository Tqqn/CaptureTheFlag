package com.tqqn.capturetheflag.data;

import com.tqqn.capturetheflag.flag.Flag;
import com.tqqn.capturetheflag.teams.GameTeam;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;

    private final Player player;
    private final String playerName;
    private GameTeam gameTeam = null;

    private boolean hasFlag = false;
    private Flag flag;

    private int kills = 0;
    private int assists = 0;

    public GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.playerName = player.getName();
    }

    public void setTeam(GameTeam gameTeam) {
        if (this.gameTeam != gameTeam) this.gameTeam = gameTeam;
    }

    public GameTeam getTeam() {
        return this.gameTeam;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void giveKitItems() {

    }

    public void addKill() {
        this.kills = (this.kills+1);
    }

    public void addAssist() {
        this.assists = (this.assists+1);
    }

    public int getKills() {
        return kills;
    }

    public int getAssists() {
        return assists;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void giveFlag(Flag flag) {
        hasFlag = true;
        this.flag = flag;
    }
    public void removeFlag() {
        hasFlag = false;
        this.flag = null;
    }

    public Flag getFlag() {
        return flag;
    }
}
