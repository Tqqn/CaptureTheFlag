package com.tqqn.capturetheflag.game.kits.types;

import com.tqqn.capturetheflag.game.kits.AbstractKit;
import com.tqqn.capturetheflag.items.PluginItems;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ArcherKit extends AbstractKit {

    public ArcherKit() {
        super("&bArcher", PluginItems.KIT_ARCHER_ICON.getItemStack());
    }

    @Override
    public Map<Integer, ItemStack> getInventoryItems() {
        return null;
    }

    @Override
    public ItemStack[] getArmorItems() {
        return new ItemStack[0];
    }
}
