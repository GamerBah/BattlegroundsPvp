package me.gamerbah.Administration.Punishments.Commands;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Time;
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
import java.util.ArrayList;

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
                plugin.playSound(player, EventSound.COMMAND_FAIL);
                return true;
            }

            ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(target.getUniqueId());

            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (punishment.getType().equals(Punishment.Type.MUTE)) {
                    if (!punishment.isPardoned()) {
                        BaseComponent baseComponent = new TextComponent(ChatColor.RED + "That player is already muted!");
                        baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Muted by: "
                                + ChatColor.WHITE + plugin.getServer().getPlayer(punishment.getEnforcer()).getName() + "\n" + ChatColor.GRAY + "Reason: "
                                + ChatColor.WHITE + punishment.getReason().getName() + "\n" + ChatColor.GRAY + "Time Remaining: " + ChatColor.WHITE +
                                Time.toString(Time.punishmentTimeRemaining(punishment.getExpiration()), true)).create()));
                        player.spigot().sendMessage(baseComponent);
                        plugin.playSound(player, EventSound.COMMAND_FAIL);
                        return true;
                    }
                }
            }

            plugin.createPunishment(targetData.getUuid(), targetData.getName(), Punishment.Type.MUTE, LocalDateTime.now(), reason.getLength(), player.getUniqueId(), reason);

            final int finalTime = reason.getLength() * 1000;
            final String finalName = reason.getName();
            if (target != null) {
                BaseComponent baseComponent = new TextComponent(ChatColor.RED + player.getName() + " muted " + ChatColor.RED + plugin.getServer().getPlayer(targetData.getUuid()).getName());
                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Reason: "
                        + ChatColor.WHITE + finalName + "\n" + ChatColor.GRAY + "Time: " + ChatColor.WHITE + Time.toString(finalTime, false)).create()));

                plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff -> staff.spigot().sendMessage(baseComponent));

                target.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.RED + " muted you for " + ChatColor.GOLD + Time.toString(finalTime, true)
                        + ChatColor.RED + " for " + ChatColor.GOLD + finalName);
            } else {
                BaseComponent baseComponent = new TextComponent(ChatColor.RED + player.getName() + " muted " + ChatColor.RED + offlinePlayer.getName());
                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Reason: "
                        + ChatColor.WHITE + finalName + "\n" + ChatColor.GRAY + "Time: " + ChatColor.WHITE + Time.toString(finalTime, false)).create()));

                plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff -> staff.spigot().sendMessage(baseComponent));
        }
        }

        return true;
    }
}
