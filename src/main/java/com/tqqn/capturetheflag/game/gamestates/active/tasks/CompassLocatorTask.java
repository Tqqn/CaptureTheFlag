package com.tqqn.capturetheflag.game.gamestates.active.tasks;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.nms.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CompassLocatorTask extends BukkitRunnable {

    private final GameManager gameManager;

    public CompassLocatorTask(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location flagLocation = null;

            if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;
            if (player.getInventory().getItemInMainHand().getType() != Material.COMPASS) return;

            switch (NMSUtils.getNBTTag(player.getInventory().getItemInMainHand(), "flagSelected").replace("\"", "")) {
                case "red":
                    flagLocation = gameManager.getTeamRed().getTeamFlag().getCurrentLocation();
                    break;
                case "blue":
                    flagLocation = gameManager.getTeamBlue().getTeamFlag().getCurrentLocation();
                    break;
            }
            if (flagLocation != null) {
                player.setCompassTarget(flagLocation);
            }
        }
    }
}
