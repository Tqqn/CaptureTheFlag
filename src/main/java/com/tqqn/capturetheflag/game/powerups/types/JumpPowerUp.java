package com.tqqn.capturetheflag.game.powerups.types;

import com.tqqn.capturetheflag.utils.PluginItems;
import com.tqqn.capturetheflag.game.powerups.PowerUp;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class JumpPowerUp extends PowerUp {

    public JumpPowerUp(Location location) {
        super(PluginItems.POWERUP_JUMP_ITEM.getItemStack(), NMessages.POWERUP_JUMP.getMessage(), location);
    }

    @Override
    public void givePowerUp(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 240, 2)); //duration = ticks = 120 ticks = 12 seconds
        player.sendMessage(SMessages.POWERUP_PICKUP.getMessage(getDisplayName()));
    }
}
