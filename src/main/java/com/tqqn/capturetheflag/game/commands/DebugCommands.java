package com.tqqn.capturetheflag.game.commands;

import com.tqqn.capturetheflag.game.GameStates;
import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.flag.FlagStatus;
import com.tqqn.capturetheflag.game.GameManager;
import com.tqqn.capturetheflag.nms.NMSArmorStand;
import com.tqqn.capturetheflag.game.tab.TabScoreboardManager;
import com.tqqn.capturetheflag.utils.NMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommands implements CommandExecutor {

    private final GameManager gameManager;
    private final TabScoreboardManager tabScoreboardManager;

    public DebugCommands(GameManager gameManager) {
        this.gameManager = gameManager;
        this.tabScoreboardManager = gameManager.getTabScoreboardManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Wrong Argument.");
            return true;

        } else if (args[0].equalsIgnoreCase("flag")) {
            if (args[1].equalsIgnoreCase("blue")) {
                gameManager.getTeamBlue().getTeamFlag().setFlagStatus(FlagStatus.STOLEN);
                return true;
            } else if (args[1].equalsIgnoreCase("red")) {
                gameManager.getTeamRed().getTeamFlag().setFlagStatus(FlagStatus.STOLEN);
                return true;
            }

        } else if (args[0].equalsIgnoreCase("kills")) {
            gameManager.getArena().getGamePlayer(player.getUniqueId()).addKill();
        } else if (args[0].equalsIgnoreCase("start")) {
            gameManager.setGameState(GameStates.ACTIVE);
        } else if (args[0].equalsIgnoreCase("kill")) {
            TabScoreboardManager.setPlayerSpectatorTab(gameManager.getArena().getGamePlayer(player.getUniqueId()));
        } else if (args[0].equalsIgnoreCase("spawnflag")) {
            Arena.getGamePlayer(player.getUniqueId()).getTeam().spawnTeamFlagOnSpawn();
        } else if (args[0].equalsIgnoreCase("item")) {
            //NMSItem nmsItem = new NMSItem(PluginItems.POWERUP_JUMP_ITEM.getItemStack());
            //nmsItem.sendSpawnItemPacketToPlayer(player, player.getLocation());

            NMSArmorStand nmsItemArmorStand = new NMSArmorStand(null);
            nmsItemArmorStand.sendSpawnArmorStandPacketToPlayer(player, player.getLocation(), true);

            NMSArmorStand nmsItemHologram = new NMSArmorStand(NMessages.POWERUP_SPEED.getMessage());
            nmsItemHologram.sendSpawnArmorStandPacketToPlayer(player, player.getLocation(), true);

            //int[] entityIds = new int[]{nmsItem.getEntityId(player), nmsItemHologram.getEntityId(player)};
            //nmsItemArmorStand.sendMountPacket(player, entityIds);
        }
        return true;
    }
}
