package com.tqqn.capturetheflag.utils;

import org.bukkit.ChatColor;

public enum NMessages {

    NO_PERMISSION("&cYou have no permission to use this command."),
    GAME_START("&cGame started!"),
    YOU_DIED("&cYou died."),
    YOU_WON("&6&lYou won!"),
    TAB_DEATH_SUFFIX(" &7&l☠"),

    FLAG_ITEM_HOLOGRAM_ENEMY("&e[RIGHT/LEFT-CLICK] &6To carry the Flag!"),

    FLAG_ITEM_HOLOGRAM_SELF("&eDefend the Flag from the enemy team!"),
    FLAG_ITEM_HOLOGRAM_RETURN("&e[RIGHT/LEFT-CLICK] &6To return your Flag!"),
    ACTIVE_SCOREBOARD_TITLE("&c&lCAPTURE &9&lTHE FLAG"),
    POWERUP_SPEED("&b&lSpeed PowerUp"),
    POWERUP_STRENGHT("&c&lStrenght PowerUp"),
    POWERUP_JUMP("&a&lJump PowerUp"),

    SETUPWIZARD_BOOT("&c!!!!! SetUp Mode actived!!!!!!"),

    SETUPWIZARD_JOIN_PLAYER_MESSAGE("&bYou are joining in setup mode. Please select and save the locations."),
    SETUPWIZARD_SAVE_LOCATION("&bYou saved the setup locations."),
    SETUPWIZARD_LOCATION_NULL("&cError! One or more locations are null. Please select the locations.");
    private final String message;

    NMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
