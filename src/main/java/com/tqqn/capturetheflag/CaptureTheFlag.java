package com.tqqn.capturetheflag;

import com.tqqn.capturetheflag.commands.TeamCommands;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.utils.PluginConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class CaptureTheFlag extends JavaPlugin {

    private static CaptureTheFlag instance;
    private PluginConfig pluginConfig;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        this.pluginConfig = new PluginConfig(this);
        this.gameManager = new GameManager(this);
        this.gameManager.init();

        registerCommands();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static CaptureTheFlag getInstance() {
        return instance;
    }

    public PluginConfig getPluginConfig() {
        return this.pluginConfig;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public void registerCommands() {
        this.getCommand("team").setExecutor(new TeamCommands(gameManager));
    }
}
