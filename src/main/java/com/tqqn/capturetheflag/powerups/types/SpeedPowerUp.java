package com.tqqn.capturetheflag.powerups.types;

import com.tqqn.capturetheflag.items.GameItems;
import com.tqqn.capturetheflag.powerups.PowerUp;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPowerUp extends PowerUp {

    public SpeedPowerUp(Location location) {
        super(GameItems.POWERUP_SPEED_ITEM.getItemStack(), NMessages.POWERUP_SPEED.getMessage(), location);
    }

    @Override
    public void givePowerUp(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 240, 2)); //duration = ticks = 120 ticks = 12 seconds
        player.sendMessage(SMessages.POWERUP_PICKUP.getMessage(getDisplayName()));
    }

}