package com.tqqn.capturetheflag.nms.v1_16_R3.objects;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.nms.framework.AbstractNMSArmorStandEntity;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class NMSArmorStand extends AbstractNMSArmorStandEntity {


    public NMSArmorStand(String displayName) {
        super(displayName);
    }

    @Override
    public void spawnArmorStand(Player player, Location location, boolean shouldSmall) {
        EntityArmorStand entityArmorStand = new EntityArmorStand(((CraftWorld)player.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());

        setEntityId(entityArmorStand.getId());

        entityArmorStand.setInvisible(true);
        entityArmorStand.setInvulnerable(true);
        entityArmorStand.setNoGravity(true);

        if (getDisplayName() != null) {
            entityArmorStand.setCustomName(IChatBaseComponent.ChatSerializer.a(getDisplayName()));
            entityArmorStand.setCustomNameVisible(true);
        }

        if (shouldSmall) {
            entityArmorStand.setSmall(true);
        }

        setEntityArmorStand(entityArmorStand);

        CaptureTheFlag.getReflectionLayer().sendPacket(player, new PacketPlayOutSpawnEntityLiving(entityArmorStand));
        CaptureTheFlag.getReflectionLayer().sendPacket(player, new PacketPlayOutEntityMetadata(getEntityId(), entityArmorStand.getDataWatcher(), true));
    }

    @Override
    public void removeArmorStand() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            int[] entityIds = new int[]{getEntityId()};
            CaptureTheFlag.getReflectionLayer().sendPacket(player, new PacketPlayOutEntityDestroy(entityIds));
        });
    }

    @Override
    public void sendMountPacket(Player player, int[] entityIds) {
        PacketPlayOutMount packetPlayOutMount = new PacketPlayOutMount((Entity) getEntityArmorStand());
        try {
            Object packetPlayOutMount2 = packetPlayOutMount.getClass().getConstructor(Entity.class).newInstance(getEntityArmorStand());

            CaptureTheFlag.getReflectionLayer().setField(packetPlayOutMount2, CaptureTheFlag.getReflectionLayer().getField(packetPlayOutMount2.getClass(), "b"), entityIds);
            CaptureTheFlag.getReflectionLayer().sendPacket(player, packetPlayOutMount2);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
