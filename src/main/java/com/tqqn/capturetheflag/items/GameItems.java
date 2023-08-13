package com.tqqn.capturetheflag.items;

import com.tqqn.capturetheflag.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GameItems {

    //SetupWizard Items
    SETUPWIZARD_SET_LOBBY_LOCATION_ITEM("&cSet Lobby Location", Material.BOOK, new String[]{"&6Sets Lobby Spawn location"}, true, 1),
    SETUPWIZARD_SET_POWERUP_LOCATIONS_ITEM("&cSet PowerUp Location", Material.FEATHER, new String[]{"&6Sets PowerUp locations."}, true, 1),
    SETUPWIZARD_SAVE_ITEM("&cSave", Material.NETHER_STAR, new String[]{"&6Saves locations."}, true, 1),
    SETUPWIZARD_SET_ARENA_LOCATION_ITEM("&cSet Arena Locations", Material.FEATHER, new String[]{"&6Sets Arena Spawn locations"}, true, 1),

    //Lobby Items
    CHOOSE_RED_TEAM("&cChoose Red Team &e[RIGHT-CLICK]", Material.RED_WOOL, new String[]{"&eRight Click to choose &cRed Team"}, false, 1),
    CHOOSE_BLUE_TEAM("&9Choose Blue Team &e[RIGHT-CLICK]", Material.BLUE_WOOL, new String[]{"&eRight Click to choose &9Blue Team"}, false, 1),

    //Player Kit Items
    PLAYER_ARROW("&cCTF Arrow", Material.ARROW, null, false, 10),
    PLAYER_BOW("&cCTF Bow", Material.BOW, null, false, 1),

    //PowerUp Items
    POWERUP_SPEED_ITEM("&cSpeed-PowerUp", Material.FEATHER, null, true, 1),
    POWERUP_JUMP_ITEM("&cJump-PowerUp", Material.RABBIT_FOOT, null, true, 1),
    POWERUP_TRACKER_ITEM("&cTracker-PowerUp", Material.BLAZE_POWDER, null, true, 1);

    private final String displayName;
    private final Material material;
    private final String[] lore;
    private final boolean itemGlow;
    private final int amount;

    GameItems(String displayName, Material material, String[] lore, boolean itemGlow, int amount) {
        this.displayName = displayName;
        this.material = material;
        this.lore = lore;
        this.itemGlow = itemGlow;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        ItemBuilder itemBuilder = new ItemBuilder(this.material, amount);
        itemBuilder.setDisplayName(this.displayName);
        if (this.lore != null) {
            itemBuilder.setLore(this.lore);
        }
        if (this.itemGlow) {
            itemBuilder.setGlow();
        }
        return itemBuilder.build();
    }

}