package com.tqqn.capturetheflag.utils;

import org.bukkit.ChatColor;

public enum SMessages {

    /**
     * Placeholder: Seconds
     */
    GAME_START_COUNTDOWN("&cGame is starting in &f%s"),

    /**
     * Placeholder: Seconds
     */
    GAME_END_COUNTDOWN("&cGame is ending in &f%s"),

    /**
     * Placeholder: PlayerName - minimumPlayers - maximumPlayers
     */
    PLAYER_JOIN("&f%s &ehas joined the game (&b%s&e/&b%s&e)!"),

    /**
     * Placeholder: PlayerName - minimumPlayers - maximumPlayers
     */
    PLAYER_QUIT("&f%s &ehas left the game."),

    /**
     * Placeholder: KilledPlayerName - KillerPlayerName
     */
    PLAYER_DEATH_BY_PLAYER("&c%s &ehas been killed by &c%s&e."),

    /**
     * Placeholder: KilledPlayerName
     */
    PLAYER_DEATH("&c%s &edied."),

    /**
     * Placeholder: TeamColor - WinnerPlayerName
     */
    TEAM_WIN("%s%s &ehas won the game!"),
    CHOOSE_TEAM("&fYou have chosen Team %s!"),
    ALREADY_IN_TEAM("&fYou are already a part of Team %s&f!");

    private final String message;

    SMessages(String message) {
        this.message = message;
    }

    public String getMessage(String... placeholder) {
        return ChatColor.translateAlternateColorCodes('&', String.format(message, placeholder));
    }

}
