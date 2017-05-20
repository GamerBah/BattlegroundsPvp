package com.battlegroundspvp.Administration.Commands;
/* Created by GamerBah on 8/25/2016 */

import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.Punishment.PunishMenu;
import com.battlegroundspvp.Utils.Enums.EventSound;
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
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.HELPER)) {
            plugin.sendNoPermission(player);
            return true;
        }

        if (args.length == 0) {
            PunishMenu punishMenu = new PunishMenu(plugin);
            punishMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_AZ, null, 0);
            return true;
        }

        if (args.length > 1) {
            plugin.sendIncorrectUsage(player, "/punish [player]");
            return true;
        }

        @SuppressWarnings("deprecation")
        PlayerData targetData = Battlegrounds.getSql().getPlayerData(args[0]);

        if (targetData == null) {
            player.sendMessage(ChatColor.RED + "That player has never joined before!");
            EventSound.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player isn't online!");
                EventSound.playSound(player, EventSound.ACTION_FAIL);
                return true;
            }
            PunishMenu punishMenu = new PunishMenu(plugin);
            punishMenu.openInventory(player, target);
        }

        return false;
    }

}