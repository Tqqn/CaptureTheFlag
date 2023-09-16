package com.tqqn.capturetheflag.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class GameUtils {

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(translateColor(message));
    }

    public static String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String[] translateColor(String[] strings) {
        ArrayList<String> msgArray = new ArrayList<>();
        for (String msg : strings) {
            msgArray.add(translateColor(msg));
        }
        return msgArray.toArray(new String[0]);
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format("%02d:%02d", m, s);
    }



}
