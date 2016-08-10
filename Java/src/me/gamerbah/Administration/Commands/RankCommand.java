package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RankCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public RankCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
            if (!playerData.hasRank(Rank.OWNER)) {
                if (player.getUniqueId().equals("dc815235-c651-4f55-b6a5-81ea33d16397")) {
                    return false;
                } else {
                    plugin.sendNoPermission(player);
                    return true;
                }
            }
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "/rank <player> <rank>");
            if (sender instanceof Player) plugin.playSound((Player) sender, EventSound.COMMAND_FAIL);
            return true;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "That player doesn't exist!");
                if (sender instanceof Player) plugin.playSound((Player) sender, EventSound.COMMAND_FAIL);
                return;
            }

            for (Rank rank : Rank.values()) {
                if (rank.toString().equalsIgnoreCase(args[1])) {
                    PlayerData playerData = plugin.getPlayerData(target.getUniqueId());
                    if (playerData != null) {
                        playerData.setRank(rank);
                        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Success! " + ChatColor.GRAY + target.getName() + "'s rank was changed to " + WordUtils.capitalizeFully(rank.toString()));
                        return;
                    } else {
                        sender.sendMessage(ChatColor.RED + "That player doesn't exist!");
                        if (sender instanceof Player) plugin.playSound((Player) sender, EventSound.COMMAND_FAIL);
                        return;
                    }
                }
            }

            sender.sendMessage(ChatColor.RED + "That rank doesn't exist! Try one of these: " + WordUtils.capitalizeFully(Arrays.toString(Rank.values()).replace("[", "").replace("]", "")));
            if (sender instanceof Player) plugin.playSound((Player) sender, EventSound.COMMAND_FAIL);
        });

        return true;
    }

}
