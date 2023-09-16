package com.tqqn.capturetheflag.setup;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.items.PluginItems;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SetupManager {

    private final CaptureTheFlag plugin;
    private Map<Integer, ItemStack> setupItems;

    private final List<Player> hasSetupItems = new ArrayList<>();

    public SetupManager(CaptureTheFlag plugin) {
        this.plugin = plugin;
        initSetupItems();
        SetupGameState setupGameState = new SetupGameState(this);
        setupGameState.init();
    }

    public void initSetupItems() {
        setupItems = new HashMap<>();
        setupItems.put(1, PluginItems.SETUPWIZARD_SET_LOBBY_LOCATION_ITEM.getItemStack());
        setupItems.put(2, PluginItems.SETUPWIZARD_SET_TEAM_BLUE_SPAWN_LOCATION_ITEM.getItemStack());
        setupItems.put(3, PluginItems.SETUPWIZARD_SET_TEAM_BLUE_FLAG_LOCATION_ITEM.getItemStack());
        setupItems.put(4, PluginItems.SETUPWIZARD_SET_TEAM_RED_SPAWN_LOCATION_ITEM.getItemStack());
        setupItems.put(5, PluginItems.SETUPWIZARD_SET_TEAM_RED_FLAG_LOCATION_ITEM.getItemStack());
        setupItems.put(6, PluginItems.SETUPWIZARD_SET_POWERUP_LOCATIONS_ITEM.getItemStack());
        setupItems.put(7, PluginItems.SETUPWIZARD_SAVE_ITEM.getItemStack());
    }

    public void addSetupItems(Player player) {
        clearPlayerInventory(player);
        for (Map.Entry<Integer, ItemStack> set : setupItems.entrySet()) {
            player.getInventory().setItem(set.getKey(), set.getValue());
        }
        hasSetupItems.add(player);
    }

    public void clearPlayerInventory(Player player) {
        player.getInventory().clear();
    }

    public void removeSetupItems(Player player) {
        clearPlayerInventory(player);
        hasSetupItems.remove(player);
    }

    public boolean hasSetupItems(Player player) {
        return hasSetupItems.contains(player);
    }

    public CaptureTheFlag getPlugin() {
        return plugin;
    }

}
