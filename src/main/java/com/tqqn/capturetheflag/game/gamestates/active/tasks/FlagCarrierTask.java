package com.tqqn.capturetheflag.game.gamestates.active.tasks;

import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.game.flag.Flag;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class FlagCarrierTask extends BukkitRunnable {

    private final Flag flag;
    private final GamePlayer flagCarrier;

    public FlagCarrierTask(Flag flag, GamePlayer flagCarrier) {
        this.flag = flag;
        this.flagCarrier = flagCarrier;
    }

    @Override
    public void run() {
        if (!flagCarrier.hasFlag()) {
            cancel();
            return;
        }
        flagCarrier.getPlayer().getLocation().getWorld().spawnParticle(Particle.REDSTONE, flagCarrier.getPlayer().getLocation(), 20, 2.5F, 3.5F, 0.5F, 0.0F, flag.getFlagDustOptions());
        flag.setCurrentLocation(flagCarrier.getPlayer().getLocation());
    }
}
