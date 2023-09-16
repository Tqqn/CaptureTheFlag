package com.tqqn.capturetheflag.game.data;

import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.flag.Flag;
import com.tqqn.capturetheflag.game.kits.AbstractKit;
import com.tqqn.capturetheflag.game.kits.types.WarriorKit;
import com.tqqn.capturetheflag.game.tab.TabScoreboardManager;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GamePlayer {

    private final UUID uuid;

    private final Player player;
    private final String playerName;
    private boolean isSpectator = false;
    private GameTeam gameTeam = null;
    private final Set<GamePlayer> assistPlayers = new HashSet<>();

    AbstractKit abstractKit = null;

    private boolean hasFlag = false;
    private Flag flag;

    private int kills = 0;
    private int assists = 0;

    public GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.playerName = player.getName();
    }

    public void spawn(Location location) {
        if (isSpectator = true) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(this.player);
            }
        }
        TabScoreboardManager.setPlayerTabTeam(this, "");
        player.setFlying(false);
        player.setAllowFlight(false);
        player.teleport(location);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        giveKit();
        isSpectator = false;
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

    public void giveKit() {
        if (abstractKit == null) this.abstractKit = new WarriorKit();
        abstractKit.giveKit(player);
    }

    public void setAbstractKit(AbstractKit abstractKit) {
        if (this.abstractKit == abstractKit) {
            player.sendMessage(NMessages.KIT_SAME_KIT.getMessage());
            return;
        }
        player.sendMessage(SMessages.KIT_CHOOSE.getMessage(abstractKit.getName()));
        this.abstractKit = abstractKit;
    }

    public void addPlayerToAssistList(GamePlayer gamePlayer) {
        assistPlayers.add(gamePlayer);
    }

    public Set<GamePlayer> getAssistPlayersSet() {
        return assistPlayers;
    }

    public boolean isSpectator() {
        return isSpectator;
    }


    public void setSpectator() {
        TabScoreboardManager.setPlayerSpectatorTab(this);

        isSpectator = true;
        player.setAllowFlight(true);
        player.setFlying(true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20000000, 1));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!(Arena.getGamePlayer(player.getUniqueId()).getTeam() == gameTeam)) {
                player.hidePlayer(this.player);
            }
        }
    }
}
