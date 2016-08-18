package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Battlegrounds plugin;

    public PlayerJoin(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        PlayerData playerData = plugin.getPlayerData(event.getUniqueId());

        if (playerData == null) {
            plugin.createPlayerData(event.getUniqueId(), event.getName());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!player.hasPlayedBefore()) {
            event.setJoinMessage(BoldColor.GOLD.getColor() + "New! " + BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                    + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName());
        } else {
            if (playerData.isStealthyJoin()) {
                event.setJoinMessage(null);
            } else {
                event.setJoinMessage(BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                        + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName());
            }
        }

        player.setPlayerListName((playerData.hasRank(Rank.ELITE) ? playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                + (playerData.hasRank(Rank.ELITE) ? ChatColor.WHITE : ChatColor.GRAY) + player.getName());

        plugin.respawn(player);
    }

}
