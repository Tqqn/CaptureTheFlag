package com.tqqn.capturetheflag.tasks;

import com.tqqn.capturetheflag.data.GamePoints;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import org.bukkit.scheduler.BukkitRunnable;

public class ActiveGameTask extends BukkitRunnable {


    int time = 0;
    int pointTime = 0;
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

        if (pointTime == 5) {
            gameManager.getArena().addTeamPoints(GamePoints.POINTS_PER_5_SECONDS);
            pointTime = 0;
        }
        time++;
        pointTime++;
    }
}
