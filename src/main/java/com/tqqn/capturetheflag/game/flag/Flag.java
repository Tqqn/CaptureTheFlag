package com.tqqn.capturetheflag.game.flag;

import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.nms.NMSArmorStand;
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

    private final Collection<NMSArmorStand> spawnedHolograms = new ArrayList<>();

    public Flag(String displayName, Location spawnLocation, Material flagMaterial, Particle.DustOptions dustOptions) {
        this.displayName = displayName;
        this.spawnLocation = spawnLocation;
        this.flagMaterial = flagMaterial;
        this.dustOptions = dustOptions;
    }

    public void spawnFlagOnSpawn() {
        flagStatus = FlagStatus.SAFE;
        spawnFlagHologram(spawnLocation);
        currentLocation = spawnLocation;

        spawnLocation.getBlock().setType(flagMaterial);
        GameManager.addSpawnedFlag(this);
    }

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
        System.out.println("Dropped flag at " + currentLocation);

        location.getBlock().setType(flagMaterial);
        GameManager.addSpawnedFlag(this);
    }

    public void removeFlag() {
        if (currentLocation != null) currentLocation.getBlock().setType(Material.AIR);
        spawnedHolograms.forEach(NMSArmorStand::sendDestroyArmorStandPacketToPlayer);
        spawnedHolograms.clear();
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

    public Particle.DustOptions getFlagDustOptions() {
        return dustOptions;
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
