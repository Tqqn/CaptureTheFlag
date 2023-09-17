package com.tqqn.capturetheflag.game.gamestates.active.tasks;

import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.nms.NMSUtils;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTask extends BukkitRunnable {

    private int timer = 10;
    private final GamePlayer gamePlayer;

    public RespawnTask(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void run() {
        if (timer == 0) {
            cancel();
            gamePlayer.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
            gamePlayer.getPlayer().removePotionEffect(PotionEffectType.SLOW);
            gamePlayer.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            gamePlayer.spawn(gamePlayer.getTeam().getSpawnLocation());
            NMSUtils.sendTitleMessage(gamePlayer.getPlayer(), NMessages.RESPAWN_MESSAGE_TITLE.getMessage(), "", 20, 20, 10);
            return;
        }

        if (timer == 5) {
            gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2000000, 2));
            gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2000000, 5));
            gamePlayer.getPlayer().setFlying(false);
            gamePlayer.getPlayer().setAllowFlight(false);
            gamePlayer.getPlayer().teleport(gamePlayer.getTeam().getSpawnLocation());
        }
        NMSUtils.sendTitleMessage(gamePlayer.getPlayer(), NMessages.RESPAWN_TIMER_TITLE.getMessage(), SMessages.RESPAWN_TIMER_SUBTITLE.getMessage(String.valueOf(timer)), 20, 20, 20);
        gamePlayer.getPlayer().sendMessage(SMessages.RESPAWN_TIMER_MESSAGE.getMessage(String.valueOf(timer)));
        timer--;
    }
}
