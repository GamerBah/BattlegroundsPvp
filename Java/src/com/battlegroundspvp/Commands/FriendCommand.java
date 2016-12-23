package com.battlegroundspvp.Commands;
/* Created by GamerBah on 9/7/2016 */

import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Friends.FriendUtils;
import com.battlegroundspvp.Utils.Messages.TextComponentMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;

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
            plugin.sendIncorrectUsage(player, "/friend <add/accept/remove/decline/list/seen> <username>");
            return true;
        }

        if (args.length == 1) {
            if (!args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("accept")
                    && !args[0].equalsIgnoreCase("decline")) {
                plugin.sendIncorrectUsage(player, "/friend <add/accept/remove/decline/list> <username>");
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
                String[] stringList = playerData.getFriends().split(",");
                ArrayList<Integer> friendIds = new ArrayList<>();
                for (int i = 0; i < stringList.length; i++) {
                    friendIds.add(Integer.parseInt(stringList[i]));
                }
                ArrayList<PlayerData> onlineFriends = new ArrayList<>();
                ArrayList<PlayerData> offlineFriends = new ArrayList<>();
                for (PlayerData pData : Battlegrounds.getSql().getAllPlayerData()) {
                    if (friendIds.contains(pData.getId())) {
                        Player friend = plugin.getServer().getPlayerExact(pData.getName());
                        if (friend == null) {
                            offlineFriends.add(pData);
                        } else {
                            onlineFriends.add(pData);
                        }
                    }
                }
                onlineFriends.sort(new Comparator<PlayerData>() {
                    @Override
                    public int compare(PlayerData pd1, PlayerData pd2) {
                        return pd1.getName().compareTo(pd2.getName());
                    }
                });
                offlineFriends.sort(new Comparator<PlayerData>() {
                    @Override
                    public int compare(PlayerData pd1, PlayerData pd2) {
                        return pd1.getName().compareTo(pd2.getName());
                    }
                });
                player.sendMessage("§m----------§f[ " + ChatColor.AQUA + "Friends " + ChatColor.WHITE + "]§m----------");
                player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(Hover over a name to view details)\n\n");
                TextComponentMessages tcm = new TextComponentMessages(plugin);
                for (PlayerData friendData : onlineFriends) {
                    Player friend = plugin.getServer().getPlayer(friendData.getUuid());
                    TextComponent friendTCM = new TextComponent(friendData.getName());
                    friendTCM.setColor(friendData.getRank().getColor());
                    friendTCM.setBold(false);
                    friendTCM.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tcm.playerStats(friend)));
                    friendTCM.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/options " + friend.getName()));

                    TextComponent statusDot = new TextComponent(" \u2022 ");
                    statusDot.setColor(ChatColor.GREEN);
                    statusDot.setBold(true);

                    BaseComponent baseComponent = new TextComponent("");
                    baseComponent.addExtra(statusDot);
                    baseComponent.addExtra(friendTCM);

                    player.spigot().sendMessage(baseComponent);
                }
                for (PlayerData friendData : offlineFriends) {
                    OfflinePlayer friend = plugin.getServer().getOfflinePlayer(friendData.getUuid());
                    TextComponent friendTCM = new TextComponent(friendData.getName());
                    friendTCM.setBold(false);
                    friendTCM.setColor(friendData.getRank().getColor());
                    friendTCM.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tcm.playerStats(friend)));

                    TextComponent statusDot = new TextComponent(" \u2022 ");
                    statusDot.setColor(ChatColor.RED);
                    statusDot.setBold(true);

                    BaseComponent baseComponent = new TextComponent("");
                    baseComponent.addExtra(statusDot);
                    baseComponent.addExtra(friendTCM);

                    player.spigot().sendMessage(baseComponent);
                }
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
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    if (Battlegrounds.pendingFriends.containsKey(target)) {
                        Battlegrounds.pendingFriends.remove(target);
                        player.sendMessage(org.bukkit.ChatColor.RED + "You friend request to "
                                + ChatColor.GOLD + target.getName() + ChatColor.RED + " has expired!");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 2, 0.5F);

                        target.sendMessage(ChatColor.GOLD + player.getName() + "'s" + ChatColor.RED + " friend request has expired!");
                        target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BASS, 2, 0.5F);
                    }
                }, 1200L);
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
        }

        return false;
    }

}