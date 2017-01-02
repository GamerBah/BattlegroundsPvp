package com.battlegroundspvp.Administration.Punishments.Commands;
/* Created by GamerBah on 8/7/2016 */


import com.battlegroundspvp.Administration.Commands.WarnCommand;
import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.Punishment.PunishMenu;
import com.battlegroundspvp.Utils.Enums.EventSound;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class KickCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public KickCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public static void kickPlayer(UUID targetUUID, Player player, HashMap<Punishment.Reason, Integer> map) {
        Battlegrounds plugin = Battlegrounds.getInstance();
        PlayerData targetData = plugin.getPlayerData(targetUUID);

        Punishment.Reason reason = null;
        for (Punishment.Reason r : map.keySet()) {
            reason = r;
        }

        Player target = plugin.getServer().getPlayer(targetUUID);
        if (target == null) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "That player isn't online!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return;
        }

        if (reason != null) {
            plugin.createPunishment(targetData.getUuid(), targetData.getName(), Punishment.Type.KICK, LocalDateTime.now(), -1, player.getUniqueId(), reason);
            if (!WarnCommand.getWarned().containsKey(targetUUID)) {
                WarnCommand.getWarned().remove(targetUUID);
            }

            final String finalName = reason.getName();
            BaseComponent baseComponent = new TextComponent(ChatColor.RED + player.getName() + " kicked " + ChatColor.RED + target.getName());
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Reason: "
                    + ChatColor.GOLD + finalName).create()));
            plugin.getServer().getOnlinePlayers().stream().filter(staff -> plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.HELPER)).forEach(staff -> staff.spigot().sendMessage(baseComponent));

            target.kickPlayer(ChatColor.RED + "You were kicked by " + ChatColor.GOLD + player.getName() + ChatColor.RED + " for " + ChatColor.GOLD + finalName + "\n"
                    + ChatColor.YELLOW + reason.getMessage() + "\n\n" + ChatColor.GRAY + "If you feel that staff abuse was an issue, please email support@battlegroundspvp.com");

            player.closeInventory();
            Battlegrounds.punishmentCreation.remove(player);
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
            plugin.sendIncorrectUsage(player, "/kick <player>");
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = plugin.getPlayerData(plugin.getServer().getOfflinePlayer(args[0]).getUniqueId());
        if (targetData == null) {
            player.sendMessage(ChatColor.RED + "That player hasn't joined before!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }
        Player target = plugin.getServer().getPlayer(targetData.getUuid());
        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player isn't online!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (targetData == playerData) {
            player.sendMessage(ChatColor.RED + "You can't kick yourself!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        PunishMenu punishMenu = new PunishMenu(plugin);
        punishMenu.openPunishMenu(player, target, Punishment.Type.KICK, null, 0);
        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);

        return true;
    }
}
