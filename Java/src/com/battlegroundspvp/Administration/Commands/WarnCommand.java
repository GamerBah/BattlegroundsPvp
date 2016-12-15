package com.battlegroundspvp.Administration.Commands;
/* Created by GamerBah on 11/8/2016 */

import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.PunishMenu;
import com.battlegroundspvp.Etc.Menus.WarnMenu;
import com.battlegroundspvp.Utils.EventSound;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class WarnCommand implements CommandExecutor {

    @Getter
    private static HashMap<UUID, Integer> warned = new HashMap<>();
    private Battlegrounds plugin;

    public WarnCommand(Battlegrounds plugin) {
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

        if (args.length > 1) {
            plugin.sendIncorrectUsage(player, "/warn <player>");
            return true;
        }

        if (args.length == 0) {
            WarnMenu warnMenu = new WarnMenu(plugin);
            warnMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_AZ, null, 0);
            return true;
        }

        if (args.length == 1) {
            @SuppressWarnings("deprecation")
            PlayerData targetData = Battlegrounds.getSql().getPlayerData(args[0]);


            if (targetData == null) {
                player.sendMessage(ChatColor.RED + "That player has never joined before!");
                Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                return true;
            }

            if (targetData.getName().equals(playerData.getName())) {
                player.sendMessage(ChatColor.RED + "You aren't able to warn yourself!");
                Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                return true;
            }

            OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player isn't online!");
                Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                return true;
            }
            WarnMenu warnMenu = new WarnMenu(plugin);
            warnMenu.openInventory(player, target, null);
        }
        return false;
    }

}