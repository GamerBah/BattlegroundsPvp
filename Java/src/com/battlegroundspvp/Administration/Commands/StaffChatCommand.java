package com.battlegroundspvp.Administration.Commands;
/* Created by GamerBah on 8/9/2016 */


import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import lombok.Getter;
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
    @Getter
    private static Set<UUID> toggled = new HashSet<>();
    private Battlegrounds plugin;

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
                    player.sendMessage(ChatColor.GRAY + "Staff Chat toggled " + BoldColor.GREEN.getColor() + "ON");
                    EventSound.playSound(player, EventSound.ACTION_SUCCESS);
                } else {
                    toggled.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + "Staff Chat toggled " + BoldColor.RED.getColor() + "OFF");
                    EventSound.playSound(player, EventSound.ACTION_SUCCESS);
                }
            } else {
                String message = StringUtils.join(args, ' ', 0, args.length);

                plugin.getServer().getOnlinePlayers().stream().filter(players ->
                        plugin.getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                        .forEach(players -> players.sendMessage(BoldColor.YELLOW.getColor() + "[STAFF] "
                                + ChatColor.RED + player.getName() + ": " + message));
            }
        }

        return false;
    }

}
