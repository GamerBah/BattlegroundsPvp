package com.battlegroundspvp.Utils.Friends;

import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Messages.TextComponentMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;

import static org.bukkit.ChatColor.*;

public class FriendMessages {

    private Battlegrounds plugin;
    private TextComponentMessages tcm = new TextComponentMessages(plugin);

    public FriendMessages(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void sendRequestMessage(Player sender, Player target) {
        EventSound.playSound(sender, EventSound.TEAM_REQUEST);
        sender.sendMessage(WHITE + "   \u00AB " + GREEN + "You sent a friend request to " + YELLOW + target.getName() + WHITE + " \u00BB");

        EventSound.playSound(target, EventSound.TEAM_REQUEST);
        target.sendMessage(" ");
        target.sendMessage(GOLD + "   \u00AB " + WHITE + "========================================" + GOLD + " \u00BB");
        target.sendMessage(" ");
        target.sendMessage(YELLOW + "           " + sender.getName() + AQUA + " has sent you a friend request!");
        target.sendMessage(" ");
        BaseComponent component = tcm.centerTextSpacesLeft();
        component.addExtra(tcm.friendAcceptButton());
        component.addExtra(tcm.centerTextSpacesMiddle());
        component.addExtra(tcm.friendDeclineButton());
        target.spigot().sendMessage(component);
        target.sendMessage(GOLD + "   \u00AB " + WHITE + "========================================" + GOLD + " \u00BB");
        target.sendMessage(" ");
    }

    public void sendAcceptMessage(Player target, Player sender) {
        EventSound.playSound(sender, EventSound.TEAM_REQUEST_ACCEPT);
        sender.sendMessage(GREEN + "   \u00AB " + YELLOW + "You are now friends with " + AQUA + target.getName() + GREEN + " \u00BB");

        EventSound.playSound(target, EventSound.TEAM_REQUEST_ACCEPT);
        target.sendMessage(GREEN + "   \u00AB " + YELLOW + "You are now friends with " + AQUA + sender.getName() + GREEN + " \u00BB");
    }

    public void sendDeclineMessage(Player target, Player sender) {
        EventSound.playSound(sender, EventSound.TEAM_REQUEST_DENY);
        sender.sendMessage(RED + "   \u00AB " + AQUA + target.getName() + RED + "declined " + YELLOW + "your friend request" + RED + " \u00BB");

        EventSound.playSound(target, EventSound.TEAM_REQUEST_DENY);
        target.sendMessage(RED + "   \u00AB " + YELLOW + "You " + RED + "declined " + AQUA + sender.getName() + YELLOW + "'s friend request" + RED + " \u00BB");
    }

    public void sendFriendList(Player player, int page) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        if (playerData.getFriends() == null) {
            player.sendMessage(ChatColor.RED + "You haven't added any friends, yet!");
            EventSound.playSound(player, EventSound.ACTION_FAIL);
            return;
        }
        String[] stringList = playerData.getFriends().split(",");
        ArrayList<Integer> friendIds = new ArrayList<>();
        int maxPages = 0;
        if (friendIds.size() % 10 == 2) {
            maxPages = friendIds.size() / 10;
        } else {
            maxPages = (int) Math.ceil(friendIds.size() / 10);
        }
        if (page > maxPages) {
            page -= 1;
        }
        for (int i = page * 10; i < stringList.length && i >= page * 10 && i < (page + 1) * 10; i++) {
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

        if (maxPages > 1) {
            player.sendMessage("§m----------§f[ " + ChatColor.AQUA + "Friends (Page " + page + "/" + maxPages + ") " + ChatColor.WHITE + "]§m----------");
        } else {
            player.sendMessage("§m----------§f[ " + ChatColor.AQUA + "Friends " + ChatColor.WHITE + "]§m----------");
        }
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
        player.sendMessage("§m----------------------------------------");
    }
}

