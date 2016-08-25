package me.gamerbah.Commands;
/* Created by GamerBah on 8/25/2016 */

import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
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


        return false;
    }

}