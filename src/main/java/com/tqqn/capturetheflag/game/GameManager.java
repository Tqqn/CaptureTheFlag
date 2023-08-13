package com.tqqn.capturetheflag.game;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.arena.Arena;
import com.tqqn.capturetheflag.items.GameItems;
import com.tqqn.capturetheflag.listeners.player.*;
import com.tqqn.capturetheflag.listeners.universal.BlockBreakListener;
import com.tqqn.capturetheflag.listeners.universal.BlockPlaceListener;
import com.tqqn.capturetheflag.tasks.ActiveGameTask;
import com.tqqn.capturetheflag.tasks.StartGameTask;
import com.tqqn.capturetheflag.teams.Team;
import com.tqqn.capturetheflag.teams.TeamColor;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private GameStates gameState = GameStates.LOBBY;
    private StartGameTask startGameTask;
    private ActiveGameTask activeGameTask;

    private Map<Integer, ItemStack> lobbyItems;

    private final CaptureTheFlag plugin;

    private Team teamRed;
    private Team teamBlue;

    private Arena arena;

    public GameManager(CaptureTheFlag plugin) {
        this.plugin = plugin;
    }

    public void init() {
        this.teamRed = new Team("&cRed", TeamColor.RED, plugin.getPluginConfig().getTeamSpawnLocation("red"));
        this.teamBlue = new Team("&9Blue", TeamColor.BLUE, plugin.getPluginConfig().getTeamSpawnLocation("blue"));
        this.arena = new Arena(plugin.getPluginConfig().getLobbySpawnLocation(), teamRed, teamBlue, plugin.getPluginConfig().getMinPlayers(), plugin.getPluginConfig().getMaxPlayers());
        registerEvents();
        initLobbyItems();
    }

    public void disableGame() {
        Bukkit.getServer().shutdown();
    }

    public void setGameState(GameStates gameState) {
        //Checks if the new gameState is the same as the old gameState.
        if (this.gameState == gameState) return;
        //checks if the current gameState is active and the new gameState is not lobby or starting.
        if (this.gameState == GameStates.ACTIVE && (gameState == GameStates.RESTARTING || gameState == GameStates.STARTING || gameState == GameStates.LOBBY)) return;

        switch (gameState) {
            case STARTING:
                this.gameState = GameStates.STARTING;
                startCountdownToStartGame();
                break;
            case ACTIVE:
                this.gameState = GameStates.ACTIVE;
                arena.teleportAllPlayersToSpawn();
                startActiveGameTask();
                break;
            case END:
                this.gameState = GameStates.END;
                GameUtils.broadcastMessage(SMessages.TEAM_WIN.getMessage(this.arena.isThereAWinner().getTeamColor().getColor(), this.arena.isThereAWinner().getDisplayName()));
                break;
            case RESTARTING:
                this.gameState = GameStates.RESTARTING;
                break;
        }
    }

    public GameStates getGameStates() {
        return this.gameState;
    }

    public Arena getArena() {
        return this.arena;
    }

    private void startCountdownToStartGame() {
        this.startGameTask = new StartGameTask(this);
        this.startGameTask.runTaskTimerAsynchronously(plugin, 0, 20L);
    }

    private void startActiveGameTask() {
        this.activeGameTask = new ActiveGameTask(this);
        this.activeGameTask.runTaskTimerAsynchronously(plugin, 0, 20L);
    }

    public Team getTeamRed() {
        return teamRed;
    }

    public Team getTeamBlue() {
        return teamBlue;
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        //Player Listeners
        pluginManager.registerEvents(new PlayerDamageListener(this), plugin);
        pluginManager.registerEvents(new PlayerDropItemListener(), plugin);
        pluginManager.registerEvents(new PlayerInteractListener(this), plugin);
        pluginManager.registerEvents(new PlayerInventoryInteractListener(this), plugin);
        pluginManager.registerEvents(new PlayerJoinListener(this), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(this), plugin);

        //Universal Listeners
        pluginManager.registerEvents(new BlockBreakListener(this), plugin);
        pluginManager.registerEvents(new BlockPlaceListener(), plugin);
    }

    public void initLobbyItems() {
        lobbyItems = new HashMap<>();
        lobbyItems.put(3, GameItems.CHOOSE_RED_TEAM.getItemStack());
        lobbyItems.put(5, GameItems.CHOOSE_BLUE_TEAM.getItemStack());
    }

    public void giveLobbyItems(Player player) {
        player.getInventory().clear();

        for (Map.Entry<Integer, ItemStack> set : lobbyItems.entrySet()) {
            player.getInventory().setItem(set.getKey(), set.getValue());
        }
    }
}
