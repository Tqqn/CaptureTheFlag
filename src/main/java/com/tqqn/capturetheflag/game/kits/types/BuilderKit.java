package com.tqqn.capturetheflag.game.kits.types;

import com.tqqn.capturetheflag.game.kits.AbstractKit;
import com.tqqn.capturetheflag.items.PluginItems;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BuilderKit extends AbstractKit {

    public BuilderKit() {
        super("&aBuilder", PluginItems.KIT_BUILDER_ICON.getItemStack());
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
