package com.tqqn.capturetheflag.game.commands;

import com.tqqn.capturetheflag.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommands implements CommandExecutor {

    private final GameManager gameManager;

    public KitCommands(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
            gameManager.getKitSelectorMenu().open(player);
            return true;
    }
}
