package me.gamerbah.Administration.Utils;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Administration.Commands.ChatCommands;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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

        if (StringUtils.startsWithIgnoreCase(event.getMessage(), "/me")
                || StringUtils.startsWithIgnoreCase(event.getMessage(), "/minecraft:")
                || StringUtils.startsWithIgnoreCase(event.getMessage(), "/bukkit:")
                || StringUtils.startsWithIgnoreCase(event.getMessage(), "/spigot:")) {
            event.setCancelled(true);
            return;
        }

        plugin.getServer().getOnlinePlayers().stream().filter(players ->
                plugin.getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER) && ChatCommands.cmdspies.contains(players.getUniqueId()))
                .forEach(players -> players.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "" + ChatColor.DARK_GRAY + player.getName() + ": " + ChatColor.GRAY + command));
    }

}
