package me.gamerbah.Administration.Punishments.Commands;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.EventSound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public MuteCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (command.getName().equalsIgnoreCase("mute")) {
            if (!playerData.hasRank(Rank.HELPER)) {
                plugin.sendNoPermission(player);
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "/mute <player> <reason>");
                plugin.playSound(player, EventSound.COMMAND_FAIL);
                return true;
            }

            PlayerData targetData = plugin.getPlayerData(plugin.getServer().getOfflinePlayer(args[0]).getUniqueId());
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(targetData.getUuid());
            Player target = Bukkit.getPlayer(targetData.getUuid());

            if (targetData == null) {
                player.sendMessage(ChatColor.RED + "That player hasn't ever joined!");
                return true;
            }

            String reason = "";
            for (int i = 1; i < args.length; i++) {
                reason += args[i] + " ";
            }
            reason = reason.trim();

            final String finalReason = reason;
            targetData.addPunishment(new Punishment(targetData.getUuid(), target.getName(), Punishment.PunishType.MUTE, System.currentTimeMillis(), -1, playerData.getUuid(), reason));

            if (target != null) {
                plugin.getServer().getOnlinePlayers().stream().filter(staff ->
                        plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff ->
                        staff.sendMessage("" + ChatColor.RED + player.getName() + " permanently muted "
                                + ChatColor.GRAY + plugin.getServer().getPlayer(targetData.getUuid()).getName() + ChatColor.RED + " for " + finalReason));
                target.sendMessage(ChatColor.RED + "You were permanently muted by " + player.getName() + " for " + reason + "\n"
                        + "Appeal on ...");
            } else {
                plugin.getServer().getOnlinePlayers().stream().filter(staff ->
                        plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff ->
                        staff.sendMessage("" + ChatColor.RED + player.getName() + " permanently muted "
                                + ChatColor.GRAY + offlinePlayer.getName() + ChatColor.RED + " for " + finalReason));
                player.sendMessage(ChatColor.GREEN + "Offline player " + ChatColor.GOLD + offlinePlayer.getName()
                        + ChatColor.GREEN + " successfully muted.");
            }
        }

        return true;
    }
}
