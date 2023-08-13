package com.tqqn.capturetheflag.utils;

import com.tqqn.capturetheflag.CaptureTheFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class PluginConfig {

    private final CaptureTheFlag plugin;

    public PluginConfig(CaptureTheFlag plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public int getMaxPlayers() {
        return plugin.getConfig().getInt("max-players");
    }

    public int getMinPlayers() {
        return plugin.getConfig().getInt("min-players");
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
}
