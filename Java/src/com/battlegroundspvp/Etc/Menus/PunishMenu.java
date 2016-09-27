package com.battlegroundspvp.Etc.Menus;
/* Created by GamerBah on 8/25/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.SignGUI;
import com.battlegroundspvp.Utils.Time;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PunishMenu {
    private Battlegrounds plugin;

    public PunishMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openSearch(Player player) {
        SignGUI.open(player);
    }

    public void openPlayersMenu(Player player, SortType sortType, String searchTerm, int page) {
        PlayerData pData = plugin.getPlayerData(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, "Punish Menu");

        int a = 0;
        for (int i = page * 45; i < plugin.getAllPlayerData().size() && i >= page * 45 && i < (page + 1) * 45; i++) {
            Collections.sort(plugin.getAllPlayerData(), new Comparator<PlayerData>() {
                @Override
                public int compare(PlayerData p1, PlayerData p2) {
                    if (sortType.equals(SortType.NAME_ZA)) {
                        return p2.getName().compareTo(p1.getName());
                    } else if (sortType.equals(SortType.RANK_HIGH_LOW)) {
                        return p1.getRank().compareTo(p2.getRank());
                    } else if (sortType.equals(SortType.RANK_LOW_HIGH)) {
                        return p2.getRank().compareTo(p1.getRank());
                    } else {
                        return p1.getName().compareTo(p2.getName());
                    }
                }
            });
            PlayerData playerData = plugin.getAllPlayerData().get(i);
            if (playerData.getId() != pData.getId()) {
                if (i < 45) {
                    if (sortType.equals(SortType.ONLINE_ONLY)) {
                        if (plugin.getServer().getPlayer(playerData.getUuid()) != null) {
                            ItemStack head = new I(Material.SKULL_ITEM).durability(3).name(playerData.getRank().getColor() + playerData.getName())
                                    .lore(ChatColor.GRAY + "Rank: " + playerData.getRank().getColor() + (playerData.getRank().equals(Rank.DEFAULT) ? "" : "" + ChatColor.BOLD) + playerData.getRank().getName());
                            SkullMeta meta = (SkullMeta) head.getItemMeta();
                            meta.setOwner(playerData.getName());
                            head.setItemMeta(meta);

                            inv.setItem(a++, head);
                        }
                    } else if (sortType.equals(SortType.SEARCH) && searchTerm != null) {
                        if (StringUtils.containsIgnoreCase(playerData.getName(), searchTerm)) {
                            ItemStack head = new I(Material.SKULL_ITEM).durability(3).name(playerData.getRank().getColor() + playerData.getName())
                                    .lore(ChatColor.GRAY + "Rank: " + playerData.getRank().getColor() + (playerData.getRank().equals(Rank.DEFAULT) ? "" : "" + ChatColor.BOLD) + playerData.getRank().getName());
                            SkullMeta meta = (SkullMeta) head.getItemMeta();
                            meta.setOwner(playerData.getName());
                            head.setItemMeta(meta);

                            inv.setItem(a++, head);
                        }
                    } else {
                        ItemStack head = new I(Material.SKULL_ITEM).durability(3).name(playerData.getRank().getColor() + playerData.getName())
                                .lore(ChatColor.GRAY + "Rank: " + playerData.getRank().getColor() + (playerData.getRank().equals(Rank.DEFAULT) ? "" : "" + ChatColor.BOLD) + playerData.getRank().getName());
                        SkullMeta meta = (SkullMeta) head.getItemMeta();
                        meta.setOwner(playerData.getName());
                        head.setItemMeta(meta);

                        inv.setItem(a++, head);
                    }
                }
            }
        }
        if (plugin.getAllPlayerData().size() > (page + 1) * 45) {
            inv.setItem(53, new I(Material.ARROW).name(ChatColor.GRAY + "Next Page"));
        }
        if (page > 0) {
            inv.setItem(45, new I(Material.ARROW).name(ChatColor.GRAY + "Previous Page"));
        }
        inv.setItem(47, new I(Material.APPLE).name(ChatColor.AQUA + "Sort by Name: " + ChatColor.GRAY + (sortType.equals(SortType.NAME_AZ) ? "Z-A" : "A-Z")));
        inv.setItem(48, new I(Material.EXP_BOTTLE).name(ChatColor.AQUA + "Sort by Rank: " + ChatColor.GRAY + (sortType.equals(SortType.RANK_HIGH_LOW) ? "Low-High" : "High-Low")));
        inv.setItem(49, new I(Material.GOLDEN_CARROT).name(ChatColor.AQUA + "Sort by Online Players Only"));
        inv.setItem(51, new I(Material.SIGN).name(ChatColor.YELLOW + "Search..."));
        player.openInventory(inv);
    }

    public void openInventory(Player player, OfflinePlayer target) {
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
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
        inv.setItem(10, new I(Material.BOOK).name(BoldColor.YELLOW.getColor() + "MUTES").lore(ChatColor.GRAY + "Past Mutes: " + ChatColor.RED + mutes)
                .lore(" ").lore(ChatColor.RED + "Click to view!"));
        inv.setItem(12, new I(Material.GOLD_BOOTS).name(BoldColor.GOLD.getColor() + "KICKS").lore(ChatColor.GRAY + "Past Kicks: " + ChatColor.RED + kicks)
                .lore(" ").lore(ChatColor.RED + "Click to view!").flag(ItemFlag.HIDE_ATTRIBUTES));
        inv.setItem(14, new I(Material.SULPHUR).name(BoldColor.RED.getColor() + "TEMP-BANS").lore(ChatColor.GRAY + "Past Temp-Bans: " + ChatColor.RED + tempbans)
                .lore(" ").lore(ChatColor.RED + "Click to view!"));
        inv.setItem(16, new I(Material.BARRIER).name(BoldColor.DARK_RED.getColor() + "BANS").lore(ChatColor.GRAY + "Past Bans: " + ChatColor.RED + bans)
                .lore(" ").lore(ChatColor.RED + "Click to view!"));
        inv.setItem(31, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));

        player.openInventory(inv);
    }

    public void openPunishMenu(Player player, OfflinePlayer target, Punishment.Type type, Punishment.Reason reason, int time) {
        PlayerData targetData = plugin.getPlayerData(target.getName());
        Inventory inv = plugin.getServer().createInventory(null, 36, "Punishment Creation: " + type.getName());

        if (reason == null) {
            inv.setItem(10, new I(Material.WATCH).name(ChatColor.YELLOW + "Time: " + (type.equals(Punishment.Type.BAN) ? BoldColor.RED.getColor() + "PERMANENT"
                    : type.equals(Punishment.Type.KICK) ? ChatColor.GRAY + "N/A" : ChatColor.GRAY + "(Select a reason first)")));
        } else {
            if (type != Punishment.Type.BAN && type != Punishment.Type.KICK) {
                inv.setItem(10, new I(Material.WATCH).name(ChatColor.YELLOW + "Time: " + ChatColor.GRAY + Time.toString(time * 1000, true))
                        .lore(ChatColor.GRAY + "Left-Click: " + ChatColor.RED + "-5 min " + ChatColor.DARK_RED + "" + ChatColor.ITALIC + "(Shift: -15 min)")
                        .lore(ChatColor.GRAY + "Right-Click: " + ChatColor.GREEN + "+5 min " + ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "(Shift: +15 min)"));
            } else {
                if (type == Punishment.Type.BAN) {
                    inv.setItem(10, new I(Material.WATCH).name(ChatColor.YELLOW + "Time: " + BoldColor.RED.getColor() + "PERMANENT"));
                } else {
                    inv.setItem(10, new I(Material.WATCH).name(ChatColor.YELLOW + "Time: " + ChatColor.GRAY + "N/A"));
                }
            }
        }
        int i = 11;
        for (Punishment.Reason reasons : Punishment.Reason.values()) {
            if (type.equals(Punishment.Type.MUTE)) {
                if (reasons.getType().equals(Punishment.Type.MUTE) || reasons.getType().equals(Punishment.Type.ALL)) {
                    String[] split = reasons.getDescription().split(",");
                    inv.setItem(i++, new I((reason != null && reason.equals(reasons) ? Material.ENCHANTED_BOOK : Material.BOOK))
                            .name(ChatColor.RED + reasons.getName() + (reason != null && reason.equals(reasons) ? BoldColor.GREEN.getColor() + " SELECTED" : ""))
                            .lore(ChatColor.GRAY + split[0])
                            .lore(ChatColor.GRAY + split[1]));
                }
            }
            if (type.equals(Punishment.Type.BAN) || type.equals(Punishment.Type.TEMP_BAN)) {
                if (reasons.getType().equals(Punishment.Type.BAN) || reasons.getType().equals(Punishment.Type.KICK_BAN) || reasons.getType().equals(Punishment.Type.ALL)) {
                    String[] split = reasons.getDescription().split(",");
                    if (type.equals(Punishment.Type.TEMP_BAN)) {
                        if (!reasons.equals(Punishment.Reason.MODDED_CLIENT)) {
                            inv.setItem(i++, new I((reason != null && reason.equals(reasons) ? Material.ENCHANTED_BOOK : Material.BOOK))
                                    .name(ChatColor.RED + reasons.getName() + (reason != null && reason.equals(reasons) ? BoldColor.GREEN.getColor() + " SELECTED" : ""))
                                    .lore(ChatColor.GRAY + split[0])
                                    .lore(ChatColor.GRAY + split[1]));
                        }
                    } else {
                        if (!reasons.equals(Punishment.Reason.MODDED_CLIENT_SUSPECTED)) {
                            inv.setItem(i++, new I((reason != null && reason.equals(reasons) ? Material.ENCHANTED_BOOK : Material.BOOK))
                                    .name(ChatColor.RED + reasons.getName() + (reason != null && reason.equals(reasons) ? BoldColor.GREEN.getColor() + " SELECTED" : ""))
                                    .lore(ChatColor.GRAY + split[0])
                                    .lore(ChatColor.GRAY + split[1]));
                        }
                    }
                }
            }
            if (type.equals(Punishment.Type.KICK)) {
                if (reasons.getType().equals(Punishment.Type.KICK) || reasons.getType().equals(Punishment.Type.KICK_BAN) || reasons.getType().equals(Punishment.Type.ALL)) {
                    String[] split = reasons.getDescription().split(",");
                    inv.setItem(i++, new I((reason != null && reason.equals(reasons) ? Material.ENCHANTED_BOOK : Material.BOOK))
                            .name(ChatColor.RED + reasons.getName() + (reason != null && reason.equals(reasons) ? BoldColor.GREEN.getColor() + " SELECTED" : ""))
                            .lore(ChatColor.GRAY + split[0])
                            .lore(ChatColor.GRAY + split[1]));
                }
            }
        }
        ItemStack wool = new I(Material.WOOL)
                .name(BoldColor.GREEN.getColor() + "ACCEPT & " + type.getName().toUpperCase()).durability(5);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Punishing: " + ChatColor.YELLOW + target.getName());
        if (reason != null) {
            lore.add(ChatColor.GRAY + "For: " + ChatColor.GOLD + reason.getName());
        } else {
            lore.add(ChatColor.GRAY + "For: " + ChatColor.RED + "Nothing (Select a book)");
        }
        if (type.equals(Punishment.Type.MUTE) || type.equals(Punishment.Type.TEMP_BAN)) {
            lore.add(ChatColor.GRAY + "Duration: " + ChatColor.AQUA + Time.toString(time * 1000, true));
        }
        ItemMeta im = wool.getItemMeta();
        im.setLore(lore);
        wool.setItemMeta(im);

        inv.setItem(30, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));
        inv.setItem(32, wool);

        player.openInventory(inv);
    }

    public void openMuteMenu(Player player, OfflinePlayer target, int page) {
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, targetData.getName() + "'s Mute History" + (page == 0 ? "" : " (" + (page + 1) + ")"));

        ArrayList<Punishment> allPunishments = plugin.getPlayerPunishments().get(targetData.getUuid());
        ArrayList<Punishment> mutes = new ArrayList<>();
        if (allPunishments != null) {
            for (int i = 0; i < allPunishments.size(); i++) {
                if (allPunishments.get(i).getType().equals(Punishment.Type.MUTE)) {
                    mutes.add(allPunishments.get(i));
                }
            }
        }

        if (!mutes.isEmpty()) {
            Collections.sort(mutes, new Comparator<Punishment>() {
                @Override
                public int compare(Punishment p1, Punishment p2) {
                    return p2.getDate().compareTo(p1.getDate());
                }
            });

            int a = 0;
            for (int i = page * 45; i < mutes.size() && i >= page * 45 && i < (page + 1) * 45; i++) {
                Punishment punishment = mutes.get(i);
                if (punishment.getType().equals(Punishment.Type.MUTE)) {
                    if (a < 45) {
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
        }
        if (mutes.size() > (page + 1) * 45) {
            inv.setItem(50, new I(Material.ARROW).name(ChatColor.GRAY + "Next Page"));
        }
        inv.setItem(48, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));
        inv.setItem(49, new I(Material.BOOK_AND_QUILL).name(BoldColor.GREEN.getColor() + "Register New Mute"));
        player.openInventory(inv);
    }

    public void openBanMenu(Player player, OfflinePlayer target, int page) {
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, targetData.getName() + "'s Ban History" + (page == 0 ? "" : " (" + (page + 1) + ")"));

        ArrayList<Punishment> allPunishments = plugin.getPlayerPunishments().get(targetData.getUuid());
        ArrayList<Punishment> bans = new ArrayList<>();
        if (allPunishments != null) {
            for (int i = 0; i < allPunishments.size(); i++) {
                if (allPunishments.get(i).getType().equals(Punishment.Type.BAN)) {
                    bans.add(allPunishments.get(i));
                }
            }
        }

        if (!bans.isEmpty()) {
            Collections.sort(bans, new Comparator<Punishment>() {
                @Override
                public int compare(Punishment p1, Punishment p2) {
                    return p2.getDate().compareTo(p1.getDate());
                }
            });

            int a = 0;
            for (int i = page * 45; i < bans.size() && i >= page * 45 && i < (page + 1) * 45; i++) {
                Punishment punishment = bans.get(i);
                if (a < 45) {
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
        }
        if (bans.size() > (page + 1) * 45) {
            inv.setItem(50, new I(Material.ARROW).name(ChatColor.GRAY + "Next Page"));
        }
        inv.setItem(48, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));
        inv.setItem(49, new I(Material.BOOK_AND_QUILL).name(BoldColor.GREEN.getColor() + "Register New Ban"));
        player.openInventory(inv);
    }

    public void openTempBanMenu(Player player, OfflinePlayer target, int page) {
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, targetData.getName() + "'s Temp-Ban History" + (page == 0 ? "" : " (" + (page + 1) + ")"));

        ArrayList<Punishment> allPunishments = plugin.getPlayerPunishments().get(targetData.getUuid());
        ArrayList<Punishment> temps = new ArrayList<>();
        if (allPunishments != null) {
            for (int i = 0; i < allPunishments.size(); i++) {
                if (allPunishments.get(i).getType().equals(Punishment.Type.TEMP_BAN)) {
                    temps.add(allPunishments.get(i));
                }
            }
        }

        if (!temps.isEmpty()) {
            Collections.sort(temps, new Comparator<Punishment>() {
                @Override
                public int compare(Punishment p1, Punishment p2) {
                    return p2.getDate().compareTo(p1.getDate());
                }
            });

            int a = 0;
            for (int i = page * 45; i < temps.size() && i >= page * 45 && i < (page + 1) * 45; i++) {
                Punishment punishment = temps.get(i);
                if (punishment.getType().equals(Punishment.Type.TEMP_BAN)) {
                    if (a < 45) {
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
        }
        if (temps.size() > (page + 1) * 45) {
            inv.setItem(50, new I(Material.ARROW).name(ChatColor.GRAY + "Next Page"));
        }
        inv.setItem(48, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));
        inv.setItem(49, new I(Material.BOOK_AND_QUILL).name(BoldColor.GREEN.getColor() + "Register New Temp-Ban"));
        player.openInventory(inv);
    }

    public void openKickMenu(Player player, OfflinePlayer target, int page) {
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, targetData.getName() + "'s Kick History" + (page == 0 ? "" : " (" + (page + 1) + ")"));

        ArrayList<Punishment> allPunishments = plugin.getPlayerPunishments().get(targetData.getUuid());
        ArrayList<Punishment> kicks = new ArrayList<>();
        if (allPunishments != null) {
            for (int i = 0; i < allPunishments.size(); i++) {
                if (allPunishments.get(i).getType().equals(Punishment.Type.KICK)) {
                    kicks.add(allPunishments.get(i));
                }
            }
        }

        if (!kicks.isEmpty()) {
            Collections.sort(kicks, new Comparator<Punishment>() {
                @Override
                public int compare(Punishment p1, Punishment p2) {
                    return p2.getDate().compareTo(p1.getDate());
                }
            });

            int a = 0;
            for (int i = page * 45; i < kicks.size() && i >= page * 45 && i < (page + 1) * 45; i++) {
                Punishment punishment = kicks.get(i);
                if (a < 45) {
                    if (punishment.getType().equals(Punishment.Type.KICK)) {
                        PlayerData playerData = plugin.getPlayerData(punishment.getEnforcer());
                        inv.setItem(a++, new I(Material.MAP).name(ChatColor.AQUA + punishment.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a '(CST)'")))
                                .lore(ChatColor.GRAY + "Kicked by: " + playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase()
                                        + ChatColor.WHITE + " " + playerData.getName())
                                .lore(ChatColor.GRAY + "Reason: " + ChatColor.GOLD + punishment.getReason().getName()));
                    }
                }
            }
        }
        if (kicks.size() > (page + 1) * 45) {
            inv.setItem(50, new I(Material.ARROW).name(ChatColor.GRAY + "Next Page"));
        }
        inv.setItem(48, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));
        inv.setItem(49, new I(Material.BOOK_AND_QUILL).name(BoldColor.GREEN.getColor() + "Kick Player"));
        player.openInventory(inv);
    }

    @AllArgsConstructor
    @Getter
    public enum SortType {
        NAME_AZ("A-Z"),
        NAME_ZA("Z-A"),
        RANK_LOW_HIGH("Low-High"),
        RANK_HIGH_LOW("High-Low"),
        ONLINE_ONLY("Online Only"),
        SEARCH("Search");

        private String name;
    }

}
