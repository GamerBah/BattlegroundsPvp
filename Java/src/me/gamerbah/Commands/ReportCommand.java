package me.gamerbah.Commands;

import lombok.Getter;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Menus.ReportGUI;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    private Battlegrounds plugin;
    @Getter
    private static HashMap<UUID, Integer> cooldown = new HashMap<>();
    @Getter
    private static HashMap<UUID, String> reportBuilders = new HashMap<>();
    @Getter
    private static HashMap<UUID, ArrayList<String>> reportArray = new HashMap<>();

    public ReportCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage(plugin.incorrectUsage + "/report <player>");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(plugin.incorrectUsage + "/report <player>");
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
            ReportGUI reportGUI = new ReportGUI(plugin);
            reportBuilders.put(player.getUniqueId(), null);
            reportArray.put(player.getUniqueId(), new ArrayList<>());
            reportGUI.openInventory(player, reported);
        } else {
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            player.sendMessage(ChatColor.RED + "You must wait " + ChatColor.YELLOW
                    + cooldown.get(player.getUniqueId()) + " seconds " + ChatColor.RED + "before you report another player!");
            return false;
        }
        return false;
    }
}
