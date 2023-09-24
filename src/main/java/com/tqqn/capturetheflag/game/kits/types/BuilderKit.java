package com.tqqn.capturetheflag.game.kits.types;

import com.tqqn.capturetheflag.game.kits.AbstractKit;
import com.tqqn.capturetheflag.items.PluginItems;
import com.tqqn.capturetheflag.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuilderKit extends AbstractKit {

    public BuilderKit() {
        super("&aBuilder", PluginItems.KIT_BUILDER_ICON.getItemStack());
    }

    @Override
    public Map<Integer, ItemStack> getInventoryItems() {
        Map<Integer, ItemStack> inventoryItems = new HashMap<>();
        ItemBuilder builderSword = new ItemBuilder(Material.IRON_SWORD, 1);
        builderSword.setDisplayName("&cBuilder Sword");
        builderSword.setLore("&7Sword of a Builder.");
        builderSword.setGlow();
        builderSword.setUnbreakable();

        inventoryItems.put(0, builderSword.build());
        inventoryItems.put(1, PluginItems.FLAG_COMPASS.setNBTTag("flagSelected", "none"));

        return inventoryItems;
    }

    @Override
    public ItemStack[] getArmorItems() {

        List<ItemStack> itemStacks = new ArrayList<>();

        // Chestplate
        ItemBuilder builderChestplate = new ItemBuilder(Material.IRON_CHESTPLATE, 1);
        builderChestplate.setDisplayName("&cBuilder Chestplate");
        builderChestplate.setLore("&7Chestplate of a Builder.");
        builderChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        builderChestplate.setUnbreakable();

        // Leggings
        ItemBuilder builderLeggings = new ItemBuilder(Material.IRON_LEGGINGS, 1);
        builderLeggings.setDisplayName("&cBuilder Leggings");
        builderLeggings.setLore("&7Leggings of a Builder.");
        builderLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        builderLeggings.setUnbreakable();

        // Boots
        ItemBuilder builderBoots = new ItemBuilder(Material.IRON_BOOTS, 1);
        builderBoots.setDisplayName("&cBuilder Boots");
        builderBoots.setLore("&7Boots of a Builder.");
        builderBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        builderBoots.setUnbreakable();
        // Armor Contents, starting with boots till helmet
        itemStacks.add(builderBoots.build());
        itemStacks.add(builderLeggings.build());
        itemStacks.add(builderChestplate.build());
        itemStacks.add(new ItemStack(Material.AIR));

        return itemStacks.toArray(new ItemStack[itemStacks.size()]);
    }
}
