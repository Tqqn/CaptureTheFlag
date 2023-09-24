package com.tqqn.capturetheflag.utils;

import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public enum PluginSounds {

    COUNTDOWN_SOUND(Sound.BLOCK_DISPENSER_FAIL, 0.5, 1.5),
    PLAYER_DEATH(Sound.ENTITY_CAT_AMBIENT, 0.5, 0.5),
    OWN_FLAG_STOLEN(Sound.ENTITY_VILLAGER_AMBIENT, 0.5, 0.1),
    OWN_FLAG_RETURNED(Sound.ENTITY_VILLAGER_CELEBRATE, 0.5, 1.5),
    OWN_FLAG_CAPTURED(Sound.ENTITY_WITHER_DEATH, 0.5, 0.1),
    ENEMY_FLAG_STOLEN(Sound.ENTITY_VILLAGER_AMBIENT, 0.5, 1.5),
    ENEMY_FLAG_RETURNED(Sound.ENTITY_VILLAGER_NO, 0.5, 0.5),
    ENEMY_FLAG_CAPTURED(Sound.ENTITY_WITHER_DEATH, 0.5, 3),
    START_GAME(Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5, 1.5),
    WIN_GAME(Sound.ENTITY_ENDER_DRAGON_AMBIENT, 0.5, 3),
    LOSE_GAME(Sound.ENTITY_PILLAGER_CELEBRATE, 0.5, 1.5);

    private final Sound sound;
    private final double volume;
    private final double pitch;

    PluginSounds(Sound sound, double volume, double pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public void playSound(Player player, Location location) {
        player.playSound(location, this.sound, (float) this.volume, (float) this.pitch);
    }

    public void playSound(Player player) {
        playSound(player, player.getLocation());
    }

    public void playSoundForAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playSound(player);
        }
    }

    public void playSoundToTeam(GameTeam gameTeam) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Arena.getGamePlayer(player.getUniqueId()).getTeam() == gameTeam) {
                playSound(player);
            }
        }
    }
}
