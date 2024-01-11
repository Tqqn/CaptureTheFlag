package com.tqqn.capturetheflag.nms.v1_16_R3.objects;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.nms.framework.AbstractNMSItemEntity;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NMSItemEntity extends AbstractNMSItemEntity {

    public NMSItemEntity(ItemStack itemStack, Player player) {
        super(itemStack, player);
    }

    @Override
    public void spawnItem(Location location) {
        if (getEntityId() != 0) {
            removeItem();
        }

        EntityItem entityItem = new EntityItem(((CraftWorld)getPlayer().getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        net.minecraft.server.v1_16_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(getItemStack());

        setEntityId(entityItem.getId());
        setEntityItem(entityItem);

        entityItem.setItemStack(nmsItemStack);
        entityItem.setInvulnerable(true);

        CaptureTheFlag.getReflectionLayer().sendPacket(getPlayer(), new PacketPlayOutSpawnEntity(entityItem, 1));
        CaptureTheFlag.getReflectionLayer().sendPacket(getPlayer(), new PacketPlayOutEntityMetadata(getEntityId(), entityItem.getDataWatcher(), true));
    }

    @Override
    public void removeItem() {
        int[] entityIds = new int[]{getEntityId()};
        CaptureTheFlag.getReflectionLayer().sendPacket(getPlayer(), new PacketPlayOutEntityDestroy(entityIds));
        setEntityId(0);
        setEntityItem(null);
    }
}
