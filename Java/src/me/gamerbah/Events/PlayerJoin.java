package me.gamerbah.Events;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
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
            event.setJoinMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "New! " + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "+"
                    + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] " + ChatColor.WHITE + event.getPlayer().getName());
        } else {
            event.setJoinMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "+"
                    + ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "] " + ChatColor.WHITE + event.getPlayer().getName());
        }
    }

}
