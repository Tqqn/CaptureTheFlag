package com.tqqn.capturetheflag.utils;

import com.tqqn.capturetheflag.CaptureTheFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class PluginConfig {

    private final CaptureTheFlag plugin;

    public PluginConfig(CaptureTheFlag plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public boolean isSetupMode() {
        return plugin.getConfig().getBoolean("setup-mode");
    }

    public int getMaxPlayers() {
        return plugin.getConfig().getInt("max-players");
    }

    public int getMinPlayers() {
        return plugin.getConfig().getInt("min-players");
    }

    public int getGameStartCountdown() {
        return plugin.getConfig().getInt("game-start-countdown");
    }

    public Location getLobbySpawnLocation() {
        return new Location(Bukkit.getWorld(plugin.getConfig().getString("lobby.spawn.world")),
                plugin.getConfig().getDouble("lobby.spawn.x"),
                plugin.getConfig().getDouble("lobby.spawn.y"),
                plugin.getConfig().getDouble("lobby.spawn.z"),
                (float) plugin.getConfig().getDouble("lobby.spawn.yaw"),
                (float) plugin.getConfig().getDouble("lobby.spawn.pitch"));
    }

    public Location getTeamSpawnLocation(String team) {
            return new Location(Bukkit.getWorld(plugin.getConfig().getString("team." + team + ".spawn.world")),
                    plugin.getConfig().getDouble("team." + team + ".spawn.x"),
                    plugin.getConfig().getDouble("team." + team + ".spawn.y"),
                    plugin.getConfig().getDouble("team." + team + ".spawn.z"),
                    (float) plugin.getConfig().getDouble("team." + team + ".spawn.yaw"),
                    (float) plugin.getConfig().getDouble("team." + team + ".spawn.pitch"));
    }

    public Location getTeamFlagSpawnLocation(String team) {
        return new Location(Bukkit.getWorld(plugin.getConfig().getString("team." + team + ".flag.world")),
                plugin.getConfig().getDouble("team." + team + ".flag.x"),
                plugin.getConfig().getDouble("team." + team + ".flag.y"),
                plugin.getConfig().getDouble("team." + team + ".flag.z"));
    }

    public List<Location> getPowerUpSpawnLocations() {
        List<Location> spawnLocations = new ArrayList<>();
        for (String key : plugin.getConfig().getConfigurationSection("powerups").getKeys(false)) {
            spawnLocations.add(new Location(Bukkit.getWorld(plugin.getConfig().getString("powerups." + key + ".world")),
                    plugin.getConfig().getDouble("powerups." + key + ".x"),
                    plugin.getConfig().getDouble("powerups." + key + ".y"),
                    plugin.getConfig().getDouble("powerups." + key + ".z")));
        }
        return spawnLocations;
    }

    public void saveLobbyLocation(Location location) {
        plugin.getConfig().set("lobby.spawn.world", location.getWorld().getName());
        plugin.getConfig().set("lobby.spawn.x", location.getX());
        plugin.getConfig().set("lobby.spawn.y", location.getY());
        plugin.getConfig().set("lobby.spawn.z", location.getZ());
        plugin.getConfig().set("lobby.spawn.yaw", location.getYaw());
        plugin.getConfig().set("lobby.spawn.pitch", location.getPitch());

        plugin.saveConfig();
    }

    public void saveBlueSpawnLocation(Location location) {
        plugin.getConfig().set("team.blue.spawn.world", location.getWorld().getName());
        plugin.getConfig().set("team.blue.spawn.x", location.getX());
        plugin.getConfig().set("team.blue.spawn.y", location.getY());
        plugin.getConfig().set("team.blue.spawn.z", location.getZ());
        plugin.getConfig().set("team.blue.spawn.yaw", location.getYaw());
        plugin.getConfig().set("team.blue.spawn.pitch", location.getPitch());

        plugin.saveConfig();
    }

    public void saveBlueFlagLocation(Location location) {
        plugin.getConfig().set("team.blue.flag.world", location.getWorld().getName());
        plugin.getConfig().set("team.blue.flag.x", location.getBlockX());
        plugin.getConfig().set("team.blue.flag.y", location.getBlockY());
        plugin.getConfig().set("team.blue.flag.z", location.getBlockZ());

        plugin.saveConfig();
    }

    public void saveRedSpawnLocation(Location location) {
        plugin.getConfig().set("team.red.spawn.world", location.getWorld().getName());
        plugin.getConfig().set("team.red.spawn.x", location.getX());
        plugin.getConfig().set("team.red.spawn.y", location.getY());
        plugin.getConfig().set("team.red.spawn.z", location.getZ());
        plugin.getConfig().set("team.red.spawn.yaw", location.getYaw());
        plugin.getConfig().set("team.red.spawn.pitch", location.getPitch());

        plugin.saveConfig();
    }

    public void saveRedFlagLocation(Location location) {
        plugin.getConfig().set("team.red.flag.world", location.getWorld().getName());
        plugin.getConfig().set("team.red.flag.x", location.getBlockX());
        plugin.getConfig().set("team.red.flag.y", location.getBlockY());
        plugin.getConfig().set("team.red.flag.z", location.getBlockZ());

        plugin.saveConfig();
    }

    public void savePowerUpLocations(List<Location> powerUpLocations) {
        int i = 0;
        for (Location location : powerUpLocations) {
            plugin.getConfig().set("powerups." + i + ".world", location.getWorld().getName());
            plugin.getConfig().set("powerups." + i + ".x", location.getX());
            plugin.getConfig().set("powerups." + i + ".y", location.getY());
            plugin.getConfig().set("powerups." + i + ".z", location.getZ());

            plugin.saveConfig();
            i++;
        }
    }
}
