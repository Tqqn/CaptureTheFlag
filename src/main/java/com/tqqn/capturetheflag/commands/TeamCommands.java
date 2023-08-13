package com.tqqn.capturetheflag.commands;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommands  implements CommandExecutor {

    private final GameManager gameManager;

    public TeamCommands(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Wrong Argument.");
            return false;

        } else if (args[0].equalsIgnoreCase("red")) {
            if (gameManager.getArena().getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamRed()) {
                player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));
                return false;
            }
            gameManager.getArena().getGamePlayer(player.getUniqueId()).setTeam(gameManager.getTeamRed());
            player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));
            return true;

        } else if (args[0].equalsIgnoreCase("blue")) {
            if (gameManager.getArena().getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamBlue()) {
                player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
                return false;
            }
            gameManager.getArena().getGamePlayer(player.getUniqueId()).setTeam(gameManager.getTeamBlue());
            player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
            return true;
        }
        return false;
    }

}
