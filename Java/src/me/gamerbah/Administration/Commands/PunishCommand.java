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
            PunishMenu punishMenu = new PunishMenu(plugin);
            punishMenu.openPlayersInventory(player);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/punish [player]");
            Battlegrounds.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = Battlegrounds.getSql().getPlayerData(args[0]);

        if (targetData == null) {
            player.sendMessage(ChatColor.RED + "That player has never joined before!");
            Battlegrounds.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player isn't online!");
                Battlegrounds.playSound(player, EventSound.COMMAND_FAIL);
                return true;
            }
            PunishMenu punishMenu = new PunishMenu(plugin);
            punishMenu.openInventory(player, target);
        }

        return false;
    }

}