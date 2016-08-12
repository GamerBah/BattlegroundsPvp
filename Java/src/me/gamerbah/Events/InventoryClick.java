package me.gamerbah.Events;
/* Created by GamerBah on 8/12/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Commands.ReportCommand;
import me.gamerbah.Etc.Menus.ReportGUI;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryClick implements Listener {

        private Battlegrounds plugin;

        public InventoryClick(Battlegrounds plugin) {
            this.plugin = plugin;
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onInventoryClick(InventoryClickEvent event) {
            Inventory inventory = event.getInventory();
            final Player player = (Player) event.getWhoClicked();

            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                if (event.getSlotType() == null || event.getCurrentItem() == null
                        || event.getCurrentItem().getType() == null) {
                    return;
                }
                if (inventory == player.getInventory()) {
                    event.setCancelled(true);
                }

                for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                    if (itemStack == null) {
                        event.setCancelled(true);
                    }
                    if (event.getCurrentItem().equals(itemStack)) {
                        event.setCancelled(true);
                    }
                }

                if (event.getClickedInventory().getName().contains("Reporting:")) {
                    ItemStack item = event.getCurrentItem();
                    ReportGUI reportGUI = new ReportGUI(plugin);
                    String targetName = inventory.getName().replace("Reporting: ", "");
                    Player target = plugin.getServer().getPlayerExact(targetName);
                    int slot = event.getSlot();
                    String message = ReportCommand.getReportBuilders().get(player.getUniqueId());
                    ArrayList<String> rm = ReportCommand.getReportArray().get(player.getUniqueId());

                    if (item.getType().equals(Material.BOOK)) {
                        rm.add(item.getItemMeta().getDisplayName().replace(ChatColor.RED + "", ""));
                        int size = ReportCommand.getReportArray().get(player.getUniqueId()).size();
                        for (int i = 0; i <= size - 1; i++) {
                            if (message != null) {
                                message = message + ", " + rm.get(i);
                            } else {
                                message = rm.get(i);
                            }
                        }
                        reportGUI.setWool(inventory, target, message);
                        inventory.setItem(slot, reportGUI.setSelected(item));
                    }
                    if (item.getType().equals(Material.ENCHANTED_BOOK)) {
                        rm.remove(item.getItemMeta().getDisplayName().replace(ChatColor.GREEN + "", ""));
                        message = null;
                        int size = ReportCommand.getReportArray().get(player.getUniqueId()).size();
                        for (int i = 0; i <= size - 1; i++) {
                            if (message != null) {
                                message = message + ", " + rm.get(i);
                            } else {
                                message = rm.get(i);
                            }
                        }
                        reportGUI.setWool(inventory, target, message);
                        inventory.setItem(slot, reportGUI.setUnSelected(item));
                    }
                    if (item.getType().equals(Material.WOOL)) {
                        if (item.getDurability() == 5) {
                            message = null;
                            int size = ReportCommand.getReportArray().get(player.getUniqueId()).size();
                            for (int i = 0; i <= size - 1; i++) {
                                if (message != null) {
                                    message = message + ", " + rm.get(i);
                                } else {
                                    message = rm.get(i);
                                }
                            }
                            if (message == null) {
                                player.closeInventory();
                                plugin.playSound(player, EventSound.COMMAND_FAIL);
                                player.sendMessage(plugin.redBold + "Oops! " + ChatColor.GRAY + "You tried to report a player without selecting any report options!");
                            } else {
                                reportGUI.report(player, target, message);
                                player.closeInventory();
                            }
                        }
                        if (item.getDurability() == 14) {
                            player.closeInventory();
                            player.sendMessage(ChatColor.RED + "Report cancelled.");
                            plugin.playSound(player, EventSound.COMMAND_CLICK);
                        }
                    }
                    event.setCancelled(true);
                }

            } else {
                return;
            }
        }
}
