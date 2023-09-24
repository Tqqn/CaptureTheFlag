package com.tqqn.capturetheflag.nms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class NMSItem {

    private final ItemStack itemStack;

    private int entityId = 0;
    private final Player player;

    Class<?> packetPlayOutSpawnEntityClass = NMSUtils.getNMSClass("PacketPlayOutSpawnEntity");
    Class<?> packetPlayOutEntityMetaClass = NMSUtils.getNMSClass("PacketPlayOutEntityMetadata");
    Class<?> packetPlayOutEntityDestroyClass = NMSUtils.getNMSClass("PacketPlayOutEntityDestroy");
    Class<?> entityItemClass = NMSUtils.getNMSClass("EntityItem");
    Class<?> entityClass = NMSUtils.getNMSClass("Entity");
    Class<?> nmsItemStackClass = NMSUtils.getNMSClass("ItemStack");
    Class<?> craftItemStack = NMSUtils.getCraftBukkitClass("inventory.CraftItemStack");
    Class<?> craftWorldClass = NMSUtils.getCraftBukkitClass("CraftWorld");
    Class<?> NMSWorldClass = NMSUtils.getNMSClass("World");
    Object entityItem;

    public NMSItem(ItemStack itemStack, Player player) {
        this.itemStack = itemStack;
        this.player = player;
    }

    public void sendSpawnItemPacketToPlayer(Location location) {
        try {
            if (entityId != 0) {
                sendDestroyItemPacketToPlayer();
            }

            Object craftWorldObj = craftWorldClass.cast(location.getWorld());
            Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle");

            Object nmsItemStack = craftItemStack.getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);

            Constructor<?> entityItemConstructor = entityItemClass.getConstructor(NMSWorldClass, double.class, double.class, double.class, nmsItemStackClass);
            Object entityItem = entityItemConstructor.newInstance(getHandleMethod.invoke(craftWorldObj), location.getX(), location.getY(), location.getZ(), nmsItemStack);

            entityId = (int) entityItemClass.getMethod("getId").invoke(entityItem);
            System.out.println(entityId);
            this.entityItem = entityItem;

            // META
            Object dataWatcherObject = entityItemClass.getMethod("getDataWatcher").invoke(entityItem);

            entityItemClass.getMethod("setInvulnerable", boolean.class).invoke(entityItem, true);

            Constructor<?> packetPlayOutSpawnEntityConstructor = packetPlayOutSpawnEntityClass.getConstructor(entityClass, int.class);
            Constructor<?> packetPlayOutSpawnEntityMetaConstructor = packetPlayOutEntityMetaClass.getConstructor(int.class, dataWatcherObject.getClass(), boolean.class);

            NMSUtils.sendPacket(player, packetPlayOutSpawnEntityConstructor.newInstance(entityItem, 1));
            NMSUtils.sendPacket(player, packetPlayOutSpawnEntityMetaConstructor.newInstance(entityId, dataWatcherObject, true));


        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public void sendDestroyItemPacketToPlayer() {
        try {
            Constructor<?> packetPlayOutEntityDestroyConstructor = packetPlayOutEntityDestroyClass.getConstructor(int[].class);
            int[] entityIds = new int[]{entityId};
            NMSUtils.sendPacket(player, packetPlayOutEntityDestroyConstructor.newInstance(entityIds));
            entityId = 0;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public Object getEntity() {
        return entityItem;
    }

    public int getEntityId() {
        return entityId;
    }
}
