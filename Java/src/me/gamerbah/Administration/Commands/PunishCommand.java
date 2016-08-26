package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/25/2016 */

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Menus.PunishMenu;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PunishCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public PunishCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Still working on this!\n" + ChatColor.RED + "For now use /punish <player>");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(plugin.incorrectUsage + "/punish [player]");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = plugin.getPlayerData(plugin.getServer().getOfflinePlayer(args[0]).getUniqueId());

        if (targetData == null) {
            player.sendMessage(ChatColor.RED + "That player has never joined before!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player isn't online!");
                plugin.playSound(player, EventSound.COMMAND_FAIL);
                return true;
            }
            PunishMenu punishMenu = new PunishMenu(plugin);
            punishMenu.openInventory(player, target);
        }

        return false;
    }

}