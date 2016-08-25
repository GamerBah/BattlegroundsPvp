package me.gamerbah.Commands;

import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Time;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MessageCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public MessageCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (plugin.getPlayerPunishments().containsKey(player.getUniqueId())) {
            ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(player.getUniqueId());
            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (!punishment.isPardoned()) {
                    BaseComponent baseComponent = new TextComponent(ChatColor.RED + "You are muted! " + ChatColor.GRAY + "(Hover to view details)");
                    baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Muted by: "
                            + ChatColor.WHITE + plugin.getServer().getPlayer(punishment.getEnforcer()).getName() + "\n" + ChatColor.GRAY + "Reason: "
                            + ChatColor.WHITE + punishment.getReason().getName() + "\n" + ChatColor.GRAY + "Time Remaining: " + ChatColor.WHITE +
                            Time.toString(Time.punishmentTimeRemaining(punishment.getExpiration()), true)).create()));
                    player.spigot().sendMessage(baseComponent);
                    plugin.playSound(player, EventSound.COMMAND_FAIL);
                    return true;
                }
            }
        }

        if (args.length <= 1) {
            player.sendMessage(ChatColor.RED + "/" + label + " <player> <message>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (!plugin.getPlayerData(target.getUniqueId()).isPrivateMessaging()) {
            player.sendMessage(ChatColor.RED + "That player isn't accepting private messages!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        plugin.getMessagers().put(player.getUniqueId(), target.getUniqueId());
        plugin.getMessagers().put(target.getUniqueId(), player.getUniqueId());

        String message = StringUtils.join(args, ' ', 1, args.length);

        if (Battlegrounds.getAfk().contains(target.getUniqueId())) {
            player.sendMessage(ChatColor.AQUA + target.getName() + " is AFK, so they might not see your message");
            plugin.playSound(player, EventSound.COMMAND_NEEDS_CONFIRMATION);
        }

        player.sendMessage(ChatColor.DARK_AQUA + "You" + ChatColor.RED + " \u00BB " + BoldColor.AQUA.getColor() + target.getName() + ChatColor.WHITE + ": " + ChatColor.AQUA + message.trim());
        target.sendMessage(BoldColor.AQUA.getColor() + player.getName()  + ChatColor.RED + " \u00BB " + ChatColor.DARK_AQUA + "You" + ChatColor.WHITE + ": " + ChatColor.AQUA + message);

        if (target.getUniqueId().toString().equals("66ca47bf-14ae-405b-9ff5-ef4bb98035eb")) {
            player.sendMessage(ChatColor.RED + "Gamer is often AFK due to plugin development. If he's AFK, he'll get back to you when he can!");
        }

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 2, 2);
        target.playSound(target.getLocation(), Sound.BLOCK_NOTE_HARP, 2, 2);

        return true;
    }
}
