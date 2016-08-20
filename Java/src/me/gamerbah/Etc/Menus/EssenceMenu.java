package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 8/17/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Donations.Essence;
import me.gamerbah.Utils.I;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class EssenceMenu {
    private Battlegrounds plugin;

    public EssenceMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Essence essence = new Essence(plugin);
        ArrayList<Essence.Type> type = Essence.convertToArray(playerData.getEssences());
        int amount = essence.getEssenceAmount(player);

        Inventory inv = plugin.getServer().createInventory(null, 54, "Battle Essences");

        int oneSlot = 0, threeSlot = 18, sixSlot = 36;

        for (int i = 0; i < amount; i++) {
            Essence.Type e = type.get(i);
            if (e.getTime() == 1) {
                if (oneSlot < 18) {
                    inv.setItem(oneSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(1)
                            .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                            .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                            .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                            .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "1 Hour" + ChatColor.GRAY + " upon activation").lore(" ")
                            .lore(ChatColor.YELLOW + "Click to activate!"));
                }
            }
            if (e.getTime() == 3) {
                if (threeSlot < 36) {
                    inv.setItem(threeSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(3)
                            .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                            .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                            .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                            .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "3 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                            .lore(ChatColor.YELLOW + "Click to activate!"));
                }
            }
            if (e.getTime() == 6) {
                inv.setItem(sixSlot++, new I(Material.BLAZE_POWDER).name(e.getColor() + e.getDisplayName()).amount(6)
                        .lore(ChatColor.GRAY + "Grants a server-wide " + e.getColor() + e.getIncrease() + "% increase" + ChatColor.GRAY + " to")
                        .lore(ChatColor.GRAY + "the amount of Souls and Battle Coins players")
                        .lore(ChatColor.GRAY + "receive when they kill another player").lore(" ")
                        .lore(ChatColor.GRAY + "Lasts for " + ChatColor.RED + "6 Hours" + ChatColor.GRAY + " upon activation").lore(" ")
                        .lore(ChatColor.YELLOW + "Click to activate!"));
            }
        }

        player.openInventory(inv);
    }

}
