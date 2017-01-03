package com.battlegroundspvp.Etc.Menus.Cosmetics;
/* Created by GamerBah on 8/20/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.Cosmetic;
import com.battlegroundspvp.Utils.Enums.Rarity;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WarcryMenu {
    private Battlegrounds plugin;

    public WarcryMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        Inventory inv = plugin.getServer().createInventory(null, 27, "Warcries");

        ItemStack rareLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(11).name(ChatColor.BLUE + "Rare Warcry").lore(ChatColor.GRAY + "Random unlock from a Cosmeticrate");
        for (int i = 9; i < 18; i++) {
            inv.setItem(i, rareLockedGlass);
        }

        ItemStack epicLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(1).name(BoldColor.GOLD.getColor() + "Epic Warcry").lore(ChatColor.GRAY + "Random unlock from a Cosmeticrate");
        for (int i = 0; i < 5; i++) {
            inv.setItem(i, epicLockedGlass);
        }

        ItemStack legendaryLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(2).name(BoldColor.PINK.getColor() + "Legendary Warcry").lore(ChatColor.GRAY + "Random unlock from a Cosmeticrate");
        for (int i = 5; i < 9; i++) {
            inv.setItem(i, legendaryLockedGlass);
        }

        int rareSlot = 9, epicSlot = 0, legendarySlot = 5;

        for (Cosmetic.Item item : Cosmetic.Item.values()) {
            if (item.getGroup().equals(Cosmetic.KILL_SOUND)) {
                if (item.getRarity() == Rarity.RARE) {
                    if (playerData.getOwnedCosmetics().contains(item.getId() + ",")) {
                        inv.setItem(rareSlot++, item.getItem());
                    }
                } else if (item.getRarity() == Rarity.EPIC) {
                    if (playerData.getOwnedCosmetics().contains(item.getId() + ",")) {
                        inv.setItem(epicSlot++, item.getItem());
                    }
                } else if (item.getRarity() == Rarity.LEGENDARY) {
                    if (playerData.getOwnedCosmetics().contains(item.getId() + ",")) {
                        inv.setItem(legendarySlot++, item.getItem());
                    }
                }
            }
        }

        inv.setItem(26, Cosmetic.Item.WARCRY_NONE.getItem());
        inv.setItem(22, new I(Material.ARROW).name(ChatColor.GRAY + "\u00AB Back"));
        player.openInventory(inv);
    }
}
