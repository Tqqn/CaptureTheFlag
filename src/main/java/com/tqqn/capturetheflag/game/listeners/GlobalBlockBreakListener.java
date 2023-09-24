package com.tqqn.capturetheflag.game.listeners;

import com.tqqn.capturetheflag.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GlobalBlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //if (event.getPlayer().isOp()) return;
        event.setCancelled(true);
    }
}
