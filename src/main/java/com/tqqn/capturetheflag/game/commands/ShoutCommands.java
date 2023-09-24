package com.tqqn.capturetheflag.game.commands;

import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.data.GamePlayer;
import com.tqqn.capturetheflag.utils.SMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ShoutCommands implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (GameManager.getGameStates() != GameStates.ACTIVE) return true;

        if (args.length > 0) {
            Player player = (Player) sender;
            GamePlayer gamePlayer = Arena.getGamePlayer(player.getUniqueId());
            StringBuilder message = new StringBuilder();
            for(int i = 0; i < args.length; i++) {
                if (i > 0) message.append(" ");
                message.append(args[i]);
            }

            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendMessage(SMessages.SHOUT_CHAT_MESSAGE_FORMAT.getMessage(gamePlayer.getTeam().getTeamTabPrefix().getPrefix(), gamePlayer.getPlayer().getName(), message.toString()));
            }
        }
        return true;
    }
}
