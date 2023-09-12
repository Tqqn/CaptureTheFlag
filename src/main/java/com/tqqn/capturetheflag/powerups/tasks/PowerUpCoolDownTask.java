package com.tqqn.capturetheflag.powerups.tasks;

import com.tqqn.capturetheflag.CaptureTheFlag;
import org.bukkit.scheduler.BukkitRunnable;

public class PowerUpCoolDownTask extends BukkitRunnable {

    private int cooldown = 30;


    @Override
    public void run() {

        if (cooldown == 0) {
            cancel();
            CaptureTheFlag.getInstance().getGameManager().getArena().spawnRandomPowerUp();
        }

        cooldown--;
    }
}
