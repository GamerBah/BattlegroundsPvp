package com.battlegroundspvp.PlayerEvents;
/* Created by GamerBah on 8/9/2016 */


import com.battlegroundspvp.Administration.Commands.ChatCommands;
import com.battlegroundspvp.Administration.Commands.StaffChatCommand;
import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Time;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

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

        if (plugin.getPlayerPunishments().containsKey(player.getUniqueId())) {
            ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(player.getUniqueId());
            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (!punishment.isPardoned()) {
                    event.setCancelled(true);
                    BaseComponent baseComponent = new TextComponent(ChatColor.RED + "You are muted! " + ChatColor.GRAY + "(Hover to view details)");
                    baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Muted by: "
                            + ChatColor.WHITE + plugin.getServer().getPlayer(punishment.getEnforcer()).getName() + "\n" + ChatColor.GRAY + "Reason: "
                            + ChatColor.WHITE + punishment.getReason().getName() + "\n" + ChatColor.GRAY + "Time Remaining: " + ChatColor.WHITE +
                            Time.toString(Time.punishmentTimeRemaining(punishment.getExpiration()), true)).create()));
                    player.spigot().sendMessage(baseComponent);
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return;
                }
            }
        }

        Rank rank = playerData.getRank();

        if (playerData.hasRank(Rank.WARRIOR)) {
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
