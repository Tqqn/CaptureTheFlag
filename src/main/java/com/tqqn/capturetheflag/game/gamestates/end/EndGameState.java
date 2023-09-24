package com.tqqn.capturetheflag.game.gamestates.end;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.game.AbstractGameState;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.gamestates.end.tasks.EndGameTask;
import com.tqqn.capturetheflag.game.teams.GameTeam;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.PluginSounds;
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
            PluginSounds.WIN_GAME.playSoundToTeam(gameTeam);

            if (gameTeam == gameManager.getTeamRed()) {
                PluginSounds.LOSE_GAME.playSoundToTeam(gameManager.getTeamBlue());
            } else {
                PluginSounds.LOSE_GAME.playSoundToTeam(gameManager.getTeamRed());
            }

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
