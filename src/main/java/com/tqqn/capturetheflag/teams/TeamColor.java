package com.tqqn.capturetheflag.teams;

import org.bukkit.ChatColor;

public enum TeamColor {

    RED("&c", "RED"),
    BLUE("&9", "BLUE");

    private final String color;
    private final String nmsChatColor;
    TeamColor(String color, String nmsChatColor) {
        this.color = color;
        this.nmsChatColor = nmsChatColor;
    }

    public String getColor() {
        return color;
    }

    public String getNMSColor() {
        return nmsChatColor;
    }
}
