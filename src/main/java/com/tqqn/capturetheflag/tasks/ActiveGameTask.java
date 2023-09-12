package com.tqqn.capturetheflag.tasks;

import com.tqqn.capturetheflag.data.GamePoints;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.scheduler.BukkitRunnable;

public final class ActiveGameTask extends BukkitRunnable {


    private static int time = 120;
    private int pointTime = 0;
    private final GameManager gameManager;

    public ActiveGameTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {

        if (gameManager.getArena().isThereAWinner() != null) {
            cancel();
            gameManager.setGameState(GameStates.END);
        }

        if (time <= 10) {
            GameUtils.broadcastMessage(SMessages.GAME_END_COUNTDOWN.getMessage(String.valueOf(time)));
        }

        if (time == 0) {
            cancel();
            gameManager.setGameState(GameStates.END);
        }

        if (pointTime == 5) {
            gameManager.getArena().addTeamPoints(GamePoints.POINTS_PER_5_SECONDS);
            pointTime = 0;
        }
        time--;
        pointTime++;
    }

    public static long getTime() {
        return time;
    }
}
