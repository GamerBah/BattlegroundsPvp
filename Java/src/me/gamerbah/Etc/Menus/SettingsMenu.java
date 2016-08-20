package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 6/1/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Donations.Essence;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class SettingsMenu {

    private Battlegrounds plugin;

    public SettingsMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Essence essence = new Essence(plugin);
        int amount = essence.getEssenceAmount(player);
        Inventory inv = plugin.getServer().createInventory(null, 36, "Settings");
        inv.setItem(11, new I(Material.DIAMOND_SWORD).name(BoldColor.DARK_RED.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!").flag(ItemFlag.HIDE_ATTRIBUTES));
        inv.setItem(12, new I(Material.GLOWSTONE_DUST).name(BoldColor.DARK_RED.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!"));
        inv.setItem(13, new I(Material.MAGMA_CREAM).name(ChatColor.AQUA + "Particle Packs")
                .lore(ChatColor.GRAY + "Select cool trails to show off!"));
        inv.setItem(15, new I(Material.BLAZE_POWDER).name((amount == 0 ? ChatColor.RED + "Battle Essence" : ChatColor.GREEN + "Battle Essence"))
                .amount(amount).lore(amount == 0 ? ChatColor.GRAY + "You don't have Battle Essence!" : ChatColor.GRAY + "You have " + ChatColor.AQUA + amount + ChatColor.GRAY + " Battle "
                        + (amount == 1 ? "Essence" : "Essences")).lore(" ").lore(ChatColor.GRAY + "You can purchase Battle Essences at our store!")
                .lore(ChatColor.YELLOW + "battlgroundspvp.enjin.com/store"));
        if (!playerData.isTeamRequests()) {
            inv.setItem(20, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Team Requests: " + BoldColor.RED.getColor() + "DISABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Enabling this will allow players").lore(ChatColor.GRAY + "to send you team requests").durability(8));
        } else {
            inv.setItem(20, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Team Requests: " + BoldColor.GREEN.getColor() + "ENABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Disabling this will prevent players").lore(ChatColor.GRAY + "from sending you team requests").durability(10));
        }

        if (!playerData.isPrivateMessaging()) {
            inv.setItem(21, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Private Messaging: " + BoldColor.RED.getColor() + "DISABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Enabling this will allow players").lore(ChatColor.GRAY + "to privately message you").durability(8));
        } else {
            inv.setItem(21, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Private Messaging: " + BoldColor.GREEN.getColor() + "ENABLED").lore(" ")
                    .lore(ChatColor.GRAY + "Disabling this will prevent players").lore(ChatColor.GRAY + "from privately messaging you").durability(10));
        }

        if (playerData.hasRank(Rank.MODERATOR)) {
            if (!playerData.isStealthyJoin()) {
                inv.setItem(22, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Stealthy Join: " + BoldColor.RED.getColor() + "DISABLED").lore(" ")
                        .lore(ChatColor.GRAY + "Enabling this will cause you to join").lore(ChatColor.GRAY + "the server without the notifications").durability(8));
            } else {
                inv.setItem(22, new I(Material.INK_SACK).name(ChatColor.YELLOW + "Stealthy Join: " + BoldColor.GREEN.getColor() + "ENABLED").lore(" ")
                        .lore(ChatColor.GRAY + "Disabling this will cause you to join").lore(ChatColor.GRAY + "the server with notifications").durability(10));
            }
        }

        player.openInventory(inv);
    }
}
