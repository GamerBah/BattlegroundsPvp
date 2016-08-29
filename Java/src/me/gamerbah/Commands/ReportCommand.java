package me.gamerbah.Commands;

import lombok.Getter;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Menus.ReportMenu;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Time;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    @Getter
    private static HashMap<UUID, Integer> cooldown = new HashMap<>();
    @Getter
    private static HashMap<UUID, String> reportBuilders = new HashMap<>();
    @Getter
    private static HashMap<UUID, ArrayList<String>> reportArray = new HashMap<>();
    private Battlegrounds plugin;

    public ReportCommand(Battlegrounds plugin) {
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

        if (args.length != 1) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/report <player>");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        Player reported = plugin.getServer().getPlayerExact(args[0]);

        if (reported == null) {
            player.sendMessage(ChatColor.RED + "That player isn't online!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (reported == player) {
            player.sendMessage(ChatColor.RED + "You can't report yourself! Unless you have something to tell us.... *gives suspicious look*");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (!cooldown.containsKey(player.getUniqueId())) {
            ReportMenu reportMenu = new ReportMenu(plugin);
            reportBuilders.put(player.getUniqueId(), null);
            reportArray.put(player.getUniqueId(), new ArrayList<>());
            reportMenu.openInventory(player, reported);
        } else {
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            player.sendMessage(ChatColor.RED + "You must wait " + ChatColor.YELLOW
                    + cooldown.get(player.getUniqueId()) + " seconds " + ChatColor.RED + "before you report another player!");
            return false;
        }
        return false;
    }
}
