package com.tqqn.capturetheflag.game.gamestates.end;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.AbstractGameState;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.tasks.EndGameTask;
import com.tqqn.capturetheflag.teams.GameTeam;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.SMessages;

public class EndGameState extends AbstractGameState {
    private final GameManager gameManager;
    private EndGameTask endGameTask;

    public EndGameState(GameManager gameManager) {
        super("End");
        this.gameManager = gameManager;
    }

    @Override
    public void register() {
        if (gameManager.getArena().whoIsWinner() != null) {
            GameTeam gameTeam = gameManager.getArena().whoIsWinner();
            GameUtils.broadcastMessage(SMessages.TEAM_WIN.getMessage(gameTeam.getTeamColor().getColor(), gameTeam.getDisplayName()));
        } else {
            GameUtils.broadcastMessage("Draw.");
        }
        this.endGameTask = new EndGameTask(gameManager);
        this.endGameTask.runTaskTimer(CaptureTheFlag.getInstance(), 0, 20L);
    }

    @Override
    public void unRegister() {
        if (!endGameTask.isCancelled()) endGameTask.cancel();
    }
}
