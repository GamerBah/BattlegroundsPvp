package com.battlegroundspvp.Etc.Menus;
/* Created by GamerBah on 9/17/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Runnables.AutoUpdate;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Listeners.ScoreboardListener;
import com.battlegroundspvp.Utils.Cosmetic;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Rarity;
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
        inventory.setItem(12, new I(Material.STAINED_GLASS_PANE).name(" ").durability(5));
        inventory.setItem(14, new I(Material.STAINED_GLASS_PANE).name(" ").durability(5));
        List<Cosmetic.Item> cosmetics = new ArrayList<>();
        for (Cosmetic.Item cosmetic : Cosmetic.Item.values()) {
            cosmetics.add(cosmetic);
        }
        int crate = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (player.getOpenInventory().getTopInventory().getName().equals("Cosmeticrates")) {
                Cosmetic.Item cosmetic = cosmetics.get(ThreadLocalRandom.current().nextInt(0, cosmetics.size()));
                if (cosmetic.getId() < 1000) {
                    inventory.setItem(13, cosmetic.getItem());
                    player.openInventory(inventory);
                    Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                    Battlegrounds.playSound(player, EventSound.CLICK);
                }
            }
        }, 0L, 2L);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getServer().getScheduler().cancelTask(crate);
            if (!AutoUpdate.updating) {
                if (!player.getOpenInventory().getTopInventory().equals(inventory)) {
                    player.openInventory(inventory);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 1F);
                Cosmetic.Item finalCosmetic = cosmetics.get(0);
                for (Cosmetic.Item item : cosmetics) {
                    if (item.getItem().equals(inventory.getItem(13))) {
                        finalCosmetic = item;
                        if (item.getRarity().equals(Rarity.EPIC)) {
                            Battlegrounds.playSound(player, EventSound.ITEM_RECEIVE_EPIC);
                        }
                        if (item.getRarity().equals(Rarity.LEGENDARY)) {
                            Battlegrounds.playSound(player, EventSound.ITEM_RECEIVE_LEGENDARY);
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
                } else {
                    int souls = ThreadLocalRandom.current().nextInt(20, 35 + 1);
                    player.sendMessage(ChatColor.GRAY + "You already have the " + finalCosmetic.getRarity().getColor() + (finalCosmetic.getRarity() == Rarity.EPIC
                            || finalCosmetic.getRarity() == Rarity.LEGENDARY ? "" + ChatColor.BOLD : "") + finalCosmetic.getName()
                            + ChatColor.GRAY + (finalCosmetic.getGroup().equals(Cosmetic.PARTICLE_PACK) ? " Particle Pack" : finalCosmetic.getGroup().equals(Cosmetic.KILL_EFFECT)
                            ? " Gore" : " Warcry") + ", so\n" + ChatColor.GRAY + "you got " + BoldColor.AQUA.getColor() + souls + " Souls");
                    scoreboardListener.updateScoreboardCoins(player, souls);
                }
            }
        }, 50L);
    }
}
