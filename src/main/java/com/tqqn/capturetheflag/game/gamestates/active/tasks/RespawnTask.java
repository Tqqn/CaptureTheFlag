package com.tqqn.capturetheflag.game.gamestates.active.tasks;

import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.nms.NMSUtils;
import com.tqqn.capturetheflag.utils.SMessages;
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
            gamePlayer.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            gamePlayer.spawn(gamePlayer.getTeam().getSpawnLocation());
            return;
        }
        NMSUtils.sendTitleMessage(gamePlayer.getPlayer(), SMessages.RESPAWN_TIMER_MESSAGE.getMessage(String.valueOf(timer)),"", 20, 20, 20);
        gamePlayer.getPlayer().sendMessage(SMessages.RESPAWN_TIMER_MESSAGE.getMessage(String.valueOf(timer)));
        timer--;
    }
}
