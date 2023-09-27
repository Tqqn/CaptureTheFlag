package com.tqqn.capturetheflag.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class GameUtils {

    /**
     * Static Method that will broadcast a message with translated colors.
     * @param message String
     */
    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(translateColor(message));
    }

    /**
     * Static Method that will return a translated color message.
     * @param message String
     */
    public static String translateColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Static Method that will return a String array with translated colors.
     * @param strings String[]
     */
    public static String[] translateColor(String[] strings) {
        ArrayList<String> msgArray = new ArrayList<>();
        for (String msg : strings) {
            msgArray.add(translateColor(msg));
        }
        return msgArray.toArray(new String[0]);
    }

    /**
     * Static Method that converts seconds into a formatted clock.
     * @param seconds long
     */
    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format("%02d:%02d", m, s);
    }



}
