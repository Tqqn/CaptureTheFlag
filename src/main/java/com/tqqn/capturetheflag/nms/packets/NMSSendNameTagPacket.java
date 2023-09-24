package com.tqqn.capturetheflag.nms.packets;

import com.tqqn.capturetheflag.nms.NMSUtils;
import com.tqqn.capturetheflag.utils.GameUtils;
import org.bukkit.entity.Player;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NMSSendNameTagPacket {

    public static void sendNameTagPlayers(Player player, String color, String prefix, String suffix, Player players, int priority) {

        String playerNameMessage = "{\"text\":\"" + player.getName() + "\"}";
        String prefixMessage = "{\"text\":\"" + prefix + " \"}";
        String suffixMessage = "{\"text\":\"" + suffix + "\"}";

        Class<?> iChatBaseCompClass = NMSUtils.getNMSClass("IChatBaseComponent");
        Class<?> enumChatColorClass = NMSUtils.getNMSClass("EnumChatFormat");

        Object playerNameChatSerializer;
        Object prefixChatSerializer;
        Object suffixChatSerializer;

        Object enumChatColor;

        try {
            Method a = NMSUtils.getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class);

            playerNameChatSerializer = a.invoke(iChatBaseCompClass, GameUtils.translateColor(playerNameMessage));
            prefixChatSerializer = a.invoke(iChatBaseCompClass, GameUtils.translateColor(prefixMessage));
            suffixChatSerializer = a.invoke(iChatBaseCompClass, GameUtils.translateColor(suffixMessage));

            enumChatColor = enumChatColorClass.getField(color).get(null);

            String playerName = player.getName();

            String teamName = "" + priority + playerName.charAt(0) + UUID.randomUUID().toString().substring(0, 10);

            List<String> playerList = new ArrayList<>();
            playerList.add(player.getName());

            Constructor<?> packetPlayOutScoreboardTeamConstructor = NMSUtils.getNMSClass("PacketPlayOutScoreboardTeam").getConstructor();
            Object packetScoreboardTeam = packetPlayOutScoreboardTeamConstructor.newInstance();

            Class<?> packetPlayOutScoreboardTeamClass = packetScoreboardTeam.getClass();

            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "a"), teamName);
            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "b"), playerNameChatSerializer);
            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "c"), prefixChatSerializer);
            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "d"), suffixChatSerializer);
            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "e"), "ALWAYS");
            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "h"), playerList);
            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "g"), enumChatColor);
            NMSUtils.setField(packetScoreboardTeam, NMSUtils.getField(packetPlayOutScoreboardTeamClass, "i"), 0);

            NMSUtils.sendPacket(players, packetScoreboardTeam);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
