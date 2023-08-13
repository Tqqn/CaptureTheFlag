package com.tqqn.capturetheflag.utils;

import org.bukkit.ChatColor;

public enum NMessages {

    NO_PERMISSION("&cYou have no permission to use this command."),
    GAME_START("&cGame started!"),
    YOU_DIED("&cYou died."),
    YOU_WON("&6&lYou won!");
    private final String message;

    NMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
