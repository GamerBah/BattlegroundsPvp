package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 8/20/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Rarity;
import me.gamerbah.Utils.Trails.Trail;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TrailMenu {
    private Battlegrounds plugin;

    public TrailMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        Inventory inv = plugin.getServer().createInventory(null, 27, "Particle Packs");

        ItemStack rareLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(11).name(ChatColor.BLUE + "Rare Particle Pack").lore(ChatColor.GRAY + "Random unlock from a Cosmeticrate");
        for (int i = 9; i < 18; i++) {
            inv.setItem(i, rareLockedGlass);
        }

        ItemStack epicLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(1).name(BoldColor.GOLD.getColor() + "Epic Particle Pack").lore(ChatColor.GRAY + "Random unlock from a Cosmeticrate");
        for (int i = 0; i < 5; i++) {
            inv.setItem(i, epicLockedGlass);
        }

        ItemStack legendaryLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(2).name(BoldColor.PINK.getColor() + "Legendary Particle Pack").lore(ChatColor.GRAY + "Random unlock from a Cosmeticrate");
        for (int i = 5; i < 9; i++) {
            inv.setItem(i, legendaryLockedGlass);
        }

        int rareSlot = 9, epicSlot = 0, legendarySlot = 5;

        for (Trail.Type type : Trail.Type.values()) {
            if (type.getRarity() == Rarity.RARE) {
                inv.setItem(rareSlot++, type.getItem());
            } else if (type.getRarity() == Rarity.EPIC) {
                inv.setItem(epicSlot++, type.getItem());
            } else if (type.getRarity() == Rarity.LEGENDARY) {
                inv.setItem(legendarySlot++, type.getItem());
            }
        }

        inv.setItem(26, Trail.Type.NONE.getItem());
        inv.setItem(22, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));
        player.openInventory(inv);
    }
}
