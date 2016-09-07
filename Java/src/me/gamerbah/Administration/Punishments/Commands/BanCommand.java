package me.gamerbah.Administration.Punishments.Commands;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import net.gpedro.integrations.slack.SlackMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BanCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public BanCommand(Battlegrounds plugin) {
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

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "/ban <player> <reason>");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = plugin.getPlayerData(plugin.getServer().getOfflinePlayer(args[0]).getUniqueId());
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(targetData.getUuid());
        Player target = Bukkit.getPlayer(targetData.getUuid());

        Punishment.Reason reason = null;
        for (Punishment.Reason type : Punishment.Reason.values()) {
            if (args[1].equalsIgnoreCase(type.toString())) {
                reason = type;
                break;
            }
        }

        if (reason == null) {
            player.sendMessage(ChatColor.RED + "Unknown reason!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (targetData == playerData) {
            player.sendMessage(ChatColor.RED + "You can't ban yourself!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (plugin.getPlayerPunishments().containsKey(targetData.getUuid())) {
            ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(targetData.getUuid());
            if (punishments != null) {
                for (int i = 0; i < punishments.size(); i++) {
                    Punishment punishment = punishments.get(i);
                    if (punishment.getType().equals(Punishment.Type.BAN)) {
                        if (!punishment.isPardoned()) {
                            PlayerData enforcerData = plugin.getPlayerData(punishment.getEnforcer());
                            BaseComponent baseComponent = new TextComponent(ChatColor.RED + "That player is already banned!");
                            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Banned by: "
                                    + enforcerData.getRank().getColor() + "" + ChatColor.BOLD + enforcerData.getRank().getName().toUpperCase()
                                    + ChatColor.WHITE + " " + enforcerData.getName() + "\n" + ChatColor.GOLD + "Reason: "
                                    + ChatColor.WHITE + punishment.getReason().getName() + "\n" + ChatColor.GRAY + "Date: " + ChatColor.AQUA
                                    + punishment.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a '(PST)'"))).create()));
                            player.spigot().sendMessage(baseComponent);
                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                            return true;
                        }
                    }
                }
            }
        }

        plugin.createPunishment(targetData.getUuid(), targetData.getName(), Punishment.Type.BAN, LocalDateTime.now(), -1, player.getUniqueId(), reason);
        plugin.slackPunishments.call(new SlackMessage(">>> _*" + player.getName() + "* banned *" + targetData.getName() + "*_\n*Reason:* _" + reason.getName() + "_"));

        final String finalName = reason.getName();
        if (target != null) {
            BaseComponent baseComponent = new TextComponent(ChatColor.RED + player.getName() + " banned " + ChatColor.RED + plugin.getServer().getPlayer(targetData.getUuid()).getName());
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Reason: "
                    + ChatColor.GOLD + finalName).create()));
            plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff -> staff.spigot().sendMessage(baseComponent));

            target.kickPlayer(ChatColor.RED + "You were banned by " + ChatColor.GOLD + player.getName() + ChatColor.RED + " for " + ChatColor.GOLD + finalName + "\n"
                    + ChatColor.YELLOW + reason.getMessage() + "\n\n" + ChatColor.GRAY + "Appeal your ban on the forums: battlegroundspvp.com/forums");
        } else {
            BaseComponent baseComponent = new TextComponent(ChatColor.RED + player.getName() + " banned " + ChatColor.RED + offlinePlayer.getName());
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Reason: "
                    + ChatColor.GOLD + finalName).create()));

            plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff -> staff.spigot().sendMessage(baseComponent));
        }

        return true;
    }
}
