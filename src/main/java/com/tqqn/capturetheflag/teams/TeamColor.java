package com.tqqn.capturetheflag.teams;

public enum TeamColor {

    RED("&c"),
    BLUE("&9");

    private final String color;
    TeamColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
