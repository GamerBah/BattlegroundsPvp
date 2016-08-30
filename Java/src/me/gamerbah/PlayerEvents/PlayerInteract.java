package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/13/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Menus.SettingsMenu;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Kits.KitManager;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Rarity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    private Battlegrounds plugin;

    public PlayerInteract(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!player.getGameMode().equals(GameMode.CREATIVE) && !(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR))) {
            event.setCancelled(true);
        }

        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL) {
            event.setCancelled(true);
        }

        if (item != null) {
            if (item.getType().equals(Material.NETHER_STAR)) {
                Inventory kitSelectorInventory = Bukkit.createInventory(null, 54, "Kit Selector");

                ItemStack commonLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(15).name(ChatColor.GRAY + "Common Kit").lore(ChatColor.GRAY + "Random unlock from the \"K-Slots\" Machine");
                for (int i = 36; i < 54; i++) {
                    kitSelectorInventory.setItem(i, commonLockedGlass);
                }

                ItemStack rareLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(11).name(ChatColor.BLUE + "Rare Kit").lore(ChatColor.GRAY + "Random unlock from the \"K-Slots\" Machine");
                for (int i = 18; i < 36; i++) {
                    kitSelectorInventory.setItem(i, rareLockedGlass);
                }

                ItemStack epicLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(1).name(BoldColor.GOLD.getColor() + "Epic Kit").lore(ChatColor.GRAY + "Random unlock from the \"K-Slots\" Machine");
                for (int i = 9; i < 18; i++) {
                    kitSelectorInventory.setItem(i, epicLockedGlass);
                }

                ItemStack legendaryLockedGlass = new I(Material.STAINED_GLASS_PANE).durability(2).name(BoldColor.PINK.getColor() + "Legendary Kit").lore(ChatColor.GRAY + "Random unlock from the \"K-Slots\" Machine");
                for (int i = 0; i < 9; i++) {
                    kitSelectorInventory.setItem(i, legendaryLockedGlass);
                }

                int commonSlot = 36, rareSlot = 18, epicSlot = 9, legendarySlot = 0;

                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                for (Kit kit : KitManager.getKits()) {
                    if (kit.getRarity() == Rarity.COMMON) {
                        if (playerData.getOwnedKits().contains(kit.getId() + ",")) {
                            kitSelectorInventory.setItem(commonSlot++, kit.getItem());
                        } else {
                            commonSlot++;
                        }
                    } else if (kit.getRarity() == Rarity.RARE) {
                        if (playerData.getOwnedKits().contains(kit.getId() + ",")) {
                            kitSelectorInventory.setItem(rareSlot++, kit.getItem());
                        } else {
                            rareSlot++;
                        }
                    } else if (kit.getRarity() == Rarity.EPIC) {
                        if (playerData.getOwnedKits().contains(kit.getId() + ",")) {
                            kitSelectorInventory.setItem(epicSlot++, kit.getItem());
                        } else {
                            epicSlot++;
                        }
                    } else if (kit.getRarity() == Rarity.LEGENDARY) {
                        if (playerData.getOwnedKits().contains(kit.getId() + ",")) {
                            kitSelectorInventory.setItem(legendarySlot++, kit.getItem());
                        } else {
                            legendarySlot++;
                        }
                    }
                }
                player.openInventory(kitSelectorInventory);

            } else if (item.getType().equals(Material.REDSTONE_COMPARATOR)) {
                SettingsMenu settingsMenu = new SettingsMenu(plugin);
                settingsMenu.openInventory(player);
            } else if (item.getType().equals(Material.BOOK)) {
                if (KitManager.getPreviousKit().containsKey(player.getUniqueId())) {
                    player.getInventory().setItem(0, null);
                    KitManager.getPreviousKit().get(player.getUniqueId()).wearCheckLevel(player);
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.85F);
                }
            } else if (item.getType().equals(Material.DIAMOND)) {
                player.sendMessage(ChatColor.RED + "Challenges aren't finished yet!");
                player.playSound(player.getLocation(), Sound.ENTITY_CAT_AMBIENT, 1, 1);
            } else if (item.getType().equals(Material.ENDER_PEARL)) {
                if (player.getLocation().distance(player.getWorld().getSpawnLocation()) < 12) {
                    event.setCancelled(true);
                    player.getInventory().setItem(1, new I(Material.ENDER_PEARL).name(Rarity.EPIC.getColor() + "Enderpearl").amount(5));
                }
            }
        }
    }
}
