package com.tqqn.capturetheflag.game;

import com.tqqn.capturetheflag.CaptureTheFlag;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public abstract class AbstractGameState implements Listener {

    private final String name;

    public AbstractGameState(String name) {
        this.name = "GameState: " + name;
    }

    public void init() {
        registerEvents();
        register();
        Bukkit.getLogger().info(name + " enabled and registered.");
    }

    public void disable() {
        unRegisterEvents();
        unRegister();
        Bukkit.getLogger().info(name + " disabled and unregistered.");
    }

    public void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, CaptureTheFlag.getInstance());
    }

    public void unRegisterEvents() {
        HandlerList.unregisterAll(this);
    }

    public abstract void register();
    public abstract void unRegister();

}
