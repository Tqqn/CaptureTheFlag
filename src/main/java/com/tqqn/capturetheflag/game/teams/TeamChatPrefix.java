package com.tqqn.capturetheflag.game.teams;

public enum TeamChatPrefix {

    RED_CHAT_PREFIX("&c[RED] "),
    BLUE_CHAT_PREFIX("&9[BLUE] ");

    private final String prefix;
    TeamChatPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
