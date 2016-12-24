package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Listeners.CombatListener;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.connorlinfoot.titleapi.TitleAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (Battlegrounds.getAfk().contains(player.getUniqueId())) {
            Battlegrounds.getAfk().remove(player.getUniqueId());
            plugin.respawn(player);
            player.sendMessage(ChatColor.GRAY + "You are no longer AFK");
            Battlegrounds.playSound(player, EventSound.CLICK);
            TitleAPI.clearTitle(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        } else {
            plugin.respawn(player, player.getWorld().getSpawnLocation().add(0.5, 8, 0.5));
            player.getInventory().clear();
            player.sendMessage(ChatColor.GRAY + "You are now AFK");
            Battlegrounds.playSound(player, EventSound.CLICK);
            Battlegrounds.getAfk().add(player.getUniqueId());
            TitleAPI.sendTitle(player, 10, 1728000, 20, BoldColor.AQUA.getColor() + "You are AFK!", ChatColor.YELLOW + "Move to start playing again!");
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false));
        }
        return false;
    }

}
