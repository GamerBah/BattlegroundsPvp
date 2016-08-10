package me.gamerbah.Administration.Commands;

import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class ChatCommands implements CommandExecutor {

    private Battlegrounds plugin;

    public ChatCommands(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public static HashSet<UUID> cmdspies = new HashSet<>();

    public static boolean chatSilenced = false;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (cmd.getName().equalsIgnoreCase("clearchat")) {
            if (!playerData.hasRank(Rank.MODERATOR)) {
                plugin.sendNoPermission(player);
                return true;
            } else {
                for (int i = 0; i <= 100; i++) {
                    for (Player players : plugin.getServer().getOnlinePlayers()) {
                        players.sendMessage(" ");
                    }
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Bukkit.getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "The chat has been cleared by " + player.getName() + "!");
                    plugin.playSound(p, EventSound.COMMAND_SUCCESS);
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("lockchat")) {
            if (!playerData.hasRank(Rank.MODERATOR)) {
                plugin.sendNoPermission(player);
                return true;
            } else {
                if (!chatSilenced) {
                    chatSilenced = true;
                    Bukkit.getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "The chat has been locked by " + player.getName() + "!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        plugin.playSound(p, EventSound.COMMAND_SUCCESS);
                    }
                } else {
                    chatSilenced = false;
                    Bukkit.getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "All chat has been re-enabled!");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        plugin.playSound(p, EventSound.COMMAND_SUCCESS);
                    }
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("cmdspy")) {
            if (!playerData.hasRank(Rank.HELPER)) {
                plugin.sendNoPermission(player);
                return true;
            } else {
                if (!cmdspies.contains(player.getUniqueId())) {
                    cmdspies.add(player.getUniqueId());
                    player.sendMessage(ChatColor.YELLOW + "Command Spy " + ChatColor.GREEN + "" + ChatColor.BOLD + "ENABLED");
                    plugin.playSound(player, EventSound.COMMAND_SUCCESS);
                } else {
                    cmdspies.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.YELLOW + "Command Spy " + ChatColor.RED + "" + ChatColor.BOLD + "DISABLED");
                    plugin.playSound(player, EventSound.COMMAND_SUCCESS);
                }
            }
        }

        return false;
    }

}
