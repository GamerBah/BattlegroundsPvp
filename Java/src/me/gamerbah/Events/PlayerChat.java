package me.gamerbah.Events;
/* Created by GamerBah on 8/9/2016 */


import me.gamerbah.Administration.Commands.ChatCommands;
import me.gamerbah.Administration.Commands.StaffChatCommand;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
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
                    .forEach(players -> players.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[STAFF] "
                            + ChatColor.RED + player.getName() + ": " + event.getMessage()));
            return;
        }

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        playerData.getPunishments().stream().filter(punishment -> punishment.getType().equals(Punishment.PunishType.MUTE)).forEach(punishment -> {
            PlayerData punisherData = plugin.getPlayerData(punishment.getEnforcerUUID());
            if (System.currentTimeMillis() <= (punishment.getTime() + punishment.getExpiration())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You were temporarily muted by " + plugin.getServer().getOfflinePlayer(punisherData.getUuid()).getName()
                        + " for " + punishment.getReason() + ".\n"
                        + "You have " + Time.toString(punishment.getTime() + punishment.getExpiration() - System.currentTimeMillis()) + " left in your mute.\n"
                        + "Appeal on climaxmc.net/forum if you believe that this is an error!");
            } else if (punishment.getExpiration() == -1) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You were permanently muted by " + plugin.getServer().getOfflinePlayer(punisherData.getUuid()).getName()
                        + " for " + punishment.getReason() + ".\n"
                        + "Appeal on climaxmc.net/forum if you believe that this is an error!");
            }
        });

        Rank rank = playerData.getRank();

        if (playerData.hasRank(Rank.MASTER)) {
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
