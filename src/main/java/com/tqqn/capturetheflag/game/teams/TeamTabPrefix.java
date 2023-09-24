package com.tqqn.capturetheflag.game.teams;

public enum TeamTabPrefix {

    RED_TAB_PREFIX("&c[R]"),
    BLUE_TAB_PREFIX("&9[B]");

    private final String prefix;
    TeamTabPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
