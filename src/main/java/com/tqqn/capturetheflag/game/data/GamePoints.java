package com.tqqn.capturetheflag.game.data;

public enum GamePoints {

    PLAYER_KILL(5),
    FLAG_CAPTURE(300),
    POINTS_PER_5_SECONDS(2),
    POINTS_NEEDED_TO_WIN(500);

    private final int points;
    GamePoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
