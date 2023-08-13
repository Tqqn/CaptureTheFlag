package com.tqqn.capturetheflag.teams;

import org.bukkit.Location;

public class Team {

    private String displayName;
    private TeamColor teamColor;

    private Location spawnLocation;
    private int points = 0;

    public Team(String displayName, TeamColor teamColor, Location spawnLocation) {
        this.displayName = displayName;
        this.teamColor = teamColor;
        this.spawnLocation = spawnLocation;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void addPoints(int points) {
        this.points = (this.points + points);
    }

    public int getPoints() {
        return this.points;
    }
}
