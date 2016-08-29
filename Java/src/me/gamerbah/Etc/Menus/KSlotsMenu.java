package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 8/28/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Kits.KitManager;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Rarity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KSlotsMenu {
    private Battlegrounds plugin;

    public KSlotsMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        Inventory inv = plugin.getServer().createInventory(null, 36, "\"K-Slots\" Machine");
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        inv.setItem(9, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inv.setItem(11, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inv.setItem(13, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inv.setItem(15, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inv.setItem(17, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));

        inv.setItem(31, new I(Material.WOOL).name(BoldColor.GREEN.getColor() + "Click to Roll!")
                .lore(ChatColor.GRAY + "Cost: " + ChatColor.AQUA + "400 Souls")
                .durability(5));
        inv.setItem(10, new I(Material.INK_SACK).name(BoldColor.GREEN.getColor() + "ACTIVE")
                .lore(ChatColor.GRAY + "This slot will be rolled")
                .durability(10));
        inv.setItem(12, new I(Material.INK_SACK).name(BoldColor.RED.getColor() + "DISABLED")
                .lore(ChatColor.GRAY + "This slot will not be rolled").lore(" ")
                .lore((playerData.hasRank(Rank.WARRIOR) ? ChatColor.YELLOW + "Click to Activate!"
                        : ChatColor.RED + "Must be " + Rank.WARRIOR.getColor() + "" + ChatColor.BOLD + "WARRIOR" + ChatColor.RED + " or higher to use!"))
                .durability(8));
        inv.setItem(14, new I(Material.INK_SACK).name(BoldColor.RED.getColor() + "DISABLED")
                .lore(ChatColor.GRAY + "This slot will not be rolled").lore(" ")
                .lore((playerData.hasRank(Rank.GLADIATOR) ? ChatColor.YELLOW + "Click to Activate!"
                        : ChatColor.RED + "Must be " + Rank.GLADIATOR.getColor() + "" + ChatColor.BOLD + "GLADIATOR" + ChatColor.RED + " or higher to use!"))
                .durability(8));
        inv.setItem(16, new I(Material.INK_SACK).name(BoldColor.RED.getColor() + "DISABLED")
                .lore(ChatColor.GRAY + "This slot will not be rolled").lore(" ")
                .lore((playerData.hasRank(Rank.CONQUEROR) ? ChatColor.YELLOW + "Click to Activate!"
                        : ChatColor.RED + "Must be " + Rank.CONQUEROR.getColor() + "" + ChatColor.BOLD + "CONQUEROR" + ChatColor.RED + " or higher to use!"))
                .durability(8));

        player.openInventory(inv);
    }

    public void beginSlots(Player player, int slots) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.clear();
        inventory.setItem(9, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inventory.setItem(10, new I(Material.BEDROCK).name(BoldColor.RED.getColor() + "Not Active"));
        inventory.setItem(11, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inventory.setItem(12, new I(Material.BEDROCK).name(BoldColor.RED.getColor() + "Not Active"));
        inventory.setItem(13, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inventory.setItem(14, new I(Material.BEDROCK).name(BoldColor.RED.getColor() + "Not Active"));
        inventory.setItem(15, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inventory.setItem(16, new I(Material.BEDROCK).name(BoldColor.RED.getColor() + "Not Active"));
        inventory.setItem(17, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        List<Kit> kits = new ArrayList<>();
        for (Kit kit : KitManager.getKits()) {
            if (!kit.equals(KitManager.getKits().get(0))) {
                kits.add(kit);
            }
        }
        if (slots > 0) {
            int slot = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (player.getOpenInventory().getTopInventory().getName().equals("\"K-Slots\" Machine")) {
                    Kit kit = kits.get(ThreadLocalRandom.current().nextInt(0, kits.size()));
                    inventory.setItem(10, kit.getItem());
                    player.openInventory(inventory);
                    plugin.playSound(player, EventSound.COMMAND_SUCCESS);
                    plugin.playSound(player, EventSound.COMMAND_CLICK);
                }
            }, 0L, 2L);

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getServer().getScheduler().cancelTask(slot);
                if (!player.getOpenInventory().getTopInventory().equals(inventory)) {
                    player.openInventory(inventory);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1F);
                for (Kit kit : kits) {
                    if (kit.getItem().equals(inventory.getItem(10))) {
                        if (kit.getRarity().equals(Rarity.EPIC)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_EPIC);
                        }
                        if (kit.getRarity().equals(Rarity.LEGENDARY)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_LEGENDARY);
                        }
                    }
                }
                if (slots == 1) {
                    inventory.setItem(31, new I(Material.END_CRYSTAL).name(BoldColor.YELLOW.getColor() + "Reset Slots").lore(ChatColor.GRAY + "Click to use the machine again!"));
                }
            }, 120L);
        }

        if (slots > 1) {
            int slot = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (player.getOpenInventory().getTopInventory().getName().equals("\"K-Slots\" Machine")) {
                    Kit kit = kits.get(ThreadLocalRandom.current().nextInt(0, kits.size()));
                    inventory.setItem(12, kit.getItem());
                    player.openInventory(inventory);
                }
            }, 0L, 2L);

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getServer().getScheduler().cancelTask(slot);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1F);
                for (Kit kit : kits) {
                    if (kit.getItem().equals(inventory.getItem(12))) {
                        if (kit.getRarity().equals(Rarity.EPIC)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_EPIC);
                        }
                        if (kit.getRarity().equals(Rarity.LEGENDARY)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_LEGENDARY);
                        }
                    }
                }
                if (slots == 2) {
                    inventory.setItem(31, new I(Material.END_CRYSTAL).name(BoldColor.YELLOW.getColor() + "Reset Slots").lore(ChatColor.GRAY + "Click to use the machine again!"));
                }
            }, 135L);
        }

        if (slots > 2) {
            int slot = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (player.getOpenInventory().getTopInventory().getName().equals("\"K-Slots\" Machine")) {
                    Kit kit = kits.get(ThreadLocalRandom.current().nextInt(0, kits.size()));
                    inventory.setItem(14, kit.getItem());
                    player.openInventory(inventory);
                }
            }, 0L, 2L);

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getServer().getScheduler().cancelTask(slot);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1F);
                for (Kit kit : kits) {
                    if (kit.getItem().equals(inventory.getItem(14))) {
                        if (kit.getRarity().equals(Rarity.EPIC)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_EPIC);
                        }
                        if (kit.getRarity().equals(Rarity.LEGENDARY)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_LEGENDARY);
                        }
                    }
                }
                if (slots == 3) {
                    inventory.setItem(31, new I(Material.END_CRYSTAL).name(BoldColor.YELLOW.getColor() + "Reset Slots").lore(ChatColor.GRAY + "Click to use the machine again!"));
                }
            }, 150L);
        }

        if (slots > 3) {
            int slot = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (player.getOpenInventory().getTopInventory().getName().equals("\"K-Slots\" Machine")) {
                    Kit kit = kits.get(ThreadLocalRandom.current().nextInt(0, kits.size()));
                    inventory.setItem(16, kit.getItem());
                    player.openInventory(inventory);
                }
            }, 0L, 2L);

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getServer().getScheduler().cancelTask(slot);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1F);
                for (Kit kit : kits) {
                    if (kit.getItem().equals(inventory.getItem(16))) {
                        if (kit.getRarity().equals(Rarity.EPIC)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_EPIC);
                        }
                        if (kit.getRarity().equals(Rarity.LEGENDARY)) {
                            plugin.playSound(player, EventSound.ITEM_RECEIVE_LEGENDARY);
                        }
                    }
                }
                if (slots == 4) {
                    inventory.setItem(31, new I(Material.END_CRYSTAL).name(BoldColor.YELLOW.getColor() + "Reset Slots").lore(ChatColor.GRAY + "Click to use the machine again!"));
                }
            }, 165L);
        }

    }

}
