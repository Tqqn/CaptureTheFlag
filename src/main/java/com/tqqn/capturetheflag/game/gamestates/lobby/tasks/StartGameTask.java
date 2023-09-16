package com.tqqn.capturetheflag.game.gamestates.lobby.tasks;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.scheduler.BukkitRunnable;

public final class StartGameTask extends BukkitRunnable {

    private final GameManager gameManager;
    private int time;

    public StartGameTask(GameManager gameManager) {
        this.gameManager = gameManager;
        this.time = gameManager.getArena().getGameStartCountdown();
    }


    @Override
    public void run() {
        if (time == 0) {
            cancel();
            gameManager.setGameState(GameStates.ACTIVE);
            GameUtils.broadcastMessage(NMessages.GAME_START.getMessage());
            return;
        }
        if (time <= 5) {
            GameUtils.broadcastMessage(SMessages.GAME_START_COUNTDOWN.getMessage(String.valueOf(time)));
        }
        time--;
    }
}
