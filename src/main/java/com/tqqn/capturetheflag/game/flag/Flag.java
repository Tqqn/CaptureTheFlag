package com.tqqn.capturetheflag.game.flag;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.nms.framework.AbstractNMSArmorStandEntity;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Flag {

    private final String displayName;
    private GameTeam gameTeam;
    private final Location spawnLocation;
    private final Material flagMaterial;
    private final Particle.DustOptions dustOptions;
    private FlagStatus flagStatus = FlagStatus.SAFE;
    private Location currentLocation;

    private final Collection<AbstractNMSArmorStandEntity> spawnedHolograms = new ArrayList<>();

    /**
     * Creates a Flag Object.
     * @param displayName String
     * @param spawnLocation Location
     * @param flagMaterial Material
     * @param dustOptions DustOptions
     */
    public Flag(String displayName, Location spawnLocation, Material flagMaterial, Particle.DustOptions dustOptions) {
        this.displayName = displayName;
        this.spawnLocation = spawnLocation;
        this.flagMaterial = flagMaterial;
        this.dustOptions = dustOptions;
    }

    /**
     * Spawns the Flag at the given spawn location.
     */
    public void spawnFlagOnSpawn() {
        flagStatus = FlagStatus.SAFE;
        spawnFlagHologram(spawnLocation);
        currentLocation = spawnLocation;

        spawnLocation.getBlock().setType(flagMaterial);
        GameManager.addSpawnedFlag(this);
    }

    /**
     * Spawns the flag at the given location. Calculates if the flag can spawn.
     * @param location Location
     * @param gamePlayer GamePlayer
     */
    public void spawnFlagOnDrop(Location location, GamePlayer gamePlayer) {
        if (location.getY() <= -1) {
            spawnFlagOnSpawn();
            GameUtils.broadcastMessage(SMessages.FLAG_SPAWNED_BECAUSE_OF_VOID.getMessage(displayName, gamePlayer.getPlayer().getName()));
            return;
        }

        if (!(location.getY()+1 != location.getWorld().getHighestBlockYAt(location)+1)) {
            location.setY(location.getWorld().getHighestBlockYAt(location)+1);
        }

        if (location.getBlock().getType() != Material.AIR) {
            location.setY(location.getY()+1);
        }

        if (location.getWorld().getHighestBlockYAt(location) == -1) {
            spawnFlagOnSpawn();
            GameUtils.broadcastMessage(SMessages.FLAG_SPAWNED_BECAUSE_OF_VOID.getMessage(displayName, gamePlayer.getPlayer().getName()));
            return;
        }


        currentLocation = location;

        flagStatus = FlagStatus.DROPPED;
        spawnFlagHologram(currentLocation);

        location.getBlock().setType(flagMaterial);
        GameManager.addSpawnedFlag(this);
    }

    /**
     * Removes the flag from its current location.
     */
    public void removeFlag() {
        if (currentLocation != null) currentLocation.getBlock().setType(Material.AIR);
        spawnedHolograms.forEach(AbstractNMSArmorStandEntity::removeArmorStand);
        spawnedHolograms.clear();
        GameManager.removeSpawnedFlag(this);
    }

    /**
     * Spawns a name hologram at the given location.
     * @param location Location
     */
    private void spawnFlagHologram(Location location) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (Arena.getGamePlayer(players.getUniqueId()).getTeam() == gameTeam) {
                AbstractNMSArmorStandEntity flagHologram;
                if (flagStatus == FlagStatus.DROPPED) {
                    flagHologram = CaptureTheFlag.getReflectionLayer().createNMSArmorStandEntity(NMessages.FLAG_ITEM_HOLOGRAM_RETURN.getMessage());
                } else {
                    flagHologram = CaptureTheFlag.getReflectionLayer().createNMSArmorStandEntity(NMessages.FLAG_ITEM_HOLOGRAM_SELF.getMessage());
                }
                flagHologram.spawnArmorStand(players, location, false);
                spawnedHolograms.add(flagHologram);

            } else {
                AbstractNMSArmorStandEntity flagHologram = CaptureTheFlag.getReflectionLayer().createNMSArmorStandEntity(NMessages.FLAG_ITEM_HOLOGRAM_ENEMY.getMessage());
                flagHologram.spawnArmorStand(players, location, false);
                spawnedHolograms.add(flagHologram);
            }
        }
    }

    /**
     * Returns the displayName of the flag.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the GameTeam the flag belongs.
     */
    public GameTeam getGameTeam() {
        return gameTeam;
    }

    /**
     * Returns the flag material.
     */
    public Material getFlagMaterial() {
        return flagMaterial;
    }

    /**
     * Returns the flag DustOptions.
     */
    public Particle.DustOptions getFlagDustOptions() {
        return dustOptions;
    }

    /**
     * Returns the flag status.
     */
    public FlagStatus getFlagStatus() {
        return flagStatus;
    }

    /**
     * Returns the current location of the flag.
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Updates the current location to the given location.
     * @param currentLocation Location
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Updates the flag status.
     * @param flagStatus FlagStatus
     */
    public void setFlagStatus(FlagStatus flagStatus) {
        this.flagStatus = flagStatus;
    }

    /**
     * Sets the GameTeam of the flag.
     * @param team GameTeam
     */
    public void setGameTeam(GameTeam team) {
        this.gameTeam = team;
    }
}
