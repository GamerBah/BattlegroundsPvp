package com.battlegroundspvp.Etc.Menus;
/* Created by GamerBah on 9/17/2016 */


import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Runnables.AutoUpdate;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Achievements.Achievement;
import com.battlegroundspvp.Listeners.ScoreboardListener;
import com.battlegroundspvp.Utils.Enums.Cosmetic;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Enums.Rarity;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CosmeticrateMenu {
    public static List<Player> usingCrates = new ArrayList<>();
    private Battlegrounds plugin;

    public CosmeticrateMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        Inventory inv = plugin.getServer().createInventory(null, 27, "Cosmeticrates");
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        inv.setItem(12, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));
        inv.setItem(13, new I(Material.CHEST).name((playerData.getCoins() >= 500 ? BoldColor.GREEN.getColor() + "Click to Buy!" : BoldColor.RED.getColor() + "Need more coins!"))
                .lore(ChatColor.GRAY + "Cost: " + ChatColor.AQUA + "500 Coins"));
        inv.setItem(14, new I(Material.STAINED_GLASS_PANE).name(" ").durability(15));


        player.openInventory(inv);
    }

    public void beginCrates(Player player) {
        usingCrates.add(player);
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        scoreboardListener.updateScoreboardCoins(player, -500);
        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.clear();
        inventory.setItem(12, new I(Material.STAINED_GLASS_PANE).name(" ").durability(14));
        inventory.setItem(14, new I(Material.STAINED_GLASS_PANE).name(" ").durability(14));
        List<Cosmetic.Item> cosmetics = new ArrayList<>();
        for (Cosmetic.Item cosmetic : Cosmetic.Item.values()) {
            cosmetics.add(cosmetic);
        }
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.0F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.0F);
            }
        });
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.1F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.1F);
            }
        }, 1L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.2F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.2F);

            }
        }, 2L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.3F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.3F);
            }
        }, 3L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.4F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.4F);
            }
        }, 4L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.5F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.5F);
            }
        }, 5L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.6F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.6F);
            }
        }, 6L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.7F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.7F);
            }
        }, 7L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.8F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.8F);
            }
        }, 8L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 1.9F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 1.9F);
            }
        }, 9L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 1F, 2.0F);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1F, 2.0F);
            }
        }, 10L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (!AutoUpdate.updating) {
                inventory.setItem(12, new I(Material.STAINED_GLASS_PANE).name(" ").durability(5));
                inventory.setItem(14, new I(Material.STAINED_GLASS_PANE).name(" ").durability(5));
                inventory.setItem(13, cosmetics.get(ThreadLocalRandom.current().nextInt(0, cosmetics.size() - 1)).getItem());
                if (!player.getOpenInventory().getTopInventory().equals(inventory)) {
                    player.updateInventory();
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1F);
                Cosmetic.Item finalCosmetic = cosmetics.get(0);
                for (Cosmetic.Item item : cosmetics) {
                    if (item.getItem().equals(inventory.getItem(13))) {
                        finalCosmetic = item;
                        if (item.getRarity().equals(Rarity.EPIC)) {
                            EventSound.playSound(player, EventSound.ITEM_RECEIVE_EPIC);
                        }
                        if (item.getRarity().equals(Rarity.LEGENDARY)) {
                            EventSound.playSound(player, EventSound.ITEM_RECEIVE_LEGENDARY);
                        }
                    }
                }
                usingCrates.remove(player);
                if (!playerData.getOwnedCosmetics().contains("," + finalCosmetic.getId() + ",")) {
                    playerData.setOwnedCosmetics(playerData.getOwnedCosmetics() + finalCosmetic.getId() + ",");
                    player.sendMessage(ChatColor.AQUA + "You unlocked the " + finalCosmetic.getRarity().getColor() + (finalCosmetic.getRarity() == Rarity.EPIC
                            || finalCosmetic.getRarity() == Rarity.LEGENDARY ? "" + ChatColor.BOLD : "") + finalCosmetic.getName() + ChatColor.AQUA
                            + (finalCosmetic.getGroup().equals(Cosmetic.PARTICLE_PACK) ? " Particle Pack!" : finalCosmetic.getGroup().equals(Cosmetic.KILL_EFFECT)
                            ? " Gore!" : " Warcry!"));
                    int particles = 0;
                    int warcries = 0;
                    int gores = 0;
                    for (Cosmetic.Item item : Cosmetic.Item.values()) {
                        if (item.getId() < 1000) {
                            if (playerData.getOwnedCosmetics().contains(item.getId() + ",")) {
                                if (item.getGroup().equals(Cosmetic.PARTICLE_PACK)) particles++;
                                if (item.getGroup().equals(Cosmetic.KILL_SOUND)) warcries++;
                                if (item.getGroup().equals(Cosmetic.KILL_EFFECT)) gores++;
                            }
                        }
                    }
                    for (Achievement.Type achievement : Achievement.Type.values()) {
                        if (achievement.getGroup().equals(Achievement.COLLECTION)) {
                            if (achievement.getName().contains("Showmanship")) {
                                if (particles >= achievement.getRequirement()) {
                                    Achievement.sendUnlockMessage(player, achievement);
                                }
                            }
                            if (achievement.getName().contains("Warcry")) {
                                if (warcries >= achievement.getRequirement()) {
                                    Achievement.sendUnlockMessage(player, achievement);
                                }
                            }
                            if (achievement.getName().contains("Savage")) {
                                if (gores >= achievement.getRequirement()) {
                                    Achievement.sendUnlockMessage(player, achievement);
                                }
                            }
                        }
                    }
                } else {
                    int souls = ThreadLocalRandom.current().nextInt(20, 35 + 1);
                    player.sendMessage(ChatColor.GRAY + "You already have the " + finalCosmetic.getRarity().getColor() + (finalCosmetic.getRarity() == Rarity.EPIC
                            || finalCosmetic.getRarity() == Rarity.LEGENDARY ? "" + ChatColor.BOLD : "") + finalCosmetic.getName()
                            + ChatColor.GRAY + (finalCosmetic.getGroup().equals(Cosmetic.PARTICLE_PACK) ? " Particle Pack" : finalCosmetic.getGroup().equals(Cosmetic.KILL_EFFECT)
                            ? " Gore" : " Warcry") + ",\n" + ChatColor.GRAY + " so you got " + BoldColor.AQUA.getColor() + souls + " Souls");
                    scoreboardListener.updateScoreboardSouls(player, souls);
                }
            }
        }, 30L);
    }
}
