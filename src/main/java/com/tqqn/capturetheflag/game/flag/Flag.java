package com.tqqn.capturetheflag.game.flag;

import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.nms.NMSArmorStand;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.utils.NMessages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Flag {

    private final String displayName;
    private GameTeam gameTeam;
    private final Location spawnLocation;
    private final Material flagMaterial;
    private FlagStatus flagStatus = FlagStatus.SAFE;
    private Location currentLocation;

    private final Collection<NMSArmorStand> spawnedHolograms = new ArrayList<>();

    public Flag(String displayName, Location spawnLocation, Material flagMaterial) {
        this.displayName = displayName;
        this.spawnLocation = spawnLocation;
        this.flagMaterial = flagMaterial;
    }

    public void spawnFlagOnSpawn() {
        flagStatus = FlagStatus.SAFE;
        spawnFlagHologram(spawnLocation);
        currentLocation = spawnLocation;

        spawnLocation.getBlock().setType(flagMaterial);
        GameManager.addSpawnedFlag(this);
    }

    public void spawnFlagOnDrop(Location location) {
        location.setY(location.getWorld().getHighestBlockYAt(location)+1);
        currentLocation = location;

        flagStatus = FlagStatus.DROPPED;
        spawnFlagHologram(currentLocation);

        location.getBlock().setType(flagMaterial);
        GameManager.addSpawnedFlag(this);
    }

    public void removeFlag() {
        if (currentLocation != null) currentLocation.getBlock().setType(Material.AIR);
        spawnedHolograms.forEach(NMSArmorStand::sendDestroyArmorStandPacketToPlayer);
        spawnedHolograms.clear();
        currentLocation = null;
        GameManager.removeSpawnedFlag(this);
    }

    private void spawnFlagHologram(Location location) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (Arena.getGamePlayer(players.getUniqueId()).getTeam() == gameTeam) {
                NMSArmorStand flagHologram;
                if (flagStatus == FlagStatus.DROPPED) {
                    flagHologram = new NMSArmorStand(NMessages.FLAG_ITEM_HOLOGRAM_RETURN.getMessage());
                } else {
                    flagHologram = new NMSArmorStand(NMessages.FLAG_ITEM_HOLOGRAM_SELF.getMessage());
                }
                flagHologram.sendSpawnArmorStandPacketToPlayer(players, location, false);
                spawnedHolograms.add(flagHologram);

            } else {
                NMSArmorStand flagHologram = new NMSArmorStand(NMessages.FLAG_ITEM_HOLOGRAM_ENEMY.getMessage());
                flagHologram.sendSpawnArmorStandPacketToPlayer(players, location, false);
                spawnedHolograms.add(flagHologram);
            }
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public GameTeam getGameTeam() {
        return gameTeam;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Material getFlagMaterial() {
        return flagMaterial;
    }

    public FlagStatus getFlagStatus() {
        return flagStatus;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setFlagStatus(FlagStatus flagStatus) {
        this.flagStatus = flagStatus;
    }
    public void setGameTeam(GameTeam team) {
        this.gameTeam = team;
    }
}
