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

public class WarriorKit extends AbstractKit {

    public WarriorKit() {
        super("&cWarrior", PluginItems.KIT_WARRIOR_ICON.getItemStack());
    }

    @Override
    public Map<Integer, ItemStack> getInventoryItems() {
        Map<Integer, ItemStack> inventoryItems = new HashMap<>();
        ItemBuilder warriorSword = new ItemBuilder(Material.DIAMOND_SWORD, 1);
        warriorSword.setDisplayName("&cWarrior Sword");
        warriorSword.setLore("&7Sword of a Warrior.");
        warriorSword.addEnchantment(Enchantment.DURABILITY, 3);

        inventoryItems.put(0, warriorSword.build());

        return inventoryItems;
    }

    @Override
    public ItemStack[] getArmorItems() {

        List<ItemStack> itemStacks = new ArrayList<>();

        // Chestplate
        ItemBuilder warriorChestPlate = new ItemBuilder(Material.LEATHER_CHESTPLATE, 1);
        warriorChestPlate.setDisplayName("&cWarrior Chestplate");
        warriorChestPlate.setLore("&7Chestplate of a Warrior.");
        warriorChestPlate.addEnchantment(Enchantment.DURABILITY, 3);
        warriorChestPlate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        // Leggings
        ItemBuilder warriorLeggings = new ItemBuilder(Material.LEATHER_LEGGINGS, 1);
        warriorLeggings.setDisplayName("&cWarrior Leggings");
        warriorLeggings.setLore("&7Leggings of a Warrior.");
        warriorLeggings.addEnchantment(Enchantment.DURABILITY, 3);
        warriorLeggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        // Boots
        ItemBuilder warriorBoots = new ItemBuilder(Material.IRON_BOOTS, 1);
        warriorBoots.setDisplayName("&cWarrior Boots");
        warriorBoots.setLore("&7Boots of a Warrior.");
        warriorBoots.addEnchantment(Enchantment.DURABILITY, 3);
        warriorBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        // Armor Contents, starting with boots till helmet
        itemStacks.add(warriorBoots.build());
        itemStacks.add(warriorLeggings.build());
        itemStacks.add(warriorChestPlate.build());
        itemStacks.add(new ItemStack(Material.AIR));

        return itemStacks.toArray(new ItemStack[itemStacks.size()]);
    }
}
