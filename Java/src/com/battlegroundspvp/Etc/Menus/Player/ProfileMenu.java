package com.battlegroundspvp.Etc.Menus.Player;
/* Created by GamerBah on 9/5/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Achievements.Achievement;
import com.battlegroundspvp.Utils.Enums.Rarity;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.KDRatio;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class ProfileMenu {
    private Battlegrounds plugin;

    public ProfileMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        Inventory inv = plugin.getServer().createInventory(null, 27, "Profile");
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        KDRatio kdRatio = new KDRatio(plugin);
        int amount = plugin.getTotalEssenceAmount(player);
        PlayerData killedByPlayerData = null;
        if (!playerData.getLastKilledBy().equals("NONE")) {
            killedByPlayerData = plugin.getPlayerData(UUID.fromString(playerData.getLastKilledBy()));
        }
        Achievement.Type achievement = null;
        for (Achievement.Type achievements : Achievement.Type.values()) {
            if (achievements.toString().equals(playerData.getTitle())) {
                achievement = achievements;
            }
        }

        ItemStack head = new I(Material.SKULL_ITEM).durability(3)
                .name(playerData.getRank().getColor() + "" + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                        + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : ChatColor.GRAY) + player.getName())
                .lore(ChatColor.GRAY + "Kills: " + ChatColor.GREEN + playerData.getKills())
                .lore(ChatColor.GRAY + "Deaths: " + ChatColor.RED + playerData.getDeaths())
                .lore(ChatColor.GRAY + "K/D Ratio: " + kdRatio.getRatioColor(player) + kdRatio.getRatio(player))
                .lore(ChatColor.GRAY + "Longest Killstreak: " + ChatColor.DARK_AQUA + playerData.getHighestKillstreak())
                .lore(ChatColor.GRAY + "Revenge Kills: " + ChatColor.BLUE + playerData.getRevengeKills())
                .lore(ChatColor.GRAY + "Killstreaks Ended: " + ChatColor.YELLOW + playerData.getKillstreaksEnded())
                .lore(ChatColor.GRAY + "Last Killed By: " + (killedByPlayerData != null ? (killedByPlayerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : "")
                        + killedByPlayerData.getName() : "--"))
                .lore(" ")
                .lore(ChatColor.GRAY + "Particle Pack: " + (playerData.getTrail().getRarity() == Rarity.COMMON ? ChatColor.DARK_GRAY : playerData.getTrail().getRarity().getColor())
                        + (playerData.getTrail().getRarity() == Rarity.EPIC || playerData.getTrail().getRarity() == Rarity.LEGENDARY ? "" + ChatColor.BOLD : "") + playerData.getTrail().getName())
                .lore(ChatColor.GRAY + "Warcry: " + (playerData.getWarcry().getRarity() == Rarity.COMMON ? ChatColor.DARK_GRAY : playerData.getWarcry().getRarity().getColor())
                        + (playerData.getWarcry().getRarity() == Rarity.EPIC || playerData.getWarcry().getRarity() == Rarity.LEGENDARY ? "" + ChatColor.BOLD : "") + playerData.getWarcry().getName())
                .lore(ChatColor.GRAY + "Gore: " + (playerData.getGore().getRarity() == Rarity.COMMON ? ChatColor.DARK_GRAY : playerData.getGore().getRarity().getColor())
                        + (playerData.getGore().getRarity() == Rarity.EPIC || playerData.getGore().getRarity() == Rarity.LEGENDARY ? "" + ChatColor.BOLD : "") + playerData.getGore().getName())
                .lore(ChatColor.GRAY + "Mastery Title: " + (achievement != null ? BoldColor.GOLD.getColor() + "[" + achievement.getTitle() + "]" : "None"))
                .lore(" ")
                .lore(ChatColor.GRAY + "Souls: " + ChatColor.AQUA + playerData.getSouls())
                .lore(ChatColor.GRAY + "Battle Coins: " + ChatColor.LIGHT_PURPLE + playerData.getCoins());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(player.getName());
        head.setItemMeta(meta);

        inv.setItem(10, new I(Material.DIAMOND_SWORD)
                .name(ChatColor.AQUA + "Warcries")
                .lore(ChatColor.GRAY + "Make sure your kills are heard!").flag(ItemFlag.HIDE_ATTRIBUTES));
        inv.setItem(11, new I(Material.GLOWSTONE_DUST)
                .name(ChatColor.AQUA + "Gores")
                .lore(ChatColor.GRAY + "Effects for your kills!"));
        inv.setItem(12, new I(Material.MAGMA_CREAM)
                .name(ChatColor.AQUA + "Particle Packs")
                .lore(ChatColor.GRAY + "Select cool trails to show off!"));
        inv.setItem(13, head);
        inv.setItem(14, new I(Material.EMERALD)
                .name(ChatColor.GREEN + "Achievements")
                .lore(ChatColor.GRAY + "View your completed achievements"));
        inv.setItem(15, new I(Material.DIAMOND)
                .name(ChatColor.YELLOW + "Daily Challenges")
                .lore(ChatColor.GRAY + "View the daily challenges, start")
                .lore(ChatColor.GRAY + "new ones, and receive rewards!")
                .lore("").lore(BoldColor.RED.getColor() + "COMING SOON!"));
        inv.setItem(16, new I(Material.BLAZE_POWDER)
                .name((amount == 0 ? ChatColor.RED + "Battle Essence" : ChatColor.GREEN + "Battle Essence"))
                .amount(amount)
                .lore(amount == 0 ? ChatColor.GRAY + "You don't have Battle Essence!" : ChatColor.GRAY + "You have " + ChatColor.AQUA + amount + ChatColor.GRAY + " Battle "
                        + (amount == 1 ? "Essence" : "Essences")).lore(" ").lore(ChatColor.GRAY + "Purchase Battle Essences at our store!")
                .lore(ChatColor.YELLOW + "store.battlegroundspvp.com"));

        player.openInventory(inv);
    }

}
