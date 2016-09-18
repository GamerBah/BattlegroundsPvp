package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Kits.Epic.DarkRider;
import com.battlegroundspvp.PlayerEvents.PlayerMove;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.Kits.KitManager;
import com.connorlinfoot.titleapi.TitleAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public SpawnCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (KitManager.isPlayerInKit(player)) {
            KitManager.getPreviousKit().put(player.getUniqueId(), KitManager.getPlayersInKits().get(player.getUniqueId()));
        }
        if (PlayerMove.getLaunched().contains(player)) {
            PlayerMove.getLaunched().remove(player);
        }
        if (Battlegrounds.getAfk().contains(player.getUniqueId())) {
            Battlegrounds.getAfk().remove(player.getUniqueId());
            player.sendMessage(ChatColor.GRAY + "You are no longer AFK");
            Battlegrounds.playSound(player, EventSound.CLICK);
            TitleAPI.clearTitle(player);
        }
        if (DarkRider.getRiding().contains(player)) {
            DarkRider.getRiding().remove(player);
            Horse horse = (Horse) player.getVehicle();
            horse.setOwner(null);
            horse.setPassenger(null);
            horse.setHealth(0);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.respawn(player), 5L);
        }
        plugin.respawn(player);
        Battlegrounds.playSound(player, EventSound.COMMAND_NEEDS_CONFIRMATION);

        return true;
    }
}
