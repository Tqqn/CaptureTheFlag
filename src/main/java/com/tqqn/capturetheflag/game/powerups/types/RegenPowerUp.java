package com.tqqn.capturetheflag.game.powerups.types;

import com.tqqn.capturetheflag.game.powerups.PowerUp;
import com.tqqn.capturetheflag.utils.PluginItems;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class RegenPowerUp extends PowerUp {

    public RegenPowerUp(Location location) {
        super(PluginItems.POWERUP_REGEN_ITEM.getItemStack(), NMessages.POWERUP_REGEN.getMessage(), location);
    }

    @Override
    public void givePowerUp(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 120, 2));
        player.sendMessage(SMessages.POWERUP_PICKUP.getMessage(getDisplayName()));
    }
}
