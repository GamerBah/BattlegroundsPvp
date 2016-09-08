package me.gamerbah.Commands;
/* Created by GamerBah on 9/7/2016 */

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Friends.FriendUtils;
import me.gamerbah.Utils.Time;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public FriendCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/friend <add/accept/remove/decline/list/seen> <username>");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("accept") && !args[0].equalsIgnoreCase("decline")) {
                player.sendMessage(Battlegrounds.incorrectUsage + "/friend <add/accept/remove/decline/list/seen> <username>");
                Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                return true;
            }
            if (args[0].equalsIgnoreCase("accept")) {
                FriendUtils friendUtils = new FriendUtils(plugin);
                if (friendUtils.hasPendingRequest(player, friendUtils.getRequester(player))) {
                    friendUtils.addFriend(friendUtils.getRequester(player), player);
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("decline")) {
                FriendUtils friendUtils = new FriendUtils(plugin);
                if (friendUtils.hasPendingRequest(player, friendUtils.getRequester(player))) {
                    friendUtils.removePendingRequest(friendUtils.getRequester(player), false);
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("list")) {
                // TODO: Show Friend List
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                if (plugin.getPlayerData(args[1]) == null) {
                    player.sendMessage(ChatColor.RED + "That player hasn't joined before!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return true;
                }
                PlayerData targetData = plugin.getPlayerData(args[1]);
                if (!plugin.getServer().getPlayer(targetData.getUuid()).isOnline()) {
                    player.sendMessage(ChatColor.RED + "That player isn't online!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[1]);
                FriendUtils friendUtils = new FriendUtils(plugin);
                if (friendUtils.areFriends(player, target)) {
                    player.sendMessage(ChatColor.RED + "You are already friends with " + target.getName() + "!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return true;
                }
                friendUtils.createPendingRequest(player, target);
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (plugin.getPlayerData(args[1]) == null) {
                    player.sendMessage(ChatColor.RED + "That player hasn't joined before!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return true;
                }
                PlayerData friendData = plugin.getPlayerData(args[1]);
                Player target = plugin.getServer().getPlayer(args[1]);
                if (!playerData.getFriends().contains(friendData.getId() + ",")) {
                    player.sendMessage(ChatColor.RED + "That player isn't your friend!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return true;
                }
                FriendUtils friendUtils = new FriendUtils(plugin);
                friendUtils.deleteFriend(player, target);
                player.sendMessage(ChatColor.RED + "You are no longer friends with " + target.getName());
            }
            if (args[0].equalsIgnoreCase("seen")) {
                if (plugin.getPlayerData(args[1]) == null) {
                    player.sendMessage(ChatColor.RED + "That player hasn't joined before!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return true;
                }
                PlayerData friendData = plugin.getPlayerData(args[1]);
                Player target = plugin.getServer().getPlayer(args[1]);
                if (!playerData.getFriends().contains(friendData.getId() + ",")) {
                    player.sendMessage(ChatColor.RED + "You can only see when your friends were last online!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return true;
                }
                if (target != null) {
                    player.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + " is currently online");
                    return true;
                } else {
                    player.sendMessage(ChatColor.AQUA + friendData.getName() + ChatColor.GRAY + " was last online " + ChatColor.RED
                            + Time.toString(Time.timeDifference(friendData.getLastOnline()), true) + " ago");
                }
                return true;
            }
        }

        return false;
    }

}