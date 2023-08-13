package com.tqqn.capturetheflag.tasks;

import com.tqqn.capturetheflag.game.GameManager;
import org.bukkit.scheduler.BukkitRunnable;

public class EndGameTask extends BukkitRunnable {

    private final GameManager gameManager;
    private int time = 10;

    public EndGameTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (time == 0) {

            gameManager.disableGame();
        }
    }
}
