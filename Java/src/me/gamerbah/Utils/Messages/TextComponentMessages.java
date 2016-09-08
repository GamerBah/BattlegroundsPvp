package me.gamerbah.Utils.Messages;// AUTHOR: gamer_000 (12/28/2015)

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.KDRatio;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

public class TextComponentMessages {

    private Battlegrounds plugin;

    public TextComponentMessages(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public TextComponent centerTextSpacesLeft() {
        return new TextComponent("                           ");
    }

    public TextComponent centerTextSpacesMiddle() {
        return new TextComponent("         ");
    }

    public TextComponent teamAcceptButton() {
        TextComponent message = new TextComponent("[ACCEPT]");
        message.setBold(true);
        message.setColor(ChatColor.GREEN);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team accept"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY
                + "Click to" + ChatColor.GREEN + " accept " + ChatColor.GRAY + "the request!").create()));
        return message;
    }

    public TextComponent teamDenyButton() {
        TextComponent message = new TextComponent("[DENY]");
        message.setBold(true);
        message.setColor(ChatColor.RED);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team deny"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY
                + "Click to" + ChatColor.RED + " deny " + ChatColor.GRAY + "the request!").create()));
        return message;
    }

    public TextComponent friendAcceptButton() {
        TextComponent message = new TextComponent("[ACCEPT]");
        message.setBold(true);
        message.setColor(ChatColor.GREEN);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY
                + "Click to" + ChatColor.GREEN + " accept " + ChatColor.GRAY + "the request!").create()));
        return message;
    }

    public TextComponent friendDeclineButton() {
        TextComponent message = new TextComponent("[DECLINE]");
        message.setBold(true);
        message.setColor(ChatColor.RED);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend decline"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY
                + "Click to" + ChatColor.RED + " decline " + ChatColor.GRAY + "the request!").create()));
        return message;
    }

    public BaseComponent[] playerStats(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        KDRatio kdRatio = new KDRatio(plugin);
        ChatColor ratioColor = kdRatio.getRatioColor(player);
        double ratio = ((double) playerData.getKills() / (double) playerData.getDeaths());
        ratio = Math.round(ratio * 100.00D) / 100.00D;
        if (playerData.getDeaths() == 0) {
            ratio = playerData.getKills();
        }

        return new ComponentBuilder(
                playerData.getRank().getColor() + "" + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                        + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : ChatColor.GRAY) + player.getName() + "\n\n"
                        + ChatColor.GRAY + "Kills: " + ChatColor.GREEN + playerData.getKills() + "\n"
                        + ChatColor.GRAY + "Deaths: " + ChatColor.RED + playerData.getDeaths() + "\n"
                        + ChatColor.GRAY + "K/D Ratio: " + ratioColor + ratio
                        + "\n\n" + ChatColor.YELLOW + "Click to open player options....").create();
    }

}
