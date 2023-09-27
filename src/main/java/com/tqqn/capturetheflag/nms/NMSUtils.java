package com.tqqn.capturetheflag.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtils {

    public static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    /**
     * Static Method that will return the NMS Class of the given name.
     * Throws ClassNotFoundException if not found.
     * @param name String
     */
    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Static Method that will return the CraftBukkit Class of the given name.
     * Throws ClassNotFoundException if not found.
     * @param name String
     */
    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Static Method that will send a packet to the given player.
     * Throws Exception if something goes wrong.
     * @param player Player
     * @param packet Object
     */
    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Static Method that will set the fields value of the Object
     * @param packet Object
     * @param field Field
     * @param value Object
     */
    public static void setField(Object packet, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(packet, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        field.setAccessible(!field.isAccessible());
    }

    /**
     * Static Method that will return the field from the given class with the given fieldName.
     * @param clazz Class
     * @param fieldName String
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Static Method that will apply an NMS-Tag to the given item with the given tagName and tagValue.
     * @param itemStack ItemStack
     * @param tagName String
     * @param tagValue String
     */
    public static ItemStack applyNBTTag(ItemStack itemStack, String tagName, String tagValue) {
        try {
            Method asNMSCopy = getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
            Object nmsItemStack = asNMSCopy.invoke(null, itemStack);

            Method getOrCreateTag = nmsItemStack.getClass().getDeclaredMethod("getOrCreateTag");
            Object nbtTagCompound = getOrCreateTag.invoke(nmsItemStack);

            Method setString = nbtTagCompound.getClass().getMethod("setString", String.class, String.class);
            setString.invoke(nbtTagCompound, tagName, tagValue);

            Method setTag = nmsItemStack.getClass().getMethod("setTag", getNMSClass("NBTTagCompound"));
            setTag.invoke(nmsItemStack, nbtTagCompound);

            Method asBukkitCopy = getCraftBukkitClass("inventory.CraftItemStack").getMethod("asBukkitCopy", getNMSClass("ItemStack"));
            return (ItemStack) asBukkitCopy.invoke(null, nmsItemStack);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Static Method that will return the NBTTag of the given ItemStack with the given key.
     * @param itemStack ItemStack
     * @param key String
     */
    public static String getNBTTag(ItemStack itemStack, String key) {
        try {
            Method asNMSCopy = getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
            Object nmsItemStack = asNMSCopy.invoke(null, itemStack);

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

    /**
     * Static Void Method that will send a title message packet to the player.
     * @param player Player
     * @param message String
     * @param subMessage String
     * @param fadeIn int
     * @param duration int
     * @param fadeOut int
     */
    public static void sendTitleMessage(Player player, String message, String subMessage, int fadeIn, int duration, int fadeOut) {
        String titleMessage = "{\"text\":\""  + message + "\"}";
        String titleSubMessage = "{\"text\":\"" + subMessage + "\"}";

        Class<?> enumTitleActionClass = getNMSClass("PacketPlayOutTitle$EnumTitleAction");
        Class<?> iChatBaseCompClass = getNMSClass("IChatBaseComponent");

        Object chatSerializer;
        Object chatSerializerSub;

        Object enumTimes;
        Object enumTitle;
        Object enumSubTitle;

        Object packetTimes;
        Object packetTitleMessage;
        Object packetSubTitleMessage;

        try {
            Method a = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class);

            chatSerializer = a.invoke(iChatBaseCompClass, titleMessage);
            chatSerializerSub = a.invoke(iChatBaseCompClass, titleSubMessage);

            enumTimes = enumTitleActionClass.getField("TIMES").get(null);
            enumTitle = enumTitleActionClass.getField("TITLE").get(null);
            enumSubTitle = enumTitleActionClass.getField("SUBTITLE").get(null);

            Constructor<?> packetPlayOutTitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(enumTitleActionClass, iChatBaseCompClass, int.class, int.class, int.class);

            packetTimes = packetPlayOutTitleConstructor.newInstance(enumTimes, chatSerializer, fadeIn, duration, fadeOut);
            packetTitleMessage = packetPlayOutTitleConstructor.newInstance(enumTitle, chatSerializer, fadeIn, duration, fadeOut);
            packetSubTitleMessage = packetPlayOutTitleConstructor.newInstance(enumSubTitle, chatSerializerSub, fadeIn, duration, fadeOut);

            sendPacket(player, packetTimes);
            sendPacket(player, packetTitleMessage);
            sendPacket(player, packetSubTitleMessage);

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
