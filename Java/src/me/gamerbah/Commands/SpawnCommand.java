package me.gamerbah.Commands;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.PlayerEvents.PlayerMove;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Kits.KitManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        plugin.respawn(player);
        plugin.playSound(player, EventSound.COMMAND_NEEDS_CONFIRMATION);

        return true;
    }
}
