package com.tqqn.capturetheflag.game.gamestates.setup;

import com.tqqn.capturetheflag.game.AbstractGameState;
import com.tqqn.capturetheflag.items.GameItems;
import com.tqqn.capturetheflag.setupwizard.SetupManager;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.PluginConfig;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SetupGameState extends AbstractGameState {

    private final SetupManager setupManager;
    private final PluginConfig pluginConfig;

    private Location lobbyLocation;
    private Location blueSpawnLocation;
    private Location blueFlagLocation;
    private Location redSpawnLocation;
    private Location redFlagLocation;
    private final List<Location> powerUpLocations = new ArrayList<>();


    public SetupGameState(SetupManager setupManager) {
        super("Setup");
        this.setupManager = setupManager;
        this.pluginConfig = setupManager.getPlugin().getPluginConfig();
    }

    @Override
    public void register() {
    }

    @Override
    public void unRegister() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(NMessages.SETUPWIZARD_JOIN_PLAYER_MESSAGE.getMessage());
        event.getPlayer().setGameMode(GameMode.CREATIVE);
        event.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().getInventory().clear();
        setupManager.removeSetupItems(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().isSimilar(GameItems.SETUPWIZARD_SET_LOBBY_LOCATION_ITEM.getItemStack())
            || event.getCurrentItem().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_BLUE_SPAWN_LOCATION_ITEM.getItemStack())
            || event.getCurrentItem().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_BLUE_FLAG_LOCATION_ITEM.getItemStack())
            || event.getCurrentItem().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_RED_SPAWN_LOCATION_ITEM.getItemStack())
            || event.getCurrentItem().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_RED_FLAG_LOCATION_ITEM.getItemStack())
            || event.getCurrentItem().isSimilar(GameItems.SETUPWIZARD_SET_POWERUP_LOCATIONS_ITEM.getItemStack())
            || event.getCurrentItem().isSimilar(GameItems.SETUPWIZARD_SAVE_ITEM.getItemStack())) event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().isSimilar(GameItems.SETUPWIZARD_SET_LOBBY_LOCATION_ITEM.getItemStack())
                || event.getItemDrop().getItemStack().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_BLUE_SPAWN_LOCATION_ITEM.getItemStack())
                || event.getItemDrop().getItemStack().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_BLUE_FLAG_LOCATION_ITEM.getItemStack())
                || event.getItemDrop().getItemStack().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_RED_SPAWN_LOCATION_ITEM.getItemStack())
                || event.getItemDrop().getItemStack().isSimilar(GameItems.SETUPWIZARD_SET_TEAM_RED_FLAG_LOCATION_ITEM.getItemStack())
                || event.getItemDrop().getItemStack().isSimilar(GameItems.SETUPWIZARD_SET_POWERUP_LOCATIONS_ITEM.getItemStack())
                || event.getItemDrop().getItemStack().isSimilar(GameItems.SETUPWIZARD_SAVE_ITEM.getItemStack())) event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (GameItems.SETUPWIZARD_SET_LOBBY_LOCATION_ITEM.getItemStack().isSimilar(itemStack)) {
                lobbyLocation = player.getLocation();
                player.sendMessage(SMessages.SETUPWIZARD_SELECTED_LOCATION.getMessage("&cLobby Location"));
                event.setCancelled(true);
            }
            if (GameItems.SETUPWIZARD_SET_TEAM_BLUE_SPAWN_LOCATION_ITEM.getItemStack().isSimilar(itemStack)) {
                blueSpawnLocation = player.getLocation();
                player.sendMessage(SMessages.SETUPWIZARD_SELECTED_LOCATION.getMessage("&cBlue Spawn Location"));
                event.setCancelled(true);
            }
            if (GameItems.SETUPWIZARD_SET_TEAM_BLUE_FLAG_LOCATION_ITEM.getItemStack().isSimilar(itemStack)) {
                blueFlagLocation = player.getLocation();
                player.sendMessage(SMessages.SETUPWIZARD_SELECTED_LOCATION.getMessage("&cBlue Flag Location"));
                event.setCancelled(true);
            }
            if (GameItems.SETUPWIZARD_SET_TEAM_RED_SPAWN_LOCATION_ITEM.getItemStack().isSimilar(itemStack)) {
                redSpawnLocation = player.getLocation();
                player.sendMessage(SMessages.SETUPWIZARD_SELECTED_LOCATION.getMessage("&cRed Spawn Location"));
                event.setCancelled(true);
            }
            if (GameItems.SETUPWIZARD_SET_TEAM_RED_FLAG_LOCATION_ITEM.getItemStack().isSimilar(itemStack)) {
                redFlagLocation = player.getLocation();
                player.sendMessage(SMessages.SETUPWIZARD_SELECTED_LOCATION.getMessage("&cRed Flag Location"));
                event.setCancelled(true);
            }
            if (GameItems.SETUPWIZARD_SET_POWERUP_LOCATIONS_ITEM.getItemStack().isSimilar(itemStack)) {
                powerUpLocations.add(player.getLocation());
                player.sendMessage(SMessages.SETUPWIZARD_SELECTED_LOCATION.getMessage("&cPowerUp Location"));
                event.setCancelled(true);
            }
            if (GameItems.SETUPWIZARD_SAVE_ITEM.getItemStack().isSimilar(itemStack)) {
                if (lobbyLocation == null || blueSpawnLocation == null || blueFlagLocation == null || redSpawnLocation == null || redFlagLocation == null || powerUpLocations.isEmpty()) {
                    player.sendMessage(NMessages.SETUPWIZARD_LOCATION_NULL.getMessage());
                    event.setCancelled(true);
                    return;
                }

                pluginConfig.saveLobbyLocation(lobbyLocation);
                pluginConfig.saveBlueSpawnLocation(blueSpawnLocation);
                pluginConfig.saveBlueFlagLocation(blueFlagLocation);
                pluginConfig.saveRedSpawnLocation(redSpawnLocation);
                pluginConfig.saveRedFlagLocation(redFlagLocation);
                pluginConfig.savePowerUpLocations(powerUpLocations);

                player.sendMessage(NMessages.SETUPWIZARD_SAVE_LOCATION.getMessage());

                lobbyLocation = null;
                blueSpawnLocation = null;
                blueFlagLocation = null;
                redSpawnLocation = null;
                redFlagLocation = null;
                powerUpLocations.clear();

            }
        }
    }
}
