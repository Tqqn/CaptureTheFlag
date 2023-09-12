package com.tqqn.capturetheflag.powerups.tasks;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.powerups.PowerUp;
import org.bukkit.scheduler.BukkitRunnable;

public class ActivePowerUpTask extends BukkitRunnable {

    private final PowerUp powerUp;

    public ActivePowerUpTask(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    @Override
    public void run() {
        if (!(GameManager.getGameStates() == GameStates.ACTIVE)) {
            cancel();
            powerUp.removePowerUp();
        }

        if (!powerUp.isPowerUpSpawned()) {
            cancel();
        }
        powerUp.pickUp();
    }
}
