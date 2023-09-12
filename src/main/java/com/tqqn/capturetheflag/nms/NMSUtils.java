package com.tqqn.capturetheflag.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtils {

    public static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public static void setField(Object packet, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(packet, value);
        } catch (IllegalAccessException | IllegalArgumentException var4) {
            var4.printStackTrace();
        }
        field.setAccessible(!field.isAccessible());
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (SecurityException | NoSuchFieldException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Object applyNMSTag(Object nmsItemStack, String tagName, String tagValue) {
        try {
            Method getOrCreateTag = nmsItemStack.getClass().getMethod("getOrCreateTag");
            Object nbtTagCompound = getOrCreateTag.invoke(nmsItemStack);

            Method setString = nbtTagCompound.getClass().getMethod("setString", String.class, String.class);
            setString.invoke(nbtTagCompound, tagName, tagValue);

            Method setTag = nmsItemStack.getClass().getMethod("setTag");
            setTag.invoke(nbtTagCompound);
            return nmsItemStack;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    //net.minecraft.server.v1_16_R3.ItemStack
    public static Object applyNMSTag(Object nmsItemStack, String tagName, int tagValue) {
        try {

            Method getOrCreateTag = nmsItemStack.getClass().getDeclaredMethod("getOrCreateTag");
            Object nbtTagCompound = getOrCreateTag.invoke(nmsItemStack);

            Method setString = nbtTagCompound.getClass().getMethod("setInt", String.class, int.class);
            setString.invoke(nbtTagCompound, tagName, tagValue);

            Method setTag = nmsItemStack.getClass().getMethod("setTag", getNMSClass("NBTTagCompound"));
            setTag.invoke(nmsItemStack, nbtTagCompound);
            return nmsItemStack;

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getNBTTag(Object nmsItemStack, String key) {
        try {

            Method hasTag = nmsItemStack.getClass().getMethod("hasTag");
            boolean hasTagResult = (boolean) hasTag.invoke(nmsItemStack);

            if (!hasTagResult) return null;

            Method getTag = nmsItemStack.getClass().getMethod("getTag");
            Object nbtTagCompound = getTag.invoke(nmsItemStack);

            Method get = nbtTagCompound.getClass().getMethod("get", String.class);
            return String.valueOf(get.invoke(nbtTagCompound, key));

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
