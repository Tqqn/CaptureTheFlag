package com.tqqn.capturetheflag.utils;

import org.bukkit.ChatColor;

public enum SMessages {

    /**
     * Placeholder: Seconds
     */
    GAME_START_COUNTDOWN("&cGame is starting in &f%s &cseconds."),

    /**
     * Placeholder: Seconds
     */
    GAME_END_COUNTDOWN("&cGame is ending in &f%s &cseconds."),
    GAME_RESTART_COUNTDOWN("&cGame is shutting down in &f%s &cseconds."),

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
    PLAYER_DEATH_BY_PLAYER("%s &ehas been killed by %s&e."),

    /**
     * Placeholder: KilledPlayerName
     */
    PLAYER_DEATH("%s &edied."),

    /**
     * Placeholder: TeamColor - WinnerPlayerName
     */
    TEAM_WIN("%s%s &ehas won the game!"),

    /**
     * Placeholder: TeamName
     */
    CHOOSE_TEAM("&fYou have chosen Team %s!"),

    /**
     * Placeholder: TeamName
     */
    ALREADY_IN_TEAM("&fYou are already a part of Team %s&f!"),

    ENEMY_FLAG_CAPTURE("%s&l%s &r&ehas captured the %s&eflag!"),
    FLAG_RETURNED("%s&l%s &r&ehas returned the %s&eflag!"),
    FLAG_STOLE("%s&l%s &r&ehas stolen the %s&eflag!"),
    FLAG_DROPPED("%s&l%s &r&ehas dropped the %s&eflag!"),
    FLAG_SPAWNED_BECAUSE_OF_VOID("&l%s &r&ehas been returned, because &l%s &r&efell in the void!"),

    FLAG_RESET_COUNTDOWN("&2Flags are resetting in &f&l%s&r&2..."),

    /**
     * Placeholder: Kills - Assists
     */
    SCOREBOARD_KILLS_ASSISTS("&fKills: &l%s &r&fAssists: &l%s"),
    POWERUP_PICKUP("&eYou picked up the &l%s &r&e!"),

    SETUPWIZARD_SELECTED_LOCATION("&eYou selected the %s &elocation!"),
    KIT_CHOOSE("&eYou selected the %s &ekit!"),
    RESPAWN_TIMER_MESSAGE("&eYou are respawning in &l&f%s &r&eseconds!"),
    RESPAWN_TIMER_SUBTITLE("&l&f%s &r&eseconds!"),
    TEAM_CHAT_MESSAGE_FORMAT("%s %s: &f%s"),
    SHOUT_CHAT_MESSAGE_FORMAT("&7[&bSHOUT&7]&r %s %s: &f%s");

    private final String message;

    SMessages(String message) {
        this.message = message;
    }

    public String getMessage(String... placeholder) {
        return ChatColor.translateAlternateColorCodes('&', String.format(message, placeholder));
    }

}
