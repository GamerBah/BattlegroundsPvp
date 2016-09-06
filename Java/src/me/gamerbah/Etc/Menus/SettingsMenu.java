package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 6/1/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsMenu {

    private Battlegrounds plugin;

    public SettingsMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 36, "Settings");

        if (!playerData.isTeamRequests()) {
            inv.setItem(11, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Team Requests: " + BoldColor.RED.getColor() + "DISABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Enabling this will allow players").lore(ChatColor.GRAY + "to send you team requests").durability(8));
        } else {
            inv.setItem(11, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Team Requests: " + BoldColor.GREEN.getColor() + "ENABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Disabling this will prevent players").lore(ChatColor.GRAY + "from sending you team requests").durability(10));
        }

        if (!playerData.isPrivateMessaging()) {
            inv.setItem(15, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Private Messaging: " + BoldColor.RED.getColor() + "DISABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Enabling this will allow players").lore(ChatColor.GRAY + "to privately message you").durability(8));
        } else {
            inv.setItem(15, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Private Messaging: " + BoldColor.GREEN.getColor() + "ENABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Disabling this will prevent players").lore(ChatColor.GRAY + "from privately messaging you").durability(10));
        }

        if (playerData.hasRank(Rank.MODERATOR)) {
            if (!playerData.isStealthyJoin()) {
                inv.setItem(13, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Stealthy Join: " + BoldColor.RED.getColor() + "DISABLED").lore(" ")
                        .lore(ChatColor.GRAY + "Enabling this will cause you to join").lore(ChatColor.GRAY + "the server without the notifications").durability(8));
            } else {
                inv.setItem(13, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Stealthy Join: " + BoldColor.GREEN.getColor() + "ENABLED").lore(" ")
                        .lore(ChatColor.GRAY + "Disabling this will cause you to join").lore(ChatColor.GRAY + "the server with notifications").durability(10));
            }
        }

        player.openInventory(inv);
    }
}
