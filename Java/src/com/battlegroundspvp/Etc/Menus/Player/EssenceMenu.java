package com.battlegroundspvp.Etc.Menus.Player;
/* Created by GamerBah on 8/17/2016 */


import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.I;
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
        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getOne50(); f++) {
            e = Essence.Type.ONE_HOUR_50_PERCENT;
            if (oneSlot < 18) {
                inv.setItem(oneSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(1)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "1 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getOne100(); f++) {
            e = Essence.Type.ONE_HOUR_100_PERCENT;
            if (oneSlot < 18) {
                inv.setItem(oneSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(1)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "1 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getOne150(); f++) {
            e = Essence.Type.ONE_HOUR_150_PERCENT;
            if (oneSlot < 18) {
                inv.setItem(oneSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(1)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "1 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }

        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getThree50(); f++) {
            e = Essence.Type.THREE_HOUR_50_PERCENT;
            if (threeSlot < 36) {
                inv.setItem(threeSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(3)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "3 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getThree100(); f++) {
            e = Essence.Type.THREE_HOUR_100_PERCENT;
            if (threeSlot < 36) {
                inv.setItem(threeSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(3)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "3 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getThree150(); f++) {
            e = Essence.Type.THREE_HOUR_150_PERCENT;
            if (threeSlot < 36) {
                inv.setItem(threeSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(3)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "3 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }

        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getSix50(); f++) {
            e = Essence.Type.SIX_HOUR_50_PERCENT;
            if (sixSlot < 45) {
                inv.setItem(sixSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(6)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "6 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getSix100(); f++) {
            e = Essence.Type.SIX_HOUR_100_PERCENT;
            if (sixSlot < 45) {
                inv.setItem(sixSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(6)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "6 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        for (int f = 0; f < plugin.getPlayerData(player.getUniqueId()).getEssenceData().getSix150(); f++) {
            e = Essence.Type.SIX_HOUR_150_PERCENT;
            if (sixSlot < 45) {
                inv.setItem(sixSlot++, new I(Material.BLAZE_POWDER).name(e.getDisplayName(true)).amount(6)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getChatColor() + e.getPercent() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "6 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }
        inv.setItem(49, new I(Material.ARROW).name(ChatColor.GRAY + "\u00AB Back"));

        player.openInventory(inv);
    }

}
