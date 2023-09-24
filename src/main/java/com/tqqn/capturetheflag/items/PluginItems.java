package com.tqqn.capturetheflag.items;

import com.tqqn.capturetheflag.nms.NMSUtils;
import com.tqqn.capturetheflag.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum PluginItems {

    //SetupWizard Items
    SETUPWIZARD_SET_LOBBY_LOCATION_ITEM("&cSet Lobby Location", Material.BOOK, new String[]{"&6Sets Lobby Spawn location"}, false, 1),
    SETUPWIZARD_SET_POWERUP_LOCATIONS_ITEM("&cSet PowerUp Location", Material.FEATHER, new String[]{"&6Sets PowerUp locations."}, false, 1),
    SETUPWIZARD_SAVE_ITEM("&cSave", Material.NETHER_STAR, new String[]{"&6Saves locations."}, false, 1),

    SETUPWIZARD_SET_TEAM_BLUE_SPAWN_LOCATION_ITEM("&cSet Blue Spawn Location", Material.BLUE_WOOL, new String[]{"&6Sets Blue Spawn location"}, false, 1),
    SETUPWIZARD_SET_TEAM_BLUE_FLAG_LOCATION_ITEM("&cSet Blue Flag Location", Material.BLUE_BANNER, new String[]{"&6Sets Blue Flag location"}, false, 1),

    SETUPWIZARD_SET_TEAM_RED_SPAWN_LOCATION_ITEM("&cSet Red Spawn Location", Material.RED_WOOL, new String[]{"&6Sets Red Spawn location"}, false, 1),
    SETUPWIZARD_SET_TEAM_RED_FLAG_LOCATION_ITEM("&cSet Red Flag Location", Material.RED_BANNER, new String[]{"&6Sets Red Flag location"}, false, 1),

    //Lobby Items
    CHOOSE_RED_TEAM("&cChoose Red Team &e[RIGHT-CLICK]", Material.RED_WOOL, new String[]{"&eRight Click to choose &cRed Team"}, false, 1),
    CHOOSE_BLUE_TEAM("&9Choose Blue Team &e[RIGHT-CLICK]", Material.BLUE_WOOL, new String[]{"&eRight Click to choose &9Blue Team"}, false, 1),

    //PowerUp Items
    POWERUP_SPEED_ITEM("&cSpeed-PowerUp", Material.FEATHER, null, true, 1),
    POWERUP_JUMP_ITEM("&cJump-PowerUp", Material.RABBIT_FOOT, null, true, 1),
    POWERUP_STRENGHT_ITEM("&cStrenght-PowerUp", Material.DIAMOND_SWORD, null, true, 1),
    POWERUP_REGEN_ITEM("&cRegen-PowerUp", Material.GOLDEN_APPLE, null, true, 1),

    //Team Flags
    RED_FLAG("&cRed Flag", Material.RED_BANNER, null, false, 1),
    BLUE_FLAG("&9Blue Flag", Material.BLUE_BANNER, null, false, 1),

    //Kit Icons
    KIT_ARCHER_ICON("Kit: &cArcher", Material.BOW, null, false, 1),
    KIT_BUILDER_ICON("Kit: &cBuilder", Material.BRICK, null, false, 1),
    KIT_WARRIOR_ICON("Kit: &cWarrior", Material.DIAMOND_SWORD, null, false, 1),

    FLAG_COMPASS("&bTracking: ", Material.COMPASS, null, false, 1);


    private final String displayName;
    private final Material material;
    private final String[] lore;
    private final boolean itemGlow;
    private final int amount;

    PluginItems(String displayName, Material material, String[] lore, boolean itemGlow, int amount) {
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

    public ItemStack setNBTTag(String key, String value) {
        return NMSUtils.applyNMSTag(getItemStack(), key, value);
    }
}
