package com.tqqn.capturetheflag.nms.framework;

import com.tqqn.capturetheflag.game.kits.AbstractKit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractNMSItemEntity {

    private final ItemStack itemStack;
    private final Player player;
    private int entityId = 0;
    private Object entityItem = null;

    public AbstractNMSItemEntity(ItemStack itemStack, Player player) {
        this.itemStack = itemStack;
        this.player = player;
    }

    public abstract void spawnItem(Location location);
    public abstract void removeItem();

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    public int getEntityId() {
        return entityId;
    }

    public Object getEntityItem() {
        return entityItem;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void setEntityItem(Object entityItem) {
        this.entityItem = entityItem;
    }
}
