package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/28/2016 */

import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.KSlotsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SlotsCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public SlotsCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        KSlotsMenu kSlotsMenu = new KSlotsMenu(plugin);
        kSlotsMenu.openInventory(player);

        return false;
    }

}