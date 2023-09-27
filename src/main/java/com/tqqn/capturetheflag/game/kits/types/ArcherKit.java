package com.tqqn.capturetheflag.game.kits.types;

import com.tqqn.capturetheflag.game.kits.AbstractKit;
import com.tqqn.capturetheflag.utils.ItemBuilder;
import com.tqqn.capturetheflag.utils.PluginItems;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcherKit extends AbstractKit {

    public ArcherKit() {
        super("&bArcher", PluginItems.KIT_ARCHER_ICON.getItemStack());
    }

    @Override
    public Map<Integer, ItemStack> getInventoryItems() {
        Map<Integer, ItemStack> inventoryItems = new HashMap<>();
        ItemBuilder archerSword = new ItemBuilder(Material.STONE_SWORD, 1);
        archerSword.setDisplayName("&cArcher Sword");
        archerSword.setLore("&7Sword of a Archer.");
        archerSword.setUnbreakable();

        ItemBuilder archerBow = new ItemBuilder(Material.BOW, 1);
        archerBow.setDisplayName("&cArcher Bow");
        archerBow.setLore("&7Bow of a Archer.");
        archerBow.setUnbreakable();

        archerBow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        archerBow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        inventoryItems.put(0, archerSword.build());
        inventoryItems.put(1, archerBow.build());
        inventoryItems.put(2, PluginItems.FLAG_COMPASS.setNBTTag("flagSelected", "none"));
        inventoryItems.put(9, new ItemStack(Material.ARROW));

        return inventoryItems;
    }

    @Override
    public ItemStack[] getArmorItems() {

        List<ItemStack> itemStacks = new ArrayList<>();

        ItemBuilder archerChestplate = new ItemBuilder(Material.GOLDEN_CHESTPLATE, 1);
        archerChestplate.setDisplayName("&cArcher Chestplate");
        archerChestplate.setLore("&7Chestplate of a Archer.");
        archerChestplate.setUnbreakable();

        archerChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemBuilder archerLeggings = new ItemBuilder(Material.LEATHER_LEGGINGS, 1);
        archerLeggings.setDisplayName("&cArcher Leggings");
        archerLeggings.setLore("&7Leggings of a Archer");
        archerLeggings.setUnbreakable();

        archerLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemBuilder archerBoots = new ItemBuilder(Material.GOLDEN_BOOTS, 1);
        archerBoots.setDisplayName("&cArcher Boots");
        archerBoots.setLore("&7Boots of a Archer.");

        itemStacks.add(archerBoots.build());
        itemStacks.add(archerLeggings.build());
        itemStacks.add(archerChestplate.build());
        itemStacks.add(new ItemStack(Material.AIR));

        return itemStacks.toArray(new ItemStack[itemStacks.size()]);
    }
}
