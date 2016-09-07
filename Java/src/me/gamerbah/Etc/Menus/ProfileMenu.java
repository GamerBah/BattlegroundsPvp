package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 9/5/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Achievements.Achievements;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.KDRatio;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Rarity;
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
        Achievements.Combat combat = null;
        Achievements.Challenge challenge = null;
        for (Achievements.Combat achievements : Achievements.Combat.values()) {
            if (achievements.toString().equals(playerData.getTitle())) {
                combat = achievements;
            }
        }
        for (Achievements.Challenge achievements : Achievements.Challenge.values()) {
            if (achievements.toString().equals(playerData.getTitle())) {
                challenge = achievements;
            }
        }

        ItemStack head = new I(Material.SKULL_ITEM).durability(3)
                .name(playerData.getRank().getColor() + "" + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                        + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : ChatColor.GRAY) + player.getName())
                .lore(" ")
                .lore(ChatColor.GRAY + "Kills: " + ChatColor.GREEN + playerData.getKills())
                .lore(ChatColor.GRAY + "Deaths: " + ChatColor.RED + playerData.getDeaths())
                .lore(ChatColor.GRAY + "K/D Ratio: " + kdRatio.getRatioColor(player) + kdRatio.getRatio(player))
                .lore(ChatColor.GRAY + "Longest Killstreak: " + ChatColor.DARK_AQUA + playerData.getHighestKillstreak())
                .lore(ChatColor.GRAY + "Revenge Kills: " + ChatColor.BLUE + playerData.getRevengeKills())
                .lore(ChatColor.GRAY + "Last Killed By: " + (killedByPlayerData != null ? (killedByPlayerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : "")
                        + killedByPlayerData.getName() : "--"))
                .lore(" ")
                .lore(ChatColor.GRAY + "Active Particle Pack: " + playerData.getTrail().getRarity().getColor() + (playerData.getTrail().getRarity() == Rarity.EPIC
                        || playerData.getTrail().getRarity() == Rarity.LEGENDARY ? "" + ChatColor.BOLD : "") + playerData.getTrail().getName())
                .lore(ChatColor.GRAY + "Mastery Title: " + (combat != null ? BoldColor.GOLD.getColor() + "[" + combat.getTitle() + "]"
                        : (challenge != null ? BoldColor.GOLD.getColor() + "[" + challenge.getTitle() + "]" : "None")))
                .lore(" ")
                .lore(ChatColor.GRAY + "Souls: " + ChatColor.AQUA + playerData.getSouls())
                .lore(ChatColor.GRAY + "Battle Coins: " + ChatColor.LIGHT_PURPLE + playerData.getCoins());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(player.getName());
        head.setItemMeta(meta);

        inv.setItem(10, new I(Material.DIAMOND_SWORD)
                .name(BoldColor.DARK_RED.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!").flag(ItemFlag.HIDE_ATTRIBUTES));
        inv.setItem(11, new I(Material.GLOWSTONE_DUST)
                .name(BoldColor.DARK_RED.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!"));
        inv.setItem(12, new I(Material.MAGMA_CREAM)
                .name(ChatColor.AQUA + "Particle Packs")
                .lore(ChatColor.GRAY + "Select cool trails to show off!"));
        inv.setItem(13, head);
        inv.setItem(14, new I(Material.EMERALD)
                .name(ChatColor.AQUA + "Achievements")
                .lore(ChatColor.GRAY + "View your completed achievements"));
        inv.setItem(15, new I(Material.DIAMOND)
                .name(ChatColor.YELLOW + "Daily Challenges")
                .lore(ChatColor.GRAY + "View the daily challenges, start")
                .lore(ChatColor.GRAY + "new ones, and receive rewards!"));
        inv.setItem(16, new I(Material.BLAZE_POWDER)
                .name((amount == 0 ? ChatColor.RED + "Battle Essence" : ChatColor.GREEN + "Battle Essence"))
                .amount(amount)
                .lore(amount == 0 ? ChatColor.GRAY + "You don't have Battle Essence!" : ChatColor.GRAY + "You have " + ChatColor.AQUA + amount + ChatColor.GRAY + " Battle "
                        + (amount == 1 ? "Essence" : "Essences")).lore(" ").lore(ChatColor.GRAY + "You can purchase Battle Essences at our store!")
                .lore(ChatColor.YELLOW + "battlegroundspvp.com/store"));

        player.openInventory(inv);
    }

}