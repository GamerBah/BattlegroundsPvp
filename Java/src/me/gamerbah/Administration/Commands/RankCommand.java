package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.WordUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankCommand implements CommandExecutor, TabCompleter {

    private Battlegrounds plugin;

    public RankCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
            if (!playerData.hasRank(Rank.OWNER) && !player.isOp()) {
                if (player.getUniqueId().equals("dc815235-c651-4f55-b6a5-81ea33d16397")) {
                    return false;
                } else {
                    plugin.sendNoPermission(player);
                    return true;
                }
            }
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "/rank <player> <rank>");
            if (sender instanceof Player) plugin.playSound((Player) sender, EventSound.COMMAND_FAIL);
            return true;
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "That player doesn't exist!");
                if (sender instanceof Player) plugin.playSound((Player) sender, EventSound.COMMAND_FAIL);
                return;
            }

            for (Rank rank : Rank.values()) {
                if (rank.getName().equalsIgnoreCase(args[1])) {
                    PlayerData playerData = plugin.getPlayerData(target.getUniqueId());
                    ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
                    if (playerData != null) {
                        if (target.isOnline()) {
                            scoreboardListener.getRanks().put(target.getUniqueId(), playerData.getRank().getColor() + (playerData.hasRank(Rank.WARRIOR)
                                    ? "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase() : playerData.getRank().getName()));
                            playerData.setRank(rank);
                            scoreboardListener.updateScoreboardRank((Player) target);
                            ((Player) target).setPlayerListName((playerData.hasRank(Rank.WARRIOR) ? playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                                    + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : ChatColor.GRAY) + target.getName());
                        } else {
                            playerData.setRank(rank);
                        }
                        if (!playerData.hasRank(Rank.MODERATOR)) {
                            playerData.setStealthyJoin(false);
                        }
                        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Success! " + ChatColor.GRAY + target.getName() + "'s rank was changed to " + WordUtils.capitalizeFully(rank.toString()));
                        return;
                    } else {
                        sender.sendMessage(ChatColor.RED + "That player doesn't exist!");
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            plugin.playSound(player, EventSound.COMMAND_FAIL);
                        }
                        return;
                    }
                }
            }

            List<String> ranks = new ArrayList<>();
            for (Rank rank : Rank.values()) {
                ranks.add(rank.getName());
            }
            sender.sendMessage(ChatColor.RED + "That rank doesn't exist! Try one of these: " + WordUtils.capitalizeFully(ranks.toString().replace("[", "").replace("]", "")));
            if (sender instanceof Player) plugin.playSound((Player) sender, EventSound.COMMAND_FAIL);
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rank")) {
            if (args.length == 2) {
                ArrayList<String> ranks = new ArrayList<>();
                if (!args[1].equals("")) {
                    for (Rank rank : Rank.values()) {
                        if (rank.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            ranks.add(rank.getName());
                        }
                    }
                } else {
                    for (Rank rank : Rank.values()) {
                        ranks.add(rank.getName());
                    }
                }
                Collections.sort(ranks);
                return ranks;
            }
        }
        return null;
    }

}
