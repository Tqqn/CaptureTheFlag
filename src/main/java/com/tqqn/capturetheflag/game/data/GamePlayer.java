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

    private final Player player;
    private boolean isSpectator = false;
    private GameTeam gameTeam = null;
    private final Set<GamePlayer> assistPlayers = new HashSet<>();
    AbstractKit abstractKit = null;
    private boolean hasFlag = false;
    private Flag flag;

    private int kills = 0;
    private int assists = 0;

    /**
     * Creates a GamePlayer Object
     * @param player Player
     */
    public GamePlayer(Player player) {
        this.player = player;
    }

    /**
     * Spawns the player at their spawn Location with a kit.
     * @param location Location
     */
    public void spawn(Location location) {
        if (isSpectator) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showPlayer(this.player);
            }
        }
        TabScoreboardManager.setPlayerTabTeam(this, "");
        player.teleport(location);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        giveKit();
        isSpectator = false;
    }

    /**
     * Sets the GameTeam of the GamePlayer.
     * @param gameTeam GameTeam
     */
    public void setTeam(GameTeam gameTeam) {
        if (this.gameTeam != gameTeam) this.gameTeam = gameTeam;
    }

    /**
     * Returns the GameTeam of the GamePlayer.
     */
    public GameTeam getTeam() {
        return this.gameTeam;
    }

    /**
     * Returns the Player Object of the GamePlayer.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Increases kills by 1 of the GamePlayer.
     */
    public void addKill() {
        this.kills = (this.kills+1);
    }

    /**
     * Increases assist by 1 of the GamePlayer.
     */
    public void addAssist() {
        this.assists = (this.assists+1);
    }

    /**
     * Returns the total kills the GamePlayer got.
     */
    public int getKills() {
        return kills;
    }

    /**
     * Returns the total assists the GamePlayer got.
     */
    public int getAssists() {
        return assists;
    }

    /**
     * Checks if the player has a flag.
     */
    public boolean hasFlag() {
        return hasFlag;
    }

    /**
     * Update hasFlag boolean variable to true and sets the hold Flag Object.
     */
    public void giveFlag(Flag flag) {
        hasFlag = true;
        this.flag = flag;
    }

    /**
     * Removes the flag object and sets the boolean to false.
     */
    public void removeFlag() {
        hasFlag = false;
        this.flag = null;
    }

    /**
     * Returns the Flag that GamePlayer has.
     */
    public Flag getFlag() {
        return flag;
    }

    /**
     * Give the GamePlayer the saved kit.
     */
    public void giveKit() {
        if (abstractKit == null) this.abstractKit = new WarriorKit();
        abstractKit.giveKit(player);
    }

    /**
     * Returns the kit the GamePlayer selected. If none is selected the default kit is WarriorKit.
     */
    public AbstractKit getKit() {
        if (abstractKit == null) this.abstractKit = new WarriorKit();
        return abstractKit;
    }

    /**
     * Sets the AbstractKit of the GamePlayer
     * @param abstractKit AbstractKit
     */
    public void setAbstractKit(AbstractKit abstractKit) {
        if (this.abstractKit == abstractKit) {
            player.sendMessage(NMessages.KIT_SAME_KIT.getMessage());
            return;
        }
        player.sendMessage(SMessages.KIT_CHOOSE.getMessage(abstractKit.getName()));
        this.abstractKit = abstractKit;
    }

    /**
     * Adds a other GamePlayer object to this GamePlayers AssistList.
     * @param gamePlayer GamePlayer
     */
    public void addPlayerToAssistList(GamePlayer gamePlayer) {
        assistPlayers.add(gamePlayer);
    }

    /**
     * Returns a Set of GamePlayers.
     */
    public Set<GamePlayer> getAssistPlayersSet() {
        return assistPlayers;
    }

    /**
     * Checks if the GamePlayer is a spectator.
     */
    public boolean isSpectator() {
        return isSpectator;
    }

    /**
     * Sets the GamePlayer in spectator mode.
     */
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
