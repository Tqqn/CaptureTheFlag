package com.tqqn.capturetheflag.game.commands;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.utils.NMessages;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommands implements CommandExecutor {

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

        }
        switch (args[0].toUpperCase()) {
            case "RED":
                if (gameManager.whichTeamIsSmaller() == gameManager.getTeamRed()) {
                    if (Arena.getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamRed()) {
                        player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));
                        return false;
                    }
                    Arena.getGamePlayer(player.getUniqueId()).setTeam(gameManager.getTeamRed());
                    player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamRed().getDisplayName()));
                    return true;
                }
                player.sendMessage(NMessages.TEAM_IS_FULL.getMessage());
            case "BLUE":
                if (gameManager.whichTeamIsSmaller() == gameManager.getTeamBlue()) {
                    if (Arena.getGamePlayer(player.getUniqueId()).getTeam() == gameManager.getTeamBlue()) {
                        player.sendMessage(SMessages.ALREADY_IN_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
                        return false;
                    }
                    Arena.getGamePlayer(player.getUniqueId()).setTeam(gameManager.getTeamBlue());
                    player.sendMessage(SMessages.CHOOSE_TEAM.getMessage(gameManager.getTeamBlue().getDisplayName()));
                    return true;
                }
                player.sendMessage(NMessages.TEAM_IS_FULL.getMessage());
        }
        return false;
    }

}
