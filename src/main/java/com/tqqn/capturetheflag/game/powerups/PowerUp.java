package com.tqqn.capturetheflag.game.powerups;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.nms.NMSArmorStand;
import com.tqqn.capturetheflag.nms.NMSItem;
import com.tqqn.capturetheflag.game.powerups.tasks.PowerUpCoolDownTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PowerUp {

    private final ItemStack displayItem;
    private final String displayName;
    private Location location;
    private boolean isSpawned;

    private NMSItem nmsItem;
    private Collection<NMSArmorStand> nmsArmorStands = new ArrayList<>();
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
        nmsItem.sendDestroyItemPacketToPlayer();
        for (NMSArmorStand nmsArmorStand : nmsArmorStands) {
            nmsArmorStand.sendDestroyArmorStandPacketToPlayer();
        }
        nmsArmorStands.clear();
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
                removePowerUp();
            }
        }
    }

    private void placePowerUp(Player player) {
        nmsItem = new NMSItem(displayItem);
        nmsItem.sendSpawnItemPacketToPlayer(player, location);

        NMSArmorStand nmsItemArmorStand = new NMSArmorStand(null);
        nmsItemArmorStand.sendSpawnArmorStandPacketToPlayer(player, location, true);
        nmsArmorStands.add(nmsItemArmorStand);

        NMSArmorStand nmsItemHologram = new NMSArmorStand(displayName);
        nmsItemHologram.sendSpawnArmorStandPacketToPlayer(player, location, true);
        nmsArmorStands.add(nmsItemHologram);

        int[] entityIds = new int[]{nmsItem.getEntityId(), nmsItemHologram.getEntityId()};
        nmsItemArmorStand.sendMountPacket(player, entityIds);
    }

    public abstract void givePowerUp(Player player);
}
