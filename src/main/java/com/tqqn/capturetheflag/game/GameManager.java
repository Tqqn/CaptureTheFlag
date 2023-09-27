package com.tqqn.capturetheflag.game;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.commands.DebugCommands;
import com.tqqn.capturetheflag.game.commands.KitCommands;
import com.tqqn.capturetheflag.game.commands.ShoutCommands;
import com.tqqn.capturetheflag.game.commands.TeamCommands;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.flag.Flag;
import com.tqqn.capturetheflag.game.gamestates.active.ActiveGameState;
import com.tqqn.capturetheflag.game.gamestates.end.EndGameState;
import com.tqqn.capturetheflag.game.gamestates.lobby.LobbyGameState;
import com.tqqn.capturetheflag.game.kits.menu.KitSelectorMenu;
import com.tqqn.capturetheflag.game.listeners.*;
import com.tqqn.capturetheflag.utils.PluginItems;
import com.tqqn.capturetheflag.game.tab.TabScoreboardManager;
import com.tqqn.capturetheflag.game.gamestates.active.tasks.ActiveGameTask;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.game.teams.TeamChatPrefix;
import com.tqqn.capturetheflag.game.teams.TeamColor;
import com.tqqn.capturetheflag.game.teams.TeamTabPrefix;
import org.bukkit.*;
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

    /**
     * Void Method that will init all the game mechanics.
     */
    public void init() {
        this.gameTeamRed = new GameTeam("&cRed", TeamColor.RED, TeamTabPrefix.RED_TAB_PREFIX, TeamChatPrefix.RED_CHAT_PREFIX, 1,  plugin.getPluginConfig().getTeamSpawnLocation("red"), new Flag("&cRed Flag", plugin.getPluginConfig().getTeamFlagSpawnLocation("red"), PluginItems.RED_FLAG.getItemStack().getType(), new Particle.DustOptions(Color.fromRGB(235, 55, 52), 1)), this);
        this.gameTeamBlue = new GameTeam("&9Blue", TeamColor.BLUE, TeamTabPrefix.BLUE_TAB_PREFIX, TeamChatPrefix.BLUE_CHAT_PREFIX, 2,  plugin.getPluginConfig().getTeamSpawnLocation("blue"), new Flag("&9Blue Flag", plugin.getPluginConfig().getTeamFlagSpawnLocation("blue"), PluginItems.BLUE_FLAG.getItemStack().getType(), new Particle.DustOptions(Color.fromRGB(52, 61, 235), 1)), this);
        gameTeamRed.getTeamFlag().setGameTeam(gameTeamRed);
        gameTeamBlue.getTeamFlag().setGameTeam(gameTeamBlue);

        this.arena = new Arena(plugin.getPluginConfig().getLobbySpawnLocation(), gameTeamRed, gameTeamBlue, plugin.getPluginConfig().getMinPlayers(), plugin.getPluginConfig().getMaxPlayers());
        initLobbyItems();
        this.kitSelectorMenu = new KitSelectorMenu();

        registerEvents();
        registerCommands();

        LobbyGameState lobbyGameState = new LobbyGameState(this);
        lobbyGameState.init();
        enabledGameStates.add(lobbyGameState);
    }

    /**
     * Void Method that will shutdown the server.
     */
    public void disableGame() {
        Bukkit.getServer().shutdown();
    }

    /**
     * Void Method that updates the GameState.
     * @param gameState GameState
     */
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

    /**
     * Static Method that returns the current GameState.
     */
    public static GameStates getGameStates() {
        return gameState;
    }

    /**
     * Returns the Arena Object.
     */
    public Arena getArena() {
        return this.arena;
    }

    /**
     * Returns the KitSelectorMenu Object.
     */
    public KitSelectorMenu getKitSelectorMenu() {
        return kitSelectorMenu;
    }

    /**
     * Returns the Red GameTeam.
     */
    public GameTeam getTeamRed() {
        return gameTeamRed;
    }

    /**
     * Returns the Blue GameTeam.
     */
    public GameTeam getTeamBlue() {
        return gameTeamBlue;
    }

    /**
     * Returns the ActiveGameTask time.
     */
    public long getActiveGameTaskTime() {
        if (GameManager.gameState == GameStates.ACTIVE) {
            return ActiveGameTask.getTime();
        }
        return 0;
    }

    /**
     * Void Method that will register events.
     */
    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        //Global Listeners
        pluginManager.registerEvents(new GlobalPlayerDamageListener(), plugin);
        pluginManager.registerEvents(new GlobalDropItemListener(), plugin);
        pluginManager.registerEvents(new GlobalPlayerInventoryListener(), plugin);
        pluginManager.registerEvents(new GlobalPlayerQuitListener(this), plugin);

        //Universal Listeners
        pluginManager.registerEvents(new GlobalBlockBreakListener(), plugin);
        pluginManager.registerEvents(new GlobalBlockPlaceListener(), plugin);
        pluginManager.registerEvents(new GlobalCreatureSpawnListener(), plugin);
        pluginManager.registerEvents(new GlobalFoodLevelChangeListener(), plugin);
    }

    /**
     * Void Method that will register commands.
     */
    private void registerCommands() {
            plugin.getCommand("team").setExecutor(new TeamCommands(this));
            plugin.getCommand("debug").setExecutor(new DebugCommands(this));
            plugin.getCommand("kit").setExecutor(new KitCommands(this));
            plugin.getCommand("shout").setExecutor(new ShoutCommands());
    }

    /**
     * Void Method that will init/create a Map with the lobbyItems.
     */
    public void initLobbyItems() {
        lobbyItems = new HashMap<>();
        lobbyItems.put(3, PluginItems.CHOOSE_RED_TEAM.getItemStack());
        lobbyItems.put(5, PluginItems.CHOOSE_BLUE_TEAM.getItemStack());
    }

    /**
     * Void Method that will give the player the lobby items.
     * @param player Player
     */
    public void giveLobbyItems(Player player) {
        clearPlayerInventory(player);

        for (Map.Entry<Integer, ItemStack> set : lobbyItems.entrySet()) {
            player.getInventory().setItem(set.getKey(), set.getValue());
        }
    }

    /**
     * Void Method that will clear the inventory of the player.
     * @param player Player
     */
    public void clearPlayerInventory(Player player) {
        player.getInventory().clear();
    }

    /**
     * Static void method that will spawn a flag.
     * @param flag Flag
     */
    public static void addSpawnedFlag(Flag flag) {
        spawnedFlags.put(flag.getCurrentLocation(), flag);

        Location flagLocation = new Location(flag.getCurrentLocation().getWorld(), flag.getCurrentLocation().getX(), flag.getCurrentLocation().getY()+1, flag.getCurrentLocation().getZ());
        spawnedFlags.put(flagLocation, flag);
    }

    /**
     * Static Method that returns the flag that is on the given location.
     * @param location Location
     */
    public static Flag getFlag(Location location) {
        return spawnedFlags.get(location);
    }

    /**
     * Static Method that will remove the spawned Flag.
     * @param flag Flag
     */
    public static void removeSpawnedFlag(Flag flag) {
        spawnedFlags.remove(flag.getCurrentLocation());
        Location flagLocation = new Location(flag.getCurrentLocation().getWorld(), flag.getCurrentLocation().getX(), flag.getCurrentLocation().getY()+1, flag.getCurrentLocation().getZ());
        spawnedFlags.remove(flagLocation);
        flag.setCurrentLocation(null);
    }

    /**
     * Static Method that returns if the location is a spawned Flag.
     * @param location Location
     */
    public static boolean isLocationSpawnedFlag(Location location) {
        Location flagLocation = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());
        flagLocation.setY(flagLocation.getY() + 1);

        return spawnedFlags.containsKey(location) || spawnedFlags.containsKey(flagLocation);
    }

    /**
     * Method that returns which team is smaller in players. If both are the same, return null.
     */
    public GameTeam whichTeamIsSmaller() {
        if (gameTeamBlue.getTeamPlayers().size() < gameTeamRed.getTeamPlayers().size()) return gameTeamBlue;
        if (gameTeamBlue.getTeamPlayers().size() > gameTeamRed.getTeamPlayers().size()) return gameTeamRed;
        return null;
    }

    /**
     * Void Method that will get all the no team players and balance these players on the overall teams.
     */
    public void balanceTeamPlayers() {
        for (GamePlayer gamePlayer : arena.getNoTeamPlayers()) {
            GameTeam gameTeam = whichTeamIsSmaller();
            if (gameTeam == null) {
                getTeamRed().addPlayerToTeam(gamePlayer);
            } else {
                gameTeam.addPlayerToTeam(gamePlayer);
            }
        }
    }

    /**
     * Returns the enemy team of the given team.
     * @param player Player
     */
    public GameTeam getEnemyTeam(GamePlayer player) {

        if (player.getTeam() == getTeamRed()) return getTeamBlue();

        return getTeamRed();
    }
}
