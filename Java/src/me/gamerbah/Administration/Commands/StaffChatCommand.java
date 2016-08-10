package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/9/2016 */


import lombok.Getter;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChatCommand implements CommandExecutor {
    private Battlegrounds plugin;
    @Getter
    private static Set<UUID> toggled = new HashSet<>();

    public StaffChatCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.HELPER)) {
            plugin.sendNoPermission(player);
        } else {
            if (args.length == 0) {
                if (!toggled.contains(player.getUniqueId())) {
                    toggled.add(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + "Staff Chat toggled " + ChatColor.GREEN + "" + ChatColor.BOLD + "ON");
                    plugin.playSound(player, EventSound.COMMAND_SUCCESS);
                } else {
                    toggled.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + "Staff Chat toggled " + ChatColor.RED + "" + ChatColor.BOLD + "OFF");
                    plugin.playSound(player, EventSound.COMMAND_SUCCESS);
                }
            } else {
                String message = StringUtils.join(args, ' ', 0, args.length);

                plugin.getServer().getOnlinePlayers().stream().filter(players ->
                        plugin.getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                        .forEach(players -> players.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[STAFF] "
                                + ChatColor.RED + player.getName() + ": " + message));
            }
        }

        return false;
    }

}
