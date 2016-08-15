package me.gamerbah.Events;
/* Created by GamerBah on 8/9/2016 */


import me.gamerbah.Administration.Commands.ChatCommands;
import me.gamerbah.Administration.Commands.StaffChatCommand;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.Time;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    private Battlegrounds plugin;

    public PlayerChat(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (StaffChatCommand.getToggled().contains(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getServer().getOnlinePlayers().stream().filter(players ->
                    plugin.getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.sendMessage(BoldColor.YELLOW.getColor() + "[STAFF] "
                            + ChatColor.RED + player.getName() + ": " + event.getMessage()));
            return;
        }

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        // TODO: Mute Check

        Rank rank = playerData.getRank();

        if (playerData.hasRank(Rank.ELITE)) {
            ChatColor color = rank.getColor();
            event.setFormat((color == null ? " " : color + " ") + ChatColor.BOLD + rank.getName().toUpperCase() + ChatColor.RESET + " %s" + ChatColor.GRAY + " \u00BB " + ChatColor.WHITE + "%s");
        } else {
            event.setFormat(ChatColor.GRAY + " %s" + ChatColor.GRAY + " \u00BB " + ChatColor.GRAY + "%s");
        }

        if (ChatCommands.chatSilenced && !playerData.hasRank(Rank.HELPER)) {
            event.setCancelled(true);
        }
    }

}
