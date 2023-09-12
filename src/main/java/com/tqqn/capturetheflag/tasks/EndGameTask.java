package com.tqqn.capturetheflag.tasks;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.scheduler.BukkitRunnable;

public final class EndGameTask extends BukkitRunnable {

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
        GameUtils.broadcastMessage(SMessages.GAME_RESTART_COUNTDOWN.getMessage(String.valueOf(time)));
        time--;
    }
}
