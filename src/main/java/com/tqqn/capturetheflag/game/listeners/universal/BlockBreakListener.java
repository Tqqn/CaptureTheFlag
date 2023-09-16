package com.tqqn.capturetheflag.game.listeners.universal;

import com.tqqn.capturetheflag.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final GameManager gameManager;

    public BlockBreakListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }
}
