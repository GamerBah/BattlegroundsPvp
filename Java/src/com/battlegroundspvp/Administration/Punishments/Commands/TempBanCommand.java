package com.battlegroundspvp.Administration.Punishments.Commands;
/* Created by GamerBah on 8/7/2016 */


import com.battlegroundspvp.Administration.Commands.WarnCommand;
import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.Punishment.PunishMenu;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Enums.Time;
import net.gpedro.integrations.slack.SlackMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TempBanCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public TempBanCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public static void tempbanPlayer(UUID targetUUID, Player player, HashMap<Punishment.Reason, Integer> map) {
        Battlegrounds plugin = Battlegrounds.getInstance();
        PlayerData targetData = plugin.getPlayerData(targetUUID);
        if (plugin.getPlayerPunishments().containsKey(targetData.getUuid())) {
            ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(targetData.getUuid());
            if (punishments != null) {
                for (int i = 0; i < punishments.size(); i++) {
                    Punishment punishment = punishments.get(i);
                    if (punishment.getType().equals(Punishment.Type.TEMP_BAN) || punishment.getType().equals(Punishment.Type.BAN)) {
                        if (!punishment.isPardoned()) {
                            player.closeInventory();
                            PlayerData enforcerData = plugin.getPlayerData(punishment.getEnforcer());
                            BaseComponent baseComponent = new TextComponent(ChatColor.RED + "That player is already temp-banned!");
                            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Banned by: "
                                    + enforcerData.getRank().getColor() + "" + ChatColor.BOLD + enforcerData.getRank().getName().toUpperCase()
                                    + ChatColor.WHITE + " " + enforcerData.getName() + "\n" + ChatColor.GRAY + "Reason: "
                                    + ChatColor.GOLD + punishment.getReason().getName() + "\n" + ChatColor.GRAY + "Time Remaining: " + ChatColor.YELLOW +
                                    Time.toString(Time.punishmentTimeRemaining(punishment.getExpiration()), true)).create()));
                            player.spigot().sendMessage(baseComponent);
                            EventSound.playSound(player, EventSound.ACTION_FAIL);
                            return;
                        }
                    }
                }
            }
        }

        Punishment.Reason reason = null;
        for (Punishment.Reason r : map.keySet()) {
            reason = r;
        }

        int time = map.get(reason);

        if (reason != null) {
            plugin.createPunishment(targetData.getUuid(), targetData.getName(), Punishment.Type.TEMP_BAN, LocalDateTime.now(), time, player.getUniqueId(), reason);
            if (!WarnCommand.getWarned().containsKey(targetUUID)) {
                WarnCommand.getWarned().remove(targetUUID);
            }
            plugin.slackPunishments.call(new SlackMessage(">>> _*" + player.getName() + "* temporarily banned *" + targetData.getName() + "*_\n*Reason:* _"
                    + reason.getName() + "_\n*Duration:* _" + Time.toString(time * 1000, true) + "_"));

            final String finalName = reason.getName();
            Player target = plugin.getServer().getPlayer(targetUUID);
            final int finalTime = time * 1000;
            if (target != null) {
                BaseComponent baseComponent = new TextComponent(ChatColor.RED + player.getName() + " temporarily banned " + ChatColor.RED + plugin.getServer().getPlayer(targetData.getUuid()).getName());
                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Reason: "
                        + ChatColor.WHITE + finalName + "\n" + ChatColor.GRAY + "Time: " + ChatColor.WHITE + Time.toString(finalTime, false)).create()));

                plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff -> staff.spigot().sendMessage(baseComponent));

                target.kickPlayer(ChatColor.RED + "You were temporarily banned by " + ChatColor.GOLD + player.getName() + ChatColor.RED + " for " + ChatColor.GOLD + finalName + "\n"
                        + ChatColor.YELLOW + reason.getMessage() + "\n\n" + ChatColor.GRAY + "Appeal your ban on the forums: battlegroundspvp.com/forums");
            } else {
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(targetUUID);
                BaseComponent baseComponent = new TextComponent(ChatColor.RED + player.getName() + " temporarily banned " + ChatColor.RED + offlinePlayer.getName());
                baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Reason: "
                        + ChatColor.WHITE + finalName + "\n" + ChatColor.GRAY + "Time: " + ChatColor.WHITE + Time.toString(finalTime, false)).create()));

                plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff -> staff.spigot().sendMessage(baseComponent));
            }
            player.closeInventory();
            Battlegrounds.punishmentCreation.remove(player);
            plugin.getGlobalStats().addBan();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.MODERATOR)) {
            plugin.sendNoPermission(player);
            return true;
        }

        if (args.length != 1) {
            plugin.sendIncorrectUsage(player, ChatColor.RED + "/tempban <player>");
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = plugin.getPlayerData(plugin.getServer().getOfflinePlayer(args[0]).getUniqueId());
        if (targetData == null) {
            player.sendMessage(ChatColor.RED + "That player hasn't joined before!");
            EventSound.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }
        OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());

        if (targetData == playerData) {
            player.sendMessage(ChatColor.RED + "You can't temp-ban yourself!");
            EventSound.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (plugin.getPlayerPunishments().containsKey(targetData.getUuid())) {
            ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(targetData.getUuid());
            if (punishments != null) {
                for (int i = 0; i < punishments.size(); i++) {
                    Punishment punishment = punishments.get(i);
                    if (punishment.getType().equals(Punishment.Type.TEMP_BAN)) {
                        if (!punishment.isPardoned()) {
                            PlayerData enforcerData = plugin.getPlayerData(punishment.getEnforcer());
                            BaseComponent baseComponent = new TextComponent(ChatColor.RED + "That player is already temp-banned!");
                            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Banned by: "
                                    + enforcerData.getRank().getColor() + "" + ChatColor.BOLD + enforcerData.getRank().getName().toUpperCase()
                                    + ChatColor.WHITE + " " + enforcerData.getName() + "\n" + ChatColor.GRAY + "Reason: "
                                    + ChatColor.GOLD + punishment.getReason().getName() + "\n" + ChatColor.GRAY + "Time Remaining: " + ChatColor.YELLOW +
                                    Time.toString(Time.punishmentTimeRemaining(punishment.getExpiration()), true)).create()));
                            player.spigot().sendMessage(baseComponent);
                            EventSound.playSound(player, EventSound.ACTION_FAIL);
                            return true;
                        }
                    }
                }
            }
        }

        PunishMenu punishMenu = new PunishMenu(plugin);
        punishMenu.openPunishMenu(player, targetData, Punishment.Type.TEMP_BAN, null, 0);
        EventSound.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);

        return true;
    }

}
