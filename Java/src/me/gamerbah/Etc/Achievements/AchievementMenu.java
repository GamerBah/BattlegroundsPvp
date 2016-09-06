package me.gamerbah.Etc.Achievements;
/* Created by GamerBah on 9/4/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class AchievementMenu {
    private Battlegrounds plugin;

    public AchievementMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 45, "Achievement Selection");
        inv.setItem(10, new I(Material.GOLD_SWORD).name(BoldColor.RED.getColor() + "Combat Achievements")
                .lore(ChatColor.GRAY + "View Achievements related to Kills and Deaths").flag(ItemFlag.HIDE_ATTRIBUTES));
        inv.setItem(11, new I(Material.DIAMOND).name(BoldColor.YELLOW.getColor() + "Challenge Achievements")
                .lore(ChatColor.GRAY + "View Achievements related to Challenges"));
        inv.setItem(12, new I(Material.BUCKET).name(BoldColor.GREEN.getColor() + "Collection Achievements")
                .lore(ChatColor.GRAY + "View Achievements related to Cosmetics and Kit Collection"));
        inv.setItem(13, new I(Material.SKULL_ITEM).durability(3).name(BoldColor.GOLD.getColor() + "Recruitment Achievements")
                .lore(ChatColor.GRAY + "View Achievements related to Recruitment and Friends"));
        inv.setItem(14, new I(Material.NAME_TAG).name(BoldColor.DARK_GREEN.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!"));
        inv.setItem(15, new I(Material.DIAMOND_BOOTS).name(BoldColor.PINK.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!").flag(ItemFlag.HIDE_ATTRIBUTES));
        inv.setItem(16, new I(Material.BLAZE_POWDER).name(BoldColor.AQUA.getColor() + "???")
                .lore(BoldColor.RED.getColor() + "COMING SOON!"));

        inv.setItem(31, new I(Material.BARRIER).name(ChatColor.RED + "Remove your Mastery Title").lore(ChatColor.GRAY + "Removes your equipped Mastery Title"));

        player.openInventory(inv);
    }

    public void openCombatAchievements(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 54, "Combat Achievements");
        int kills = playerData.getKills();
        int deaths = playerData.getDeaths();
        int killstreaksEnded = playerData.getKillstreaksEnded();
        int revengeKills = playerData.getRevengeKills();
        int highestKillstreak = playerData.getHighestKillstreak();
        for (int i = 0; i < 36; i++) {
            for (Achievements.Combat ach : Achievements.Combat.values()) {
                if (i < 6) {
                    if (ach.getName().contains("Brutality")) {
                        if (ach.getName().contains("Mastery")) {
                            inv.setItem(i++, new I((kills >= ach.getRequirement() ? Material.EMERALD_BLOCK : Material.COAL_BLOCK)).name((kills >= ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (kills >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(" + ChatColor.GOLD + kills + ChatColor.GRAY + "/" + ach.getRequirement() + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins")
                                    .lore("§7 ● " + BoldColor.GOLD.getColor() + "[" + ach.getTitle() + "] Mastery Title"));
                        } else {
                            inv.setItem(i++, new I(Material.STAINED_CLAY).name((kills > ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .durability((kills >= ach.getRequirement() ? 5 : 14))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (kills >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(" + ChatColor.GOLD + kills + ChatColor.GRAY + "/" + ach.getRequirement() + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins"));
                        }
                    }
                }
                if (i > 8 && i < 15) {
                    if (ach.getName().contains("Vengeful")) {
                        if (ach.getName().contains("Mastery")) {
                            inv.setItem(i++, new I((revengeKills >= ach.getRequirement() ? Material.EMERALD_BLOCK : Material.COAL_BLOCK)).name((revengeKills >= ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (revengeKills >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(" + ChatColor.GOLD + revengeKills + ChatColor.GRAY + "/" + ach.getRequirement() + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins")
                                    .lore("§7 ● " + BoldColor.GOLD.getColor() + "[" + ach.getTitle() + "] Mastery Title"));
                        } else {
                            inv.setItem(i++, new I(Material.STAINED_CLAY).name((revengeKills > ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .durability((revengeKills >= ach.getRequirement() ? 5 : 14))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (revengeKills >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(" + ChatColor.GOLD + revengeKills + ChatColor.GRAY + "/" + ach.getRequirement() + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins"));
                        }
                    }
                }
                if (i > 17 && i < 24) {
                    if (ach.getName().contains("Buzzkill")) {
                        if (ach.getName().contains("Mastery")) {
                            inv.setItem(i++, new I((killstreaksEnded >= ach.getRequirement() ? Material.EMERALD_BLOCK : Material.COAL_BLOCK)).name((killstreaksEnded >= ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (killstreaksEnded >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(" + ChatColor.GOLD + killstreaksEnded + ChatColor.GRAY + "/" + ach.getRequirement() + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins")
                                    .lore("§7 ● " + BoldColor.GOLD.getColor() + "[" + ach.getTitle() + "] Mastery Title"));
                        } else {
                            inv.setItem(i++, new I(Material.STAINED_CLAY).name((killstreaksEnded > ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .durability((killstreaksEnded >= ach.getRequirement() ? 5 : 14))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (killstreaksEnded >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(" + ChatColor.GOLD + killstreaksEnded + ChatColor.GRAY + "/" + ach.getRequirement() + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins"));
                        }
                    }
                }
                if (i > 26 && i < 34) {
                    if (ach.getName().contains("Sadist")) {
                        if (ach.getName().contains("Mastery")) {
                            inv.setItem(i++, new I((highestKillstreak >= ach.getRequirement() ? Material.EMERALD_BLOCK : Material.COAL_BLOCK)).name((highestKillstreak >= ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (highestKillstreak >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(Highest Reached: " + ChatColor.GOLD + highestKillstreak + ChatColor.GRAY + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins")
                                    .lore("§7 ● " + BoldColor.GOLD.getColor() + "[" + ach.getTitle() + "] Mastery Title"));
                        } else {
                            inv.setItem(i++, new I(Material.STAINED_CLAY).name((highestKillstreak > ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                                    .durability((highestKillstreak >= ach.getRequirement() ? 5 : 14))
                                    .lore(ChatColor.GRAY + ach.getDescription())
                                    .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Status: " + (highestKillstreak >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                            + ChatColor.GRAY + "(Highest Reached: " + ChatColor.GOLD + highestKillstreak + ChatColor.GRAY + ")"))
                                    .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                                    .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                                    .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins"));
                        }
                    }
                }
                if (i > 33) {
                    int slot = 7;
                    inv.setItem(slot, new I((deaths >= ach.getRequirement() ? Material.GLOWSTONE_DUST : Material.SULPHUR))
                            .name((deaths >= ach.getRequirement() ? BoldColor.GREEN.getColor() + ach.getName() : ChatColor.RED + ach.getName()))
                            .lore(ChatColor.GRAY + ach.getDescription())
                            .lore(ChatColor.GRAY + "Status: " + (deaths >= ach.getRequirement() ? BoldColor.GREEN.getColor() + "COMPLETE" : BoldColor.RED.getColor() + "INCOMPLETE "
                                    + ChatColor.GRAY + "(" + ChatColor.GOLD + deaths + ChatColor.GRAY + "/" + ach.getRequirement() + ")"))
                            .lore("").lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rewards:")
                            .lore("§7 ● " + BoldColor.AQUA.getColor() + ach.getSoulReward() + " Souls")
                            .lore("§7 ● " + BoldColor.PINK.getColor() + ach.getCoinReward() + " Battle Coins")
                            .lore("§7 ● " + BoldColor.GOLD.getColor() + "[" + ach.getTitle() + "] Mastery Title"));
                    i += 9;
                }
            }
            inv.setItem(48, new I(Material.ARROW).name(ChatColor.GRAY + "Go Back"));
        }
        player.openInventory(inv);
    }

}
