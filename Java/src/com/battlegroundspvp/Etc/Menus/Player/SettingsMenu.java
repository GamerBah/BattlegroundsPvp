package com.battlegroundspvp.Etc.Menus.Player;
/* Created by GamerBah on 6/1/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.ParticleQuality;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
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
            inv.setItem(11, new I(Material.INK_SACK).durability(8)
                    .name(ChatColor.YELLOW + "Team Requests: " + BoldColor.RED.getColor() + "DISABLED")
                    .lore(ChatColor.GRAY + "Enabling this will allow players")
                    .lore(ChatColor.GRAY + "to send you team requests"));
        } else {
            inv.setItem(11, new I(Material.INK_SACK).durability(10)
                    .name(ChatColor.YELLOW + "Team Requests: " + BoldColor.GREEN.getColor() + "ENABLED")
                    .lore(ChatColor.GRAY + "Disabling this will prevent players")
                    .lore(ChatColor.GRAY + "from sending you team requests"));
        }

        if (!playerData.isPrivateMessaging()) {
            inv.setItem(15, new I(Material.INK_SACK).durability(8)
                    .name(ChatColor.YELLOW + "Private Messaging: " + BoldColor.RED.getColor() + "DISABLED")
                    .lore(ChatColor.GRAY + "Enabling this will allow players").lore(ChatColor.GRAY + "to privately message you"));
        } else {
            inv.setItem(15, new I(Material.INK_SACK).durability(10)
                    .name(ChatColor.YELLOW + "Private Messaging: " + BoldColor.GREEN.getColor() + "ENABLED")
                    .lore(ChatColor.GRAY + "Disabling this will prevent players")
                    .lore(ChatColor.GRAY + "from privately messaging you"));
        }

        if (playerData.getParticleQuality().equals(ParticleQuality.LOW)) {
            inv.setItem(13, ParticleQuality.LOW.getItem());
        }
        if (playerData.getParticleQuality().equals(ParticleQuality.MEDIUM)) {
            inv.setItem(13, ParticleQuality.MEDIUM.getItem());
        }
        if (playerData.getParticleQuality().equals(ParticleQuality.HIGH)) {
            //inv.setItem(13, ParticleQuality.HIGH.getItem());
            inv.setItem(13, new I(Material.CLAY_BRICK).name(ChatColor.AQUA + "Particle Quality").lore(BoldColor.RED.getColor() + "COMING SOON!"));
        }

        if (playerData.hasRank(Rank.MODERATOR)) {
            if (!playerData.isStealthyJoin()) {
                inv.setItem(12, new I(Material.SULPHUR)
                        .name(ChatColor.GOLD + "Stealthy Join: " + BoldColor.RED.getColor() + "DISABLED")
                        .lore(ChatColor.GRAY + "Enabling this will cause you to join")
                        .lore(ChatColor.GRAY + "the server without the notifications"));
            } else {
                inv.setItem(12, new I(Material.SUGAR)
                        .name(ChatColor.GOLD + "Stealthy Join: " + BoldColor.GREEN.getColor() + "ENABLED")
                        .lore(ChatColor.GRAY + "Disabling this will cause you to join")
                        .lore(ChatColor.GRAY + "the server with notifications"));
            }
            if (!Battlegrounds.getCmdspies().contains(player.getUniqueId())) {
                inv.setItem(14, new I(Material.SULPHUR)
                        .name(ChatColor.GOLD + "Command Spy: " + BoldColor.RED.getColor() + "DISABLED")
                        .lore(ChatColor.GRAY + "Enabling this will allow you to see")
                        .lore(ChatColor.GRAY + "every command that players execute"));
            } else {
                inv.setItem(14, new I(Material.SUGAR)
                        .name(ChatColor.GOLD + "Command Spy: " + BoldColor.GREEN.getColor() + "ENABLED")
                        .lore(ChatColor.GRAY + "Disabling this will stop you from seeing")
                        .lore(ChatColor.GRAY + "every command that players execute"));
            }
        }

        player.openInventory(inv);
    }
}
