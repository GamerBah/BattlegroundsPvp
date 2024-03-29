package com.battlegroundspvp.Administration.Donations;
/* Created by GamerBah on 8/18/2016 */


import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.Time;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DonationMessages {
    private Battlegrounds plugin;

    public DonationMessages(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void sendEssensePurchaseMessage(Player player, Essence.Type type) {
        TextComponent one = new TextComponent("    " + type.getDisplayName(true) + " Battle Essence\n");
        one.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(type.getDisplayName(true) + "\n"
                + ChatColor.GRAY + "Grants a server-wide " + type.getChatColor() + type.getPercent() + "% increase" + ChatColor.GRAY + " to\n"
                + ChatColor.GRAY + "the amount of Souls and Battle Coins players\n"
                + ChatColor.GRAY + "receive when they kill another player\n\n"
                + ChatColor.GRAY + "Lasts for " + ChatColor.RED + type.getDuration() + (type.getDuration() == 1 ? " Hour" : " Hours") + ChatColor.GRAY + " upon activation\n\n"
                + ChatColor.YELLOW + "Click to view!").create()));
        one.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/essences"));

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 0.6F, 1.2F), 15L);
        player.sendMessage(" ");
        player.sendMessage(type.getChatColor() + "\u00AB" + ChatColor.WHITE + "========================================" + type.getChatColor() + "\u00BB");
        player.sendMessage(" ");
        player.sendMessage(BoldColor.GREEN.getColor() + "   THANKS FOR YOUR PURCHASE!");
        player.sendMessage(ChatColor.YELLOW + "    We greatly appreciate your support!");
        player.sendMessage(" ");
        BaseComponent component = new TextComponent(ChatColor.GRAY + "    You can now find your\n");
        component.addExtra(one);
        component.addExtra(new TextComponent(ChatColor.GRAY + "    in your Player Profile\n"));
        player.spigot().sendMessage(component);
        player.sendMessage(type.getChatColor() + "\u00AB" + ChatColor.WHITE + "========================================" + type.getChatColor() + "\u00BB");
        player.sendMessage(" ");
    }

    public void sendEssenceActivationMessage(Essence.Type type, Player activator) {
        TextComponent thanks = new TextComponent("    " + ChatColor.DARK_AQUA + "Click here to thank them!");
        thanks.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "/thanks " + activator.getName()).create()));
        thanks.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thanks " + activator.getName()));

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_SMALL_FALL, 0.75F, 0.2F), 10L);
            player.sendMessage(" ");
            player.sendMessage(type.getChatColor() + "\u00AB" + ChatColor.WHITE + "========================================" + type.getChatColor() + "\u00BB");
            player.sendMessage(" ");
            player.sendMessage("    " + BoldColor.GOLD.getColor() + (!player.getName().equals(plugin.getConfig().getString("essenceOwner"))
                    ? activator.getName() : "You") + BoldColor.YELLOW.getColor() + " activated a Battle Essence!");
            player.sendMessage(ChatColor.GRAY + "    All players will receive " + type.getChatColor() + type.getPercent() + "% more " + ChatColor.GRAY + "Souls");
            player.sendMessage(ChatColor.GRAY + "    and Battle Coins for " + ChatColor.RED + type.getDuration() + (type.getDuration() == 1 ? " Hour" : " Hours") + ChatColor.GRAY + "!");
            player.sendMessage(" ");
            if (!player.getName().equals(plugin.getConfig().getString("essenceOwner"))) {
                player.spigot().sendMessage(thanks);
                player.sendMessage(" ");
            }
            player.sendMessage(type.getChatColor() + "\u00AB" + ChatColor.WHITE + "========================================" + type.getChatColor() + "\u00BB");
            player.sendMessage(" ");
        }
    }

    public void sendActiveEssenceMessage(Player player) {
        PlayerData playerData = plugin.getPlayerData(plugin.getConfig().getString("essenceOwner"));
        TextComponent thanks = new TextComponent("    " + ChatColor.DARK_AQUA + "Click here to thank them!");
        thanks.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "/thanks " + playerData.getName()).create()));
        thanks.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/thanks " + playerData.getName()));

        Essence.Type type = Essence.Type.ONE_HOUR_50_PERCENT;
        for (Essence.Type essence : Essence.Type.values()) {
            if (essence.getDuration() == plugin.getConfig().getInt("essenceTime") && essence.getPercent() == plugin.getConfig().getInt("essenceIncrease")) {
                type = essence;
            }
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_SMALL_FALL, 0.75F, 0.2F), 10L);
        player.sendMessage(type.getChatColor() + "\u00AB" + ChatColor.WHITE + "========================================" + type.getChatColor() + "\u00BB");
        player.sendMessage(" ");
        player.sendMessage("    " + BoldColor.GOLD.getColor() + (!player.getName().equals(plugin.getConfig().getString("essenceOwner"))
                ? playerData.getName() + BoldColor.YELLOW.getColor() + " has an active Battle Essence!" : "Your " + plugin.getConfig().getInt("essenceIncrease") + "% Battle Essence is still active!"));
        player.sendMessage(ChatColor.GRAY + "    All players receive " + type.getChatColor() + type.getPercent() + "% more " + ChatColor.GRAY + "Souls");
        player.sendMessage(ChatColor.GRAY + "    and Battle Coins for " + ChatColor.RED + Time.toString(plugin.getConfig().getInt("essenceTimeRemaining") * 1000, true) + ChatColor.GRAY + "!");
        player.sendMessage(" ");
        if (!player.getName().equals(plugin.getConfig().getString("essenceOwner"))) {
            player.spigot().sendMessage(thanks);
            player.sendMessage(" ");
        }
        player.sendMessage(type.getChatColor() + "\u00AB" + ChatColor.WHITE + "========================================" + type.getChatColor() + "\u00BB");
    }

    public void sendRankPurchaseMessage(Player player, Rank rank) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 0.6F, 1.2F), 15L);
        player.sendMessage(" ");
        player.sendMessage(rank.getColor() + "\u00AB" + ChatColor.WHITE + "========================================" + rank.getColor() + "\u00BB");
        player.sendMessage(" ");
        player.sendMessage(BoldColor.GREEN.getColor() + "   THANKS FOR YOUR PURCHASE!");
        player.sendMessage(ChatColor.YELLOW + "    We greatly appreciate your support!");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.GRAY + "   Your rank is now " + rank.getColor() + rank.getName().toUpperCase() + ChatColor.GRAY + "! Enjoy!");
        player.sendMessage(rank.getColor() + "\u00AB" + ChatColor.WHITE + "========================================" + rank.getColor() + "\u00BB");
        player.sendMessage(" ");
    }

}
