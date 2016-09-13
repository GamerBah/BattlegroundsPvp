package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.OptionsMenu;
import com.battlegroundspvp.Utils.EventSound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OptionsCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public OptionsCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/options <player>");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        Player target = plugin.getServer().getPlayerExact(args[0]);

        if (target == null) {
            player.sendMessage(org.bukkit.ChatColor.RED + "That player is not online!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        OptionsMenu optionsMenu = new OptionsMenu(plugin);
        optionsMenu.openInventory(player, target);

        return false;
    }
}
