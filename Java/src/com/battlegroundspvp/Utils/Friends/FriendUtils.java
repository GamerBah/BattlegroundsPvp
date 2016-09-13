package com.battlegroundspvp.Utils.Friends;

import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import org.bukkit.entity.Player;

public class FriendUtils {
    private Battlegrounds plugin;

    public FriendUtils(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void createPendingRequest(Player sender, Player target) {
        FriendMessages friendMessages = new FriendMessages(plugin);
        Battlegrounds.pendingFriends.put(target, sender);
        friendMessages.sendRequestMessage(sender, target);
    }

    public void removePendingRequest(Player target, boolean accepted) {
        FriendMessages friendMessages = new FriendMessages(plugin);
        if (accepted) {
            friendMessages.sendAcceptMessage(target, Battlegrounds.pendingFriends.get(target));
        } else {
            friendMessages.sendDeclineMessage(target, Battlegrounds.pendingFriends.get(target));
        }
        Battlegrounds.pendingFriends.remove(target);
    }

    public void addFriend(Player sender, Player target) {
        PlayerData playerData = plugin.getPlayerData(sender.getUniqueId());
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        if (playerData.getFriends() == null) {
            playerData.setFriends(targetData.getId() + ",");
        } else {
            playerData.setFriends(playerData.getFriends() + targetData.getId() + ",");
        }
        if (targetData.getFriends() == null) {
            targetData.setFriends(playerData.getId() + ",");
        } else {
            targetData.setFriends(targetData.getFriends() + playerData.getId() + ",");
        }
        removePendingRequest(target, true);
    }

    public void deleteFriend(Player player, Player target) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        if (playerData.getFriends().contains(targetData.getId() + ",")) {
            playerData.setFriends(playerData.getFriends().replace(targetData.getId() + ",", ""));
            targetData.setFriends(targetData.getFriends().replace(playerData.getId() + ",", ""));
        }
    }

    public boolean hasPendingRequest(Player target, Player sender) {
        if (Battlegrounds.pendingFriends.containsKey(target)) {
            return Battlegrounds.pendingFriends.get(target).equals(sender);
        }
        return false;
    }

    public boolean areFriends(Player player, Player target) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        if (playerData.getFriends() == null) {
            return false;
        }
        return playerData.getFriends().contains(targetData.getId() + ",");
    }

    public Player getRequester(Player target) {
        return Battlegrounds.pendingFriends.get(target);
    }

}
