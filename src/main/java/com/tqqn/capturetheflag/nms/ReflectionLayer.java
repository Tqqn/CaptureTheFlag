package com.tqqn.capturetheflag.nms;

import com.tqqn.capturetheflag.nms.framework.AbstractNMSArmorStandEntity;
import com.tqqn.capturetheflag.nms.framework.AbstractNMSItemEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public interface ReflectionLayer {

    String getVersionString();

    void sendPacket(Player player, Object packet);

    AbstractNMSItemEntity createNMSItemEntity(ItemStack itemStack, Player player);
    AbstractNMSArmorStandEntity createNMSArmorStandEntity(String displayName);

    void sendTitleMessage(Player player, String message, String subMessage, int fadeIn, int duration, int fadeOut);
    void sendNameTag(Player player, String color, String prefix, String suffix, Player players, int priority);

    //NBT
    ItemStack applyNBTTag(ItemStack itemStack, String tagName, String tagValue);
    String getNBTTag(ItemStack itemStack, String key);

    Field getField(Class<?> clazz, String fieldName);
    void setField(Object packet, Field field, Object value);

    Class<?> getNMSClass(String name);
}
