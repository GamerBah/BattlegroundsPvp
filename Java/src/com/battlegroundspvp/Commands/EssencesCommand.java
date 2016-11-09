package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/18/2016 */


import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Menus.EssenceMenu;
import com.battlegroundspvp.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EssencesCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public EssencesCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 0) {
            plugin.sendIncorrectUsage(player, "/essences");
            return true;
        }

        int amount = 0;
        for (Essence.Type eType : Essence.Type.values()) {
            if (!plugin.getEssenceData(eType).containsKey(player.getUniqueId())) {
                amount += 0;
            } else {
                amount += plugin.getEssenceData(eType).get(player.getUniqueId());
            }
        }
        if (amount == 0) {
            player.sendMessage(ChatColor.RED + "You don't have any Battle Essences!");
            player.sendMessage(ChatColor.YELLOW + "Buy one from our store! " + ChatColor.GOLD + "battlegroundspvp.com/store");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        EssenceMenu essenceMenu = new EssenceMenu(plugin);
        essenceMenu.openInventory(player);

        return true;
    }


}
