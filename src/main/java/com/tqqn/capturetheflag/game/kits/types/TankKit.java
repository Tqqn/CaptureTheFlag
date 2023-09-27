package com.tqqn.capturetheflag.game.kits.types;

import com.tqqn.capturetheflag.game.kits.AbstractKit;
import com.tqqn.capturetheflag.utils.PluginItems;
import com.tqqn.capturetheflag.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TankKit extends AbstractKit {

    public TankKit() {
        super("&aTank", PluginItems.KIT_TANK_ICON.getItemStack());
    }

    @Override
    public Map<Integer, ItemStack> getInventoryItems() {
        Map<Integer, ItemStack> inventoryItems = new HashMap<>();
        ItemBuilder tankSword = new ItemBuilder(Material.STONE_SWORD, 1);
        tankSword.setDisplayName("&cTank Sword");
        tankSword.setLore("&7Sword of a Tank.");
        tankSword.setGlow();
        tankSword.setUnbreakable();

        inventoryItems.put(0, tankSword.build());
        inventoryItems.put(1, PluginItems.FLAG_COMPASS.setNBTTag("flagSelected", "none"));

        return inventoryItems;
    }

    @Override
    public ItemStack[] getArmorItems() {

        List<ItemStack> itemStacks = new ArrayList<>();

        // Chestplate
        ItemBuilder tankChestplate = new ItemBuilder(Material.IRON_CHESTPLATE, 1);
        tankChestplate.setDisplayName("&cTank Chestplate");
        tankChestplate.setLore("&7Chestplate of a Tank.");
        tankChestplate.setUnbreakable();

        tankChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        // Leggings
        ItemBuilder tankLeggings = new ItemBuilder(Material.IRON_LEGGINGS, 1);
        tankLeggings.setDisplayName("&cTank Leggings");
        tankLeggings.setLore("&7Leggings of a Tank.");
        tankLeggings.setUnbreakable();

        tankLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        // Boots
        ItemBuilder tankBoots = new ItemBuilder(Material.IRON_BOOTS, 1);
        tankBoots.setDisplayName("&cTank Boots");
        tankBoots.setLore("&7Boots of a Tank.");
        tankBoots.setUnbreakable();

        tankBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        // Armor Contents, starting with boots till helmet
        itemStacks.add(tankBoots.build());
        itemStacks.add(tankLeggings.build());
        itemStacks.add(tankChestplate.build());
        itemStacks.add(new ItemStack(Material.AIR));

        return itemStacks.toArray(new ItemStack[itemStacks.size()]);
    }
}
