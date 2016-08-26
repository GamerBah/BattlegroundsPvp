package me.gamerbah.Administration.Punishments.Commands;
/* Created by GamerBah on 8/26/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Data.Query;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UnbanCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public UnbanCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.ADMIN)) {
            plugin.sendNoPermission(player);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "/unban <player>");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = plugin.getPlayerData(plugin.getServer().getOfflinePlayer(args[0]).getUniqueId());

        if (targetData == null) {
            player.sendMessage(ChatColor.RED + "That player is not online or doesn't exist!");
            return true;
        }

        ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(targetData.getUuid());

        Punishment p = null;
        for (int i = 0; i < punishments.size(); i++) {
            Punishment punishment = punishments.get(i);
            if (punishment.getType().equals(Punishment.Type.BAN)) {
                if (!punishment.isPardoned()) {
                    p = punishment;
                    Battlegrounds.getSql().executeUpdate(Query.UPDATE_PUNISHMENT_PARDONED, true, targetData.getUuid().toString(), punishment.getType().toString(), punishment.getDate().toString());
                    punishment.setPardoned(true);
                }
            }
        }

        if (p == null) {
            player.sendMessage(ChatColor.RED + "That player isn't muted!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff ->
                staff.sendMessage(ChatColor.RED + player.getName() + " unbanned " + targetData.getName()));

        return true;
    }
}
