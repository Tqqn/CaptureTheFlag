package com.tqqn.capturetheflag.game.powerups;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.nms.NMSArmorStand;
import com.tqqn.capturetheflag.nms.NMSItem;
import com.tqqn.capturetheflag.game.powerups.tasks.PowerUpCoolDownTask;
import com.tqqn.capturetheflag.utils.PluginSounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class PowerUp {

    private final ItemStack displayItem;
    private final String displayName;
    private Location location;
    private boolean isSpawned;
    private final Collection<NMSArmorStand> nmsArmorStands = new ArrayList<>();
    private final Collection<NMSItem> nmsItems = new ArrayList<>();
    private PowerUpCoolDownTask powerUpCoolDownTask;

    public PowerUp(ItemStack displayItem, String displayName, Location location) {
        this.displayItem = displayItem;
        this.displayName = displayName;
        this.location = location;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setPowerUp() {

        this.isSpawned = true;

        Bukkit.getOnlinePlayers().forEach(this::placePowerUp);
    }

    public void removePowerUp() {
        this.isSpawned = false;
        for (NMSArmorStand nmsArmorStand : nmsArmorStands) {
            nmsArmorStand.sendDestroyArmorStandPacketToPlayer();
        }

        for (NMSItem nmsItem : nmsItems) {
            nmsItem.sendDestroyItemPacketToPlayer();
        }

        nmsArmorStands.clear();
        nmsItems.clear();

        powerUpCoolDownTask = new PowerUpCoolDownTask();
        powerUpCoolDownTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20);
    }

    public boolean isPowerUpSpawned() {
        return isSpawned;
    }

    public void pickUp() {
        if (!isSpawned) return;
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5);

        Player nearestPlayer = null;

        if (entities.isEmpty()) return;


        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                if (nearestPlayer == null) {
                    nearestPlayer = player;
                }
                if (location.distance(player.getLocation()) > location.distance(nearestPlayer.getLocation())) {
                    nearestPlayer = player;
                }

                if (Arena.getGamePlayer(nearestPlayer.getUniqueId()).isSpectator()) return;

                givePowerUp(nearestPlayer);
                PluginSounds.POWERUP_PICKUP.playSound(nearestPlayer);
                removePowerUp();
            }
        }
    }

    private void placePowerUp(Player player) {

        NMSItem nmsItem = new NMSItem(displayItem, player);
        nmsItem.sendSpawnItemPacketToPlayer(location);
        nmsItems.add(nmsItem);

        NMSArmorStand nmsItemArmorStand = new NMSArmorStand(null);
        nmsItemArmorStand.sendSpawnArmorStandPacketToPlayer(player, location, true);
        nmsArmorStands.add(nmsItemArmorStand);

        NMSArmorStand nmsItemHologram = new NMSArmorStand(displayName);
        nmsItemHologram.sendSpawnArmorStandPacketToPlayer(player, new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ()), true);
        nmsArmorStands.add(nmsItemHologram);

        int[] entityIds = new int[]{nmsItem.getEntityId()};
        nmsItemArmorStand.sendMountPacket(player, entityIds);
    }

    public abstract void givePowerUp(Player player);
}
