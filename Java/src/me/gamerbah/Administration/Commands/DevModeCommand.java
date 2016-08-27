package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/26/2016 */

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevModeCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public DevModeCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.OWNER)) {
            plugin.sendNoPermission(player);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Code required.");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (!args[0].equals("GamerBahxxRiteTurnOnlyxxAvoriz")) {
            player.sendMessage(ChatColor.RED + "Incorrect code.");
        }

        if (!plugin.getConfig().getBoolean("developmentMode")) {
            for (Player players : plugin.getServer().getOnlinePlayers()) {
                PlayerData playerData1 = plugin.getPlayerData(players.getUniqueId());
                if (!playerData1.hasRank(Rank.HELPER)) {
                    players.kickPlayer(ChatColor.RED + "You were kicked because the server was put into\n" + BoldColor.GOLD.getColor()
                            + "DEVELOPMENT MODE\n\n" + ChatColor.AQUA + "This means that we are fixing bugs, or found another issue we needed to take care of\n\n"
                            + ChatColor.GRAY + "We put the server into Development mode in order to reduce the risk of\n§7corrupting player data, etc. The server should be open shortly!");
                }
                plugin.playSound(players, EventSound.COMMAND_SUCCESS);
            }
            plugin.getConfig().set("developmentMode", true);
            plugin.saveConfig();
            plugin.getServer().broadcastMessage(BoldColor.RED.getColor() + "\nSERVER HAS BEEN PUT INTO " + BoldColor.GOLD.getColor() + "DEVELOPMENT MODE\n ");
        } else {
            plugin.getConfig().set("developmentMode", false);
            plugin.saveConfig();
            for (Player players : plugin.getServer().getOnlinePlayers()) {
                players.sendMessage(BoldColor.RED.getColor() + "\nSERVER IS NO LONGER IN " + BoldColor.GOLD.getColor() + "DEVELOPMENT MODE\n ");
                plugin.playSound(players, EventSound.COMMAND_SUCCESS);
            }
        }

        return false;
    }

}