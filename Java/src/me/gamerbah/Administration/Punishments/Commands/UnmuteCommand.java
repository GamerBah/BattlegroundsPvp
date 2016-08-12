package me.gamerbah.Administration.Punishments.Commands;
/* Created by GamerBah on 8/10/2016 */


import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Data.Query;
import me.gamerbah.Utils.EventSound;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class UnmuteCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public UnmuteCommand(Battlegrounds plugin) {
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
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "/unmute <player>");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = plugin.getPlayerData(plugin.getServer().getOfflinePlayer(args[0]).getUniqueId());

        if (targetData == null) {
            player.sendMessage(ChatColor.RED + "That player is not online or doesn't exist!");
            return true;
        }

        if (targetData.getPunishments() != null && targetData.getPunishments().size() != 0) {
            Set<Punishment> remove = new HashSet<>();
            targetData.getPunishments().stream()
                    .filter(punishment -> punishment.getExpiration() == -1 || System.currentTimeMillis() <= (punishment.getTime() + punishment.getExpiration()))
                    .filter(punishment -> punishment.getType().equals(Punishment.PunishType.MUTE))
                    .forEach(remove::add);
            if (remove.size() == 0) {
                player.sendMessage(ChatColor.RED + "That player isn't muted!");
                return true;
            }
            remove.forEach(targetData::removePunishment);
            plugin.getServer().getOnlinePlayers().stream().filter(staff ->
                    plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff ->
                    staff.sendMessage(ChatColor.RED + player.getName() + " unmuted " +
                            plugin.getServer().getOfflinePlayer(targetData.getUuid()).getName() + "."));
        } else {
            player.sendMessage(ChatColor.RED + "That player isn't muted!");
        }

        return true;
    }

}
