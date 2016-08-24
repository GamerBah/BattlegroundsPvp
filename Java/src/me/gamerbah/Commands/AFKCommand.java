package me.gamerbah.Commands;
/* Created by GamerBah on 8/15/2016 */


import com.connorlinfoot.titleapi.TitleAPI;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Listeners.CombatListener;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public AFKCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (CombatListener.getTagged().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You cannot change your AFK status while in combat!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (Battlegrounds.getAfk().contains(player.getUniqueId())) {
            Battlegrounds.getAfk().remove(player.getUniqueId());
            plugin.respawn(player);
            player.sendMessage(ChatColor.GRAY + "You are no longer AFK");
            plugin.playSound(player, EventSound.COMMAND_CLICK);
            TitleAPI.clearTitle(player);
        } else {
            Battlegrounds.getAfk().add(player.getUniqueId());
            plugin.respawn(player, player.getWorld().getSpawnLocation().add(0.5, 8, 0.5));
            player.sendMessage(ChatColor.GRAY + "You are now AFK");
            plugin.playSound(player, EventSound.COMMAND_CLICK);
            TitleAPI.sendTitle(player, 10, 1728000, 20, BoldColor.AQUA.getColor() + "You are AFK!", ChatColor.YELLOW + "Move to start playing again!");
        }
        return false;
    }

}
