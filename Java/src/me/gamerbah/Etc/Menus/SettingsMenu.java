package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 6/1/2016 */


import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.I;
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
        inv.setItem(11, new I(Material.DIAMOND_SWORD).name(BoldColor.DARK_RED.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!"));
        inv.setItem(13, new I(Material.GLOWSTONE_DUST).name(BoldColor.DARK_RED.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!"));
        inv.setItem(15, new I(Material.SEEDS).name(BoldColor.DARK_RED.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!"));
        if (!playerData.isTeamRequests()) {
            inv.setItem(21, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Team Requests: " + ChatColor.RED + "" + ChatColor.BOLD + "DISABLED").durability(8));
        } else {
            inv.setItem(21, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Team Requests: " + ChatColor.GREEN + "" + ChatColor.BOLD + "ENABLED").durability(10));
        }

        if (!playerData.isPrivateMessaging()) {
            inv.setItem(23, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Private Messaging: " + ChatColor.RED + "" + ChatColor.BOLD + "DISABLED").durability(8));
        } else {
            inv.setItem(23, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Private Messaging: " + ChatColor.GREEN + "" + ChatColor.BOLD + "ENABLED").durability(10));
        }

        if (playerData.hasRank(Rank.MODERATOR)) {
            if (!playerData.isStealthyJoin()) {
                inv.setItem(26, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Stealthy Join: " + ChatColor.RED + "" + ChatColor.BOLD + "DISABLED").durability(8));
            } else {
                inv.setItem(26, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Stealthy Join: " + ChatColor.GREEN + "" + ChatColor.BOLD + "ENABLED").durability(10));
            }
        }

        player.openInventory(inv);
    }

}
