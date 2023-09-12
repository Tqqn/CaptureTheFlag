package com.tqqn.capturetheflag.nms;

import com.tqqn.capturetheflag.utils.GameUtils;
import net.minecraft.server.v1_16_R3.EntityItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSArmorStand {

    private String displayName = null;
    private int entityId = 0;

    Class<?> packetPlayOutSpawnEntityLivingClass = NMSUtils.getNMSClass("PacketPlayOutSpawnEntityLiving");
    Class<?> packetPlayOutEntityMetaClass = NMSUtils.getNMSClass("PacketPlayOutEntityMetadata");
    Class<?> packetPlayOutEntityDestroyClass = NMSUtils.getNMSClass("PacketPlayOutEntityDestroy");
    Class<?> packetPlayOutMountClass = NMSUtils.getNMSClass("PacketPlayOutMount");
    Class<?> entityLivingClass = NMSUtils.getNMSClass("EntityLiving");
    Class<?> entityClass = NMSUtils.getNMSClass("Entity");

    Class<?> entityItemClass = NMSUtils.getNMSClass("EntityItem");
    Class<?> iChatBaseCompClass = NMSUtils.getNMSClass("IChatBaseComponent");
    Class<?> craftWorldClass = NMSUtils.getCraftBukkitClass("CraftWorld");
    Class<?> NMSWorldClass = NMSUtils.getNMSClass("World");

    Object displayNameChatSerializer;
    Object entityArmorStand;

    public NMSArmorStand(String displayName) {
        if (displayName != null) {
            this.displayName = "{\"text\":\"" + displayName + "\"}";
        }
    }

    public void sendSpawnArmorStandPacketToAllPlayers(Location location) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendSpawnArmorStandPacketToPlayer(player, location, false);
        }
    }

    public void sendSpawnArmorStandPacketToPlayer(Player player, Location location, boolean shouldSmall) {

        try {
            if (entityId != 0) {
                sendDestroyArmorStandPacketToPlayer();
            }

            Object craftWorldObj = craftWorldClass.cast(location.getWorld());
            Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle");

            Constructor<?> entityArmorStandConstructor = NMSUtils.getNMSClass("EntityArmorStand").getConstructor(NMSWorldClass, double.class, double.class, double.class);
            Object entityArmorStand = entityArmorStandConstructor.newInstance(getHandleMethod.invoke(craftWorldObj), location.getX(), location.getY(), location.getZ());

            Method getDataWatcher = entityLivingClass.getMethod("getDataWatcher");
            Object dataWatcherObject = getDataWatcher.invoke(entityArmorStand);

            Method getId = entityLivingClass.getMethod("getId");
            entityId = (int) getId.invoke(entityArmorStand);

            // META

            Method setInvisible = entityArmorStand.getClass().getMethod("setInvisible", boolean.class);
            Method setNoGravity = entityArmorStand.getClass().getMethod("setNoGravity", boolean.class);

            if (displayName != null) {
                Method a = NMSUtils.getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
                displayNameChatSerializer = a.invoke(iChatBaseCompClass, GameUtils.translateColor(displayName));
                Method setCustomName = entityArmorStand.getClass().getMethod("setCustomName", iChatBaseCompClass);
                setCustomName.invoke(entityArmorStand, displayNameChatSerializer);
                Method setCustomNameVisible = entityArmorStand.getClass().getMethod("setCustomNameVisible", boolean.class);
                setCustomNameVisible.invoke(entityArmorStand, true);
            }

            if (shouldSmall) {
                Method setSmall = entityArmorStand.getClass().getMethod("setSmall", boolean.class);
                setSmall.invoke(entityArmorStand, true);
            }

            setInvisible.invoke(entityArmorStand, true);
            setNoGravity.invoke(entityArmorStand, true);

            this.entityArmorStand = entityArmorStand;
            Constructor<?> packetPlayOutSpawnEntityLivingConstructor = packetPlayOutSpawnEntityLivingClass.getConstructor(entityLivingClass);
            Constructor<?> packetPlayOutEntityMetaDataConstructor = packetPlayOutEntityMetaClass.getConstructor(int.class, dataWatcherObject.getClass(), boolean.class);

            NMSUtils.sendPacket(player, packetPlayOutSpawnEntityLivingConstructor.newInstance(entityArmorStand));
            NMSUtils.sendPacket(player, packetPlayOutEntityMetaDataConstructor.newInstance(entityId, dataWatcherObject, true));

        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public void sendDestroyArmorStandPacketToPlayer() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Constructor<?> packetPlayOutEntityDestroyConstructor = packetPlayOutEntityDestroyClass.getConstructor(int[].class);
                int[] entityIds = new int[]{entityId};
                NMSUtils.sendPacket(player, packetPlayOutEntityDestroyConstructor.newInstance(entityIds));
            }
            entityId = 0;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public void sendMountPacket(Player player, int[] entityIds) {
        try {
            Constructor<?> packetPlayOutMountConstructor = packetPlayOutMountClass.getConstructor(entityClass);
            Object packetPlayOutMount = packetPlayOutMountConstructor.newInstance(entityArmorStand);
            NMSUtils.setField(packetPlayOutMount, NMSUtils.getField(packetPlayOutMountClass, "b"), entityIds);

            NMSUtils.sendPacket(player, packetPlayOutMount);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }



    public int getEntityId() {
        return entityId;
    }

    public Object getSpawnedArmorStand() {
        return entityArmorStand;
    }
}
