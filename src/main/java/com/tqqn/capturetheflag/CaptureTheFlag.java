package com.tqqn.capturetheflag;

import com.tqqn.capturetheflag.game.commands.DebugCommands;
import com.tqqn.capturetheflag.game.commands.TeamCommands;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.nms.ReflectionLayer;
import com.tqqn.capturetheflag.setup.commands.SetupCommands;
import com.tqqn.capturetheflag.setup.SetupManager;
import com.tqqn.capturetheflag.utils.MenuManager;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CaptureTheFlag extends JavaPlugin {

    private static CaptureTheFlag instance;

    private static ReflectionLayer reflectionLayer;
    private PluginConfig pluginConfig;
    private GameManager gameManager;
    private SetupManager setupManager;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        instance = this;
        findReflectionLayer();
        this.pluginConfig = new PluginConfig(this);
        if (pluginConfig.isSetupMode()) {
            this.setupManager = new SetupManager(this);
            Bukkit.getLogger().info(NMessages.SETUPWIZARD_BOOT.getMessage());
        } else {
            this.gameManager = new GameManager(this);
            this.gameManager.init();
        }
        this.menuManager = new MenuManager();
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

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public void registerCommands() {
        if (pluginConfig.isSetupMode()) {
            this.getCommand("setup").setExecutor(new SetupCommands(setupManager));
        } else {
            this.getCommand("team").setExecutor(new TeamCommands(gameManager));
            this.getCommand("debug").setExecutor(new DebugCommands(gameManager));
        }
    }

    public void findReflectionLayer() {
        String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName();
        String version = bukkitVersion.substring(bukkitVersion.lastIndexOf('.') + 1);
        try {
            Class<?> nmsClass = Class.forName("com.tqqn.capturetheflag.nms." + version + "." + version);
            Bukkit.getLogger().info("Using reflection layer for version " + version);
            reflectionLayer = (ReflectionLayer) nmsClass.getConstructors()[0].newInstance();
        } catch (Exception ignored) {
            Bukkit.getLogger().info("This version is not supported - " + version);
            Bukkit.getServer().shutdown();
        }
    }

    public static ReflectionLayer getReflectionLayer() {
        return reflectionLayer;
    }
}
