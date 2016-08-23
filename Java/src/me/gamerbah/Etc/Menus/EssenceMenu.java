package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 8/17/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Donations.Essence;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.I;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EssenceMenu {
    private Battlegrounds plugin;

    public EssenceMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        Inventory inv = plugin.getServer().createInventory(null, 54, "Battle Essences");

        int oneSlot = 0, threeSlot = 18, sixSlot = 36;

        Essence.Type e;
        for (int f = 0; f < plugin.getEssenceData(Essence.Type.ONE_HOUR_50_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.ONE_HOUR_50_PERCENT;
                if (oneSlot < 18) {
                    inv.setItem(oneSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(1)
                            .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                            .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                            .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                            .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "1 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                            .lore(ChatColor.YELLOW + "Click to activate!"));
                }
            }
        for (int f = 0; f < plugin.getEssenceData(Essence.Type.ONE_HOUR_100_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.ONE_HOUR_100_PERCENT;
            if (oneSlot < 18) {
                inv.setItem(oneSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(1)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "1 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getEssenceData(Essence.Type.ONE_HOUR_150_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.ONE_HOUR_150_PERCENT;
            if (oneSlot < 18) {
                inv.setItem(oneSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(1)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "1 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }

        for (int f = 0; f < plugin.getEssenceData(Essence.Type.THREE_HOUR_50_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.THREE_HOUR_50_PERCENT;
                if (threeSlot < 36) {
                    inv.setItem(threeSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(3)
                            .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                            .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                            .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                            .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "3 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                            .lore(ChatColor.YELLOW + "Click to activate!"));
                }
            }
        for (int f = 0; f < plugin.getEssenceData(Essence.Type.THREE_HOUR_100_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.THREE_HOUR_100_PERCENT;
            if (threeSlot < 36) {
                inv.setItem(threeSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(3)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "3 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
            }
        for (int f = 0; f < plugin.getEssenceData(Essence.Type.THREE_HOUR_150_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.THREE_HOUR_150_PERCENT;
            if (threeSlot < 36) {
                inv.setItem(threeSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(3)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "3 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }

        for (int f = 0; f < plugin.getEssenceData(Essence.Type.SIX_HOUR_50_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.SIX_HOUR_50_PERCENT;
            if (sixSlot < 54) {
                inv.setItem(sixSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(6)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "6 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getEssenceData(Essence.Type.SIX_HOUR_100_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.SIX_HOUR_100_PERCENT;
            if (sixSlot < 54) {
                inv.setItem(sixSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(6)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "6 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getEssenceData(Essence.Type.SIX_HOUR_150_PERCENT).get(player.getUniqueId()); f++) {
            e = Essence.Type.SIX_HOUR_150_PERCENT;
            if (sixSlot < 54) {
                inv.setItem(sixSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(6)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "6 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }

        player.openInventory(inv);
    }

}
