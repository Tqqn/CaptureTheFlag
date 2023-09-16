package com.tqqn.capturetheflag.setup.commands;

import com.tqqn.capturetheflag.setup.SetupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommands implements CommandExecutor {

    private final SetupManager setupManager;

    public SetupCommands(SetupManager setupManager) {
        this.setupManager = setupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!setupManager.hasSetupItems(player)) {
            setupManager.addSetupItems(player);
        } else {
            setupManager.removeSetupItems(player);
        }
        return true;
    }

}
