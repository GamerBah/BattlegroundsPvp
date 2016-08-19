package me.gamerbah.Commands;
/* Created by GamerBah on 8/18/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Menus.EssenceMenu;
import me.gamerbah.Utils.EventSound;
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
            player.sendMessage(plugin.incorrectUsage + "/essences");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }
        EssenceMenu essenceMenu = new EssenceMenu(plugin);
        essenceMenu.openInventory(player);

        return true;
    }


}
