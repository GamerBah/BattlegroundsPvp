package com.battlegroundspvp.Administration.Commands;

import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommands implements CommandExecutor {

    public static boolean chatSilenced = false;
    private Battlegrounds plugin;

    public ChatCommands(Battlegrounds plugin) {
        this.plugin = plugin;
    }

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
                    p.sendMessage(BoldColor.RED.getColor() + "The chat has been cleared by " + player.getName() + "!");
                    p.sendMessage(" ");
                    Battlegrounds.playSound(p, EventSound.ACTION_SUCCESS);
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
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(" ");
                        p.sendMessage(BoldColor.RED.getColor() + "The chat has been locked by " + player.getName() + "!");
                        p.sendMessage(" ");
                        Battlegrounds.playSound(p, EventSound.ACTION_SUCCESS);
                    }
                } else {
                    chatSilenced = false;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(" ");
                        p.sendMessage(BoldColor.GREEN.getColor() + "All chat has been re-enabled!");
                        p.sendMessage(" ");
                        Battlegrounds.playSound(p, EventSound.ACTION_SUCCESS);
                    }
                }
            }
        }

        return false;
    }

}
