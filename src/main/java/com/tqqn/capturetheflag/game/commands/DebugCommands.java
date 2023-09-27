package com.tqqn.capturetheflag.game.commands;

import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommands implements CommandExecutor {

    private final GameManager gameManager;

    public DebugCommands(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Wrong Argument.");
            return true;

        } else if (args[0].equalsIgnoreCase("start")) {
            gameManager.setGameState(GameStates.ACTIVE);
        } else if (args[0].equalsIgnoreCase("stop")) {
            gameManager.setGameState(GameStates.END);
        }
        return true;
    }
}
