package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 8/25/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Time;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PunishMenu {
    private Battlegrounds plugin;

    public PunishMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openPlayersInventory(Player p) {
        Inventory inv = plugin.getServer().createInventory(null, 54, "Punish Menu");
        for (int i = 0; i < plugin.getServer().getOnlinePlayers().size(); i++) {
            PlayerData playerData;
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (!player.equals(p)) {
                    playerData = plugin.getPlayerData(player.getUniqueId());
                    ItemStack head = new I(Material.SKULL_ITEM).durability(3).name(playerData.getRank().getColor() + player.getName())
                            .lore(ChatColor.GRAY + "Rank: " + playerData.getRank().getColor() + (playerData.getRank().equals(Rank.DEFAULT) ? "" : "" + ChatColor.BOLD) + playerData.getRank().getName());
                    SkullMeta meta = (SkullMeta) head.getItemMeta();
                    meta.setOwner(player.getName());
                    head.setItemMeta(meta);

                    inv.setItem(i++, head);
                }
            }
            p.openInventory(inv);
        }
    }

    public void openInventory(Player player, OfflinePlayer target) {
        PlayerData targetData = plugin.getPlayerData(target.getName());
        Inventory inv = plugin.getServer().createInventory(null, 36, "Punishing: " + targetData.getName());

        int mutes = 0, kicks = 0, tempbans = 0, bans = 0;
        ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(targetData.getUuid());
        if (plugin.getPlayerPunishments().get(targetData.getUuid()) != null) {
            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (punishment.getType().equals(Punishment.Type.MUTE)) {
                    mutes++;
                }
                if (punishment.getType().equals(Punishment.Type.KICK)) {
                    kicks++;
                }
                if (punishment.getType().equals(Punishment.Type.TEMP_BAN)) {
                    tempbans++;
                }
                if (punishment.getType().equals(Punishment.Type.BAN)) {
                    bans++;
                }
            }
        }
        inv.setItem(10, new I(Material.BOOK).name(BoldColor.YELLOW.getColor() + "MUTES").lore(ChatColor.GRAY + "Past Mutes: " + ChatColor.RED + mutes).lore(" ")
                .lore(ChatColor.RED + "Click to add a mute to this player!"));
        inv.setItem(12, new I(Material.GOLD_BOOTS).name(BoldColor.GOLD.getColor() + "KICKS").lore(ChatColor.GRAY + "Past Kicks: " + ChatColor.RED + kicks)
                .lore(" ").lore(ChatColor.RED + "Click to kick this player!").flag(ItemFlag.HIDE_ATTRIBUTES));
        inv.setItem(14, new I(Material.SULPHUR).name(BoldColor.RED.getColor() + "TEMP-BANS").lore(ChatColor.GRAY + "Past Temp-Bans: " + ChatColor.RED + tempbans)
                .lore(" ").lore(ChatColor.RED + "Click to add a temp-ban to this player!"));
        inv.setItem(16, new I(Material.BARRIER).name(BoldColor.DARK_RED.getColor() + "BANS").lore(ChatColor.GRAY + "Past Bans: " + ChatColor.RED + bans)
                .lore(" ").lore(ChatColor.RED + "Click to add a ban to this player!"));

        player.openInventory(inv);
    }

    public void openMuteMenu(Player player, OfflinePlayer target) {
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, targetData.getName() + "'s Mute History");

        ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(targetData.getUuid());

        if (plugin.getPlayerPunishments().get(target.getUniqueId()) != null) {
            Collections.sort(punishments, new Comparator<Punishment>() {
                @Override
                public int compare(Punishment p1, Punishment p2) {
                    return p2.getDate().compareTo(p1.getDate());
                }
            });

            int a = 0;
            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (punishment.getType().equals(Punishment.Type.MUTE)) {
                    PlayerData playerData = plugin.getPlayerData(punishment.getEnforcer());
                    inv.setItem(a++, new I(Material.MAP).name(ChatColor.AQUA + punishment.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a '(CST)'")))
                            .lore(ChatColor.GRAY + "Muted by: " + playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase()
                                    + ChatColor.WHITE + " " + playerData.getName())
                            .lore(ChatColor.GRAY + "Reason: " + ChatColor.GOLD + punishment.getReason().getName())
                            .lore(ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + Time.toString(punishment.getDuration() * 1000, false))
                            .lore(ChatColor.GRAY + "Active: " + (punishment.isPardoned() ? ChatColor.RED + "No" : ChatColor.GREEN + "Yes")));
                }
            }
        }
        player.openInventory(inv);
    }

    public void openBanMenu(Player player, OfflinePlayer target) {
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, targetData.getName() + "'s Ban History");

        ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(targetData.getUuid());

        if (plugin.getPlayerPunishments().get(target.getUniqueId()) != null) {
            Collections.sort(punishments, new Comparator<Punishment>() {
                @Override
                public int compare(Punishment p1, Punishment p2) {
                    return p2.getDate().compareTo(p1.getDate());
                }
            });

            int a = 0;
            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (punishment.getType().equals(Punishment.Type.BAN)) {
                    PlayerData playerData = plugin.getPlayerData(punishment.getEnforcer());
                    inv.setItem(a++, new I(Material.MAP).name(ChatColor.AQUA + punishment.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a '(CST)'")))
                            .lore(ChatColor.GRAY + "Banned by: " + playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase()
                                    + ChatColor.WHITE + " " + playerData.getName())
                            .lore(ChatColor.GRAY + "Reason: " + ChatColor.GOLD + punishment.getReason().getName())
                            .lore(ChatColor.GRAY + "Active: " + (punishment.isPardoned() ? ChatColor.RED + "No" : ChatColor.GREEN + "Yes")));
                }
            }
        }
        player.openInventory(inv);
    }

}
