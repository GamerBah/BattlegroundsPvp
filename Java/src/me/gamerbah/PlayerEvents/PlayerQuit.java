package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Commands.ReportCommand;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.BoldColor;
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
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

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

        if (playerData.isStealthyJoin()) {
            event.setQuitMessage(null);
        } else {
            event.setQuitMessage(BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.RED.getColor() + "-"
                    + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName());
        }
    }

}
