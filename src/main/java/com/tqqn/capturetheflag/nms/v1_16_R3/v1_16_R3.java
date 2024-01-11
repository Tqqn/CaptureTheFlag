package com.tqqn.capturetheflag.nms.v1_16_R3;

import com.tqqn.capturetheflag.nms.framework.AbstractNMSArmorStandEntity;
import com.tqqn.capturetheflag.nms.framework.AbstractNMSItemEntity;
import com.tqqn.capturetheflag.nms.ReflectionLayer;
import com.tqqn.capturetheflag.nms.v1_16_R3.objects.NMSArmorStand;
import com.tqqn.capturetheflag.nms.v1_16_R3.objects.NMSItemEntity;
import com.tqqn.capturetheflag.utils.GameUtils;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class v1_16_R3 implements ReflectionLayer {
    @Override
    public String getVersionString() {
        return "v1_16_R3";
    }

    @Override
    public void sendPacket(Player player, Object packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public AbstractNMSItemEntity createNMSItemEntity(ItemStack itemStack, Player player) {
        return new NMSItemEntity(itemStack, player);
    }

    @Override
    public AbstractNMSArmorStandEntity createNMSArmorStandEntity(String displayName) {
        return new NMSArmorStand(displayName);
    }

    @Override
    public void sendTitleMessage(Player player, String message, String subMessage, int fadeIn, int duration, int fadeOut) {
        String titleMessage = "{\"text\":\""  + message + "\"}";
        String titleSubMessage = "{\"text\":\"" + subMessage + "\"}";

        sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, IChatBaseComponent.ChatSerializer.a(titleMessage), fadeIn, duration, fadeOut));
        sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(titleMessage), fadeIn, duration, fadeOut));
        sendPacket(player, new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(titleSubMessage), fadeIn, duration, fadeOut));
    }

    @Override
    public void sendNameTag(Player player, String color, String prefix, String suffix, Player players, int priority) {
        try {
            String playerNameMessage = "{\"text\":\"" + player.getName() + "\"}";
            String prefixMessage = "{\"text\":\"" + prefix + " \"}";
            String suffixMessage = "{\"text\":\"" + suffix + "\"}";

            String teamName = "" + priority + player.getName().charAt(0) + UUID.randomUUID().toString().substring(0, 10);
            List<String> playerList = new ArrayList<>();
            playerList.add(player.getName());

            Object packetPlayOutScoreboardTeam2 = PacketPlayOutScoreboardTeam.class.getConstructor().newInstance();

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "a"), teamName);

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "b"), IChatBaseComponent.ChatSerializer.a(GameUtils.translateColor(playerNameMessage)));

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "c"), IChatBaseComponent.ChatSerializer.a(GameUtils.translateColor(prefixMessage)));

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "d"), IChatBaseComponent.ChatSerializer.a(GameUtils.translateColor(suffixMessage)));

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "e"), "ALWAYS");

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "h"), playerList);

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "g"), EnumChatFormat.valueOf(color));

            setField(packetPlayOutScoreboardTeam2, getField(packetPlayOutScoreboardTeam2.getClass(), "i"), 0);

            sendPacket(players, packetPlayOutScoreboardTeam2);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ItemStack applyNBTTag(ItemStack itemStack, String tagName, String tagValue) {
        net.minecraft.server.v1_16_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTagCompound = nmsItemStack.getOrCreateTag();
        nbtTagCompound.setString(tagName, tagValue);
        nmsItemStack.setTag(nbtTagCompound);

        return CraftItemStack.asBukkitCopy(nmsItemStack);
    }

    @Override
    public String getNBTTag(ItemStack itemStack, String key) {
        net.minecraft.server.v1_16_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsItemStack.hasTag() || nmsItemStack.getTag().getString(key) == null) return null;

        return nmsItemStack.getTag().getString(key);
    }

    @Override
    public void setField(Object packet, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(packet, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        field.setAccessible(!field.isAccessible());
    }

    @Override
    public Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersionString() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
