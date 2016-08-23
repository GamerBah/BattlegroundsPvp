package me.gamerbah.Listeners;
/* Created by GamerBah on 8/12/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Donations.DonationMessages;
import me.gamerbah.Administration.Donations.Essence;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Commands.ReportCommand;
import me.gamerbah.Etc.Menus.EssenceMenu;
import me.gamerbah.Etc.Menus.ReportGUI;
import me.gamerbah.Etc.Menus.SettingsMenu;
import me.gamerbah.Etc.Menus.TrailMenu;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.FireworkUtils;
import me.gamerbah.Utils.Kits.KitManager;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Rarity;
import me.gamerbah.Utils.Teams.TeamMessages;
import me.gamerbah.Utils.Trails.Trail;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryClickListener implements Listener {

    private Battlegrounds plugin;

    public InventoryClickListener(Battlegrounds plugin) {
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

            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null
                    && event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                if (inventory.getName().equals("Kit Selector")) {
                    KitManager.getKits().stream().filter(kit -> event.getCurrentItem().getItemMeta().getDisplayName()
                            .equals(kit.getItem().getItemMeta().getDisplayName())).forEach(kit -> {
                        kit.wearCheckLevel(player);
                        plugin.getServer().getScheduler().runTask(plugin, player::closeInventory);
                        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.85F);
                    });
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
                            player.sendMessage(BoldColor.RED.getColor() + "Oops! " + ChatColor.GRAY + "You tried to report a player without selecting any report options!");
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

            if (event.getClickedInventory().getName().contains("Options")) {
                Player target = plugin.getServer().getPlayerExact(event.getClickedInventory().getName().substring(12));
                if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    ReportGUI reportGUI = new ReportGUI(plugin);
                    if (target == player) {
                        player.sendMessage(ChatColor.RED + "You can't report yourself! Unless you have something to tell us.... *gives suspicious look*");
                        player.closeInventory();
                        return;
                    }
                    ReportCommand.getReportBuilders().put(player.getUniqueId(), null);
                    ReportCommand.getReportArray().put(player.getUniqueId(), new ArrayList<>());
                    reportGUI.openInventory(player, target);
                }

                if (event.getCurrentItem().getType().equals(Material.FEATHER)) {
                    player.closeInventory();
                    BaseComponent component = new TextComponent("Click here to message " + target.getName() + "!");
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + target.getName() + " "));
                    component.setColor(ChatColor.AQUA);
                    component.setBold(true);
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "/msg " + target.getName() + " <message>").create()));
                    player.spigot().sendMessage(component);
                }

                if (event.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
                    player.closeInventory();
                    if (target == player) {
                        player.sendMessage(ChatColor.RED + "You can't team with yourself!");
                        return;
                    }
                    TeamMessages teamMessages = new TeamMessages(plugin);
                    teamMessages.sendRequestMessage(player, target);
                }

                event.setCancelled(true);
            }

            if (event.getClickedInventory().getName().equals("Settings")) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                EssenceMenu essenceMenu = new EssenceMenu(plugin);
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                if (item.getType().equals(Material.INK_SACK)) {
                    SettingsMenu settingsMenu = new SettingsMenu(plugin);

                    if (item.getItemMeta().getDisplayName().contains("Stealthy")) {
                        if (!playerData.isStealthyJoin()) {
                            playerData.setStealthyJoin(true);
                            settingsMenu.openInventory(player);
                            plugin.playSound(player, EventSound.COMMAND_CLICK);
                        } else {
                            playerData.setStealthyJoin(false);
                            plugin.playSound(player, EventSound.COMMAND_CLICK);
                            settingsMenu.openInventory(player);
                        }
                    }
                    if (item.getItemMeta().getDisplayName().contains("Messaging")) {
                        if (!playerData.isPrivateMessaging()) {
                            playerData.setPrivateMessaging(true);
                            plugin.playSound(player, EventSound.COMMAND_CLICK);
                            settingsMenu.openInventory(player);
                        } else {
                            playerData.setPrivateMessaging(false);
                            plugin.playSound(player, EventSound.COMMAND_CLICK);
                            settingsMenu.openInventory(player);
                        }
                    }
                    if (item.getItemMeta().getDisplayName().contains("Requests")) {
                        if (!playerData.isTeamRequests()) {
                            playerData.setTeamRequests(true);
                            plugin.playSound(player, EventSound.COMMAND_CLICK);
                            settingsMenu.openInventory(player);
                        } else {
                            playerData.setTeamRequests(false);
                            plugin.playSound(player, EventSound.COMMAND_CLICK);
                            settingsMenu.openInventory(player);
                        }
                    }
                }
                if (item.getType().equals(Material.BLAZE_POWDER)) {
                    if (plugin.getTotalEssenceAmount(player) == 0) {
                        plugin.playSound(player, EventSound.COMMAND_FAIL);
                    } else {
                        essenceMenu.openInventory(player);
                    }
                }

                if (item.getType().equals(Material.MAGMA_CREAM)) {
                    TrailMenu trailMenu = new TrailMenu(plugin);
                    trailMenu.openInventory(player);
                }

            }

            if (event.getClickedInventory().getName().equals("Battle Essences")) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                Essence essence = new Essence(plugin);
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);

                if (item.getType().equals(Material.BLAZE_POWDER)) {
                    if (plugin.getConfig().getBoolean("essenceActive")) {
                        if (plugin.getConfig().get("essenceOwner").equals(player.getName())) {
                            player.closeInventory();
                            player.sendMessage(ChatColor.RED + "You already have a Battle Essence active!");
                            plugin.playSound(player, EventSound.COMMAND_FAIL);
                        } else {
                            player.closeInventory();
                            player.sendMessage(ChatColor.RED + "Someone already has a Battle Essence active!");
                            plugin.playSound(player, EventSound.COMMAND_FAIL);
                        }
                    } else {
                        player.closeInventory();
                        String name = item.getItemMeta().getDisplayName();
                        essence.activateEssence(player, Essence.typeFromName(name.substring(2, item.getItemMeta().getDisplayName().length())));
                        player.sendMessage(ChatColor.YELLOW + "You activated your " + name + ChatColor.YELLOW + " Battle Essence! Enjoy!");
                        player.sendMessage(BoldColor.GREEN.getColor() + "Thanks again for the purchase!");
                        DonationMessages donationMessages = new DonationMessages(plugin);
                        donationMessages.sendEssenceActivationMessage(Essence.typeFromName(name.substring(2, item.getItemMeta().getDisplayName().length())), player);
                        for (Location location : plugin.getFireworkBlocks()) {
                            FireworkUtils.spawnRandomFirework(location);
                        }
                    }
                }
            }

            if (event.getClickedInventory().getName().equals("Particle Packs")) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);

                Trail.Type trail = Trail.typeFromName(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()));
                if (trail == null) {
                    return;
                }
                playerData.setTrail(trail);
                player.closeInventory();
                player.sendMessage(ChatColor.GRAY + (trail.getRarity().equals(Rarity.COMMON)
                        ? "You removed your active particle pack" : "You set your particle pack to " + trail.getRarity().getColor() + trail.getName()));
                plugin.playSound(player, EventSound.COMMAND_SUCCESS);
            }

        } else {
            event.setCancelled(false);
        }
    }
}
