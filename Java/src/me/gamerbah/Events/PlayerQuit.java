package me.gamerbah.Events;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Commands.ReportCommand;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private Battlegrounds plugin;

    public PlayerQuit(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (Battlegrounds.killStreak.containsKey(player.getUniqueId())) {
            Battlegrounds.killStreak.remove(player.getUniqueId());
        }

        if (ReportCommand.getReportBuilders().containsKey(player.getUniqueId())) {
            ReportCommand.getReportBuilders().remove(player.getUniqueId());
        }

        if (ReportCommand.getReportArray().containsKey(player.getUniqueId())) {
            ReportCommand.getReportArray().remove(player.getUniqueId());
        }

        if (plugin.getServer().getOnlinePlayers().size() < 10) {
            plugin.getServer().getOnlinePlayers().stream().filter(players -> Battlegrounds.currentTeams.containsKey(players.getName())
                    || Battlegrounds.currentTeams.containsValue(players.getName())).forEach(players -> {
                plugin.playSound(player, EventSound.COMMAND_NEEDS_CONFIRMATION);
                players.sendMessage(ChatColor.RED + "Since there are less than 10 players online, your team has been suspended!");
                players.sendMessage(ChatColor.GRAY + "It will resume once there are 10 players online again");
            });
        }

        event.setQuitMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.RED + "" + ChatColor.BOLD + "+"
                + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] " + ChatColor.WHITE + event.getPlayer().getName());
    }

}
