package com.battlegroundspvp.Commands;
/* Created by GamerBah on 9/17/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.CosmeticrateMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public CrateCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        CosmeticrateMenu cosmeticrateMenu = new CosmeticrateMenu(plugin);
        cosmeticrateMenu.openInventory(player);

        return false;
    }
}
