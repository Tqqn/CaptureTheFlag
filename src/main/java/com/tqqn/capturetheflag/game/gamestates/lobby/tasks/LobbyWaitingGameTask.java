package com.tqqn.capturetheflag.game.gamestates.lobby.tasks;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.utils.GameUtils;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.PluginSounds;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyWaitingGameTask extends BukkitRunnable {

    private final GameManager gameManager;

    private static int time = 300;
    private boolean canStart = false;

    public LobbyWaitingGameTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        if (gameManager.getArena().canStart() && time == 0) {
            cancel();
            gameManager.setGameState(GameStates.ACTIVE);
            return;
        }

        if (gameManager.getArena().canStart()) {
            if (!canStart) {
                time = 30;
                canStart = true;
            }
            if (time <= 5) {
                GameUtils.broadcastMessage(SMessages.GAME_START_COUNTDOWN.getMessage(String.valueOf(time)));
                PluginSounds.COUNTDOWN_SOUND.playSoundForAll();
            }
        } else {
            if (canStart) {
                canStart = false;
                time = 300;
            }
        }

        if (time == 0) {
            GameUtils.broadcastMessage(NMessages.LOBBY_NOT_ENOUGH_PLAYERS.getMessage());
            PluginSounds.COUNTDOWN_SOUND.playSoundForAll();
            time = 300;
        }
        time--;
    }

    public static int getWaitingTime() {
        return time;
    }
}
