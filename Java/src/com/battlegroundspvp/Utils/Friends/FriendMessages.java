package com.battlegroundspvp.Utils.Friends;

import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.Messages.TextComponentMessages;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class FriendMessages {

    private Battlegrounds plugin;
    private TextComponentMessages tcm = new TextComponentMessages(plugin);

    public FriendMessages(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void sendRequestMessage(Player sender, Player target) {
        Battlegrounds.playSound(sender, EventSound.TEAM_REQUEST);
        sender.sendMessage(WHITE + "   \u00AB " + GREEN + "You sent a friend request to " + YELLOW + target.getName() + WHITE + " \u00BB");

        Battlegrounds.playSound(target, EventSound.TEAM_REQUEST);
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
        Battlegrounds.playSound(sender, EventSound.TEAM_REQUEST_ACCEPT);
        sender.sendMessage(GREEN + "   \u00AB " + YELLOW + "You are now friends with " + AQUA + target.getName() + GREEN + " \u00BB");

        Battlegrounds.playSound(target, EventSound.TEAM_REQUEST_ACCEPT);
        target.sendMessage(GREEN + "   \u00AB " + YELLOW + "You are now friends with " + AQUA + sender.getName() + GREEN + " \u00BB");
    }

    public void sendDeclineMessage(Player target, Player sender) {
        Battlegrounds.playSound(sender, EventSound.TEAM_REQUEST_DENY);
        sender.sendMessage(RED + "   \u00AB " + AQUA + target.getName() + RED + "declined " + YELLOW + "your friend request" + RED + " \u00BB");

        Battlegrounds.playSound(target, EventSound.TEAM_REQUEST_DENY);
        target.sendMessage(RED + "   \u00AB " + YELLOW + "You " + RED + "declined " + AQUA + sender.getName() + YELLOW + "'s friend request" + RED + " \u00BB");
    }
}
