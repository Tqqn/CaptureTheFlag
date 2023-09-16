package com.tqqn.capturetheflag.game;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.commands.DebugCommands;
import com.tqqn.capturetheflag.game.commands.KitCommands;
import com.tqqn.capturetheflag.game.commands.TeamCommands;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.flag.Flag;
import com.tqqn.capturetheflag.game.gamestates.active.ActiveGameState;
import com.tqqn.capturetheflag.game.gamestates.end.EndGameState;
import com.tqqn.capturetheflag.game.gamestates.lobby.LobbyGameState;
import com.tqqn.capturetheflag.game.kits.menu.KitSelectorMenu;
import com.tqqn.capturetheflag.items.PluginItems;
import com.tqqn.capturetheflag.game.listeners.global.*;
import com.tqqn.capturetheflag.game.listeners.universal.BlockBreakListener;
import com.tqqn.capturetheflag.game.listeners.universal.BlockPlaceListener;
import com.tqqn.capturetheflag.game.tab.TabScoreboardManager;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.ActiveGameTask;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.game.teams.TeamChatPrefix;
import com.tqqn.capturetheflag.game.teams.TeamColor;
import com.tqqn.capturetheflag.game.teams.TeamTabPrefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    private static GameStates gameState = GameStates.LOBBY;
    private final TabScoreboardManager tabScoreboardManager = new TabScoreboardManager();
    private static final Map<Location, Flag> spawnedFlags = new HashMap<>();

    private final List<AbstractGameState> enabledGameStates = new ArrayList<>();

    private Map<Integer, ItemStack> lobbyItems;

    private final CaptureTheFlag plugin;

    private GameTeam gameTeamRed;
    private GameTeam gameTeamBlue;

    private Arena arena;

    private KitSelectorMenu kitSelectorMenu;

    public GameManager(CaptureTheFlag plugin) {
        this.plugin = plugin;
    }

    public void init() {
        this.gameTeamRed = new GameTeam("&cRed", TeamColor.RED, TeamTabPrefix.RED_TAB_PREFIX, TeamChatPrefix.RED_CHAT_PREFIX, 1,  plugin.getPluginConfig().getTeamSpawnLocation("red"), new Flag("&cRed Flag", plugin.getPluginConfig().getTeamFlagSpawnLocation("red"), PluginItems.RED_FLAG.getItemStack().getType()), this);
        this.gameTeamBlue = new GameTeam("&9Blue", TeamColor.BLUE, TeamTabPrefix.BLUE_TAB_PREFIX, TeamChatPrefix.BLUE_CHAT_PREFIX, 2,  plugin.getPluginConfig().getTeamSpawnLocation("blue"), new Flag("&9Blue Flag", plugin.getPluginConfig().getTeamFlagSpawnLocation("blue"), PluginItems.BLUE_FLAG.getItemStack().getType()), this);
        gameTeamRed.getTeamFlag().setGameTeam(gameTeamRed);
        gameTeamBlue.getTeamFlag().setGameTeam(gameTeamBlue);

        this.arena = new Arena(plugin.getPluginConfig().getLobbySpawnLocation(), gameTeamRed, gameTeamBlue, plugin.getPluginConfig().getMinPlayers(), plugin.getPluginConfig().getMaxPlayers(), plugin.getPluginConfig().getGameStartCountdown());
        initLobbyItems();
        this.kitSelectorMenu = new KitSelectorMenu();

        registerEvents();
        registerCommands();

        LobbyGameState lobbyGameState = new LobbyGameState(this);
        lobbyGameState.init();
        enabledGameStates.add(lobbyGameState);
    }

    public void disableGame() {
        Bukkit.getServer().shutdown();
    }

    public void setGameState(GameStates gameState) {
        //Checks if the new gameState is the same as the old gameState.
        if (GameManager.gameState == gameState) return;
        //checks if the current gameState is active and the new gameState is not lobby.
        if (GameManager.gameState == GameStates.ACTIVE && (gameState == GameStates.LOBBY)) return;

        enabledGameStates.get(0).disable();
        enabledGameStates.clear();

        switch (gameState) {
            case ACTIVE:
                GameManager.gameState = GameStates.ACTIVE;
                ActiveGameState activeGameState = new ActiveGameState(this);
                activeGameState.init();
                enabledGameStates.add(activeGameState);
                break;
            case END:
                GameManager.gameState = GameStates.END;
                EndGameState endGameState = new EndGameState(this);
                endGameState.init();
                enabledGameStates.add(endGameState);
                break;
        }
    }

    public static GameStates getGameStates() {
        return gameState;
    }

    public Arena getArena() {
        return this.arena;
    }

    public KitSelectorMenu getKitSelectorMenu() {
        return kitSelectorMenu;
    }

    public GameTeam getTeamRed() {
        return gameTeamRed;
    }

    public GameTeam getTeamBlue() {
        return gameTeamBlue;
    }

    public long getActiveGameTaskTime() {
        if (GameManager.gameState == GameStates.ACTIVE) {
            return ActiveGameTask.getTime();
        }
        return 0;
    }

    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        //Global Listeners
        pluginManager.registerEvents(new GlobalPlayerDamageListener(), plugin);
        pluginManager.registerEvents(new GlobalDropItemListener(), plugin);
        pluginManager.registerEvents(new GlobalPlayerInventoryListener(), plugin);
        pluginManager.registerEvents(new GlobalPlayerQuitListener(), plugin);

        //Universal Listeners
        pluginManager.registerEvents(new BlockBreakListener(this), plugin);
        pluginManager.registerEvents(new BlockPlaceListener(), plugin);
    }

    private void registerCommands() {
            plugin.getCommand("team").setExecutor(new TeamCommands(this));
            plugin.getCommand("debug").setExecutor(new DebugCommands(this));
            plugin.getCommand("kit").setExecutor(new KitCommands(this));
    }

    public void initLobbyItems() {
        lobbyItems = new HashMap<>();
        lobbyItems.put(3, PluginItems.CHOOSE_RED_TEAM.getItemStack());
        lobbyItems.put(5, PluginItems.CHOOSE_BLUE_TEAM.getItemStack());
    }

    public void giveLobbyItems(Player player) {
        clearPlayerInventory(player);

        for (Map.Entry<Integer, ItemStack> set : lobbyItems.entrySet()) {
            player.getInventory().setItem(set.getKey(), set.getValue());
        }
    }

    public void clearPlayerInventory(Player player) {
        player.getInventory().clear();
    }

    public TabScoreboardManager getTabScoreboardManager() {
        return tabScoreboardManager;
    }

    public static void addSpawnedFlag(Flag flag) {
        spawnedFlags.put(flag.getCurrentLocation(), flag);
    }

    public static Flag getFlag(Location location) {
        return spawnedFlags.get(location);
    }

    public static void removeSpawnedFlag(Flag flag) {
        spawnedFlags.remove(flag.getCurrentLocation());
    }

    public static boolean isLocationSpawnedFlag(Location location) {
        return spawnedFlags.containsKey(location);
    }

    private GameTeam whichTeamIsSmaller() {
        if (gameTeamBlue.getTeamPlayers().size() < gameTeamRed.getTeamPlayers().size()) {
            return gameTeamBlue;
        } else {
            return gameTeamRed;
        }
    }

    public void balanceTeamPlayers() {
        for (GamePlayer gamePlayer : arena.getNoTeamPlayers()) {
            GameTeam gameTeam = whichTeamIsSmaller();
            gameTeam.addPlayerToTeam(gamePlayer, gameTeam);
        }
    }
}
