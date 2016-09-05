package me.gamerbah.Administration.Utils;
/* Created by GamerBah on 8/15/2016 */


import com.connorlinfoot.titleapi.TitleAPI;
import me.gamerbah.Administration.Commands.ChatCommands;
import me.gamerbah.Administration.Commands.FreezeCommand;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Runnables.TrailRunnable;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerCommandPreProccess implements Listener {
    private Battlegrounds plugin;

    public PlayerCommandPreProccess(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        String command = event.getMessage();

        if (FreezeCommand.reloadFreeze) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "No commands are available during an update!");
            Battlegrounds.playSound(player, EventSound.COMMAND_FAIL);
            return;
        }

        if (StringUtils.startsWithIgnoreCase(command, "/me")
                || StringUtils.startsWithIgnoreCase(command, "/minecraft:")
                || StringUtils.startsWithIgnoreCase(command, "/bukkit:")
                || StringUtils.startsWithIgnoreCase(command, "/spigot:")
                || StringUtils.startsWithIgnoreCase(command, "/battlegrounds:")) {
            event.setCancelled(true);
            return;
        }

        if (StringUtils.equalsIgnoreCase(command, "/help")) {
            event.setCancelled(true);
            player.performCommand("battlegrounds:help");
            return;
        }
        if (StringUtils.equalsIgnoreCase(command, "/help staff")) {
            event.setCancelled(true);
            player.performCommand("battlegrounds:help staff");
            return;
        }

        if (Battlegrounds.getAfk().contains(player.getUniqueId()) && !StringUtils.startsWithIgnoreCase(command, "/afk") && !StringUtils.startsWithIgnoreCase(command, "/spawn")) {
            Battlegrounds.getAfk().remove(player.getUniqueId());
            player.sendMessage(ChatColor.GRAY + "You are no longer AFK");
            Battlegrounds.playSound(player, EventSound.COMMAND_CLICK);
            TitleAPI.clearTitle(player);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            plugin.respawn(player);
        }

        if (TrailRunnable.getStill().containsKey(player)) {
            TrailRunnable.getStill().put(player, 0);
        }

        plugin.getServer().getOnlinePlayers().stream().filter(players ->
                plugin.getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER) && ChatCommands.cmdspies.contains(players.getUniqueId()))
                .forEach(players -> players.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "" + ChatColor.DARK_GRAY + player.getName() + ": " + ChatColor.GRAY + command));
    }

}
