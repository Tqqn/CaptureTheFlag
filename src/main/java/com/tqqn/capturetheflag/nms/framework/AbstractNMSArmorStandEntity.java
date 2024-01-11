package com.tqqn.capturetheflag.nms.framework;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class AbstractNMSArmorStandEntity {

    private String displayName = null;
    private int entityId = 0;
    private Object entityArmorStand = null;

    public AbstractNMSArmorStandEntity(String displayName) {
        if (displayName != null) {
            this.displayName = "{\"text\":\"" + displayName + "\"}";
        }
    }

    public abstract void spawnArmorStand(Player player, Location location, boolean shouldSmall);
    public abstract void removeArmorStand();
    public abstract void sendMountPacket(Player player, int[] entityIds);

    public String getDisplayName() {
        return displayName;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityArmorStand(Object entityArmorStand) {
        this.entityArmorStand = entityArmorStand;
    }

    public Object getEntityArmorStand() {
        return entityArmorStand;
    }
}
