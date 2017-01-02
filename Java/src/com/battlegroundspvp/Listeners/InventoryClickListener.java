package com.battlegroundspvp.Listeners;
/* Created by GamerBah on 8/12/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Donations.DonationMessages;
import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Administration.Punishments.Commands.BanCommand;
import com.battlegroundspvp.Administration.Punishments.Commands.KickCommand;
import com.battlegroundspvp.Administration.Punishments.Commands.MuteCommand;
import com.battlegroundspvp.Administration.Punishments.Commands.TempBanCommand;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Commands.ReportCommand;
import com.battlegroundspvp.Etc.Achievements.Achievement;
import com.battlegroundspvp.Etc.Achievements.AchievementMenu;
import com.battlegroundspvp.Etc.Menus.CosmeticrateMenu;
import com.battlegroundspvp.Etc.Menus.Cosmetics.GoreMenu;
import com.battlegroundspvp.Etc.Menus.Cosmetics.TrailMenu;
import com.battlegroundspvp.Etc.Menus.Cosmetics.WarcryMenu;
import com.battlegroundspvp.Etc.Menus.KSlotsMenu;
import com.battlegroundspvp.Etc.Menus.Player.EssenceMenu;
import com.battlegroundspvp.Etc.Menus.Player.ProfileMenu;
import com.battlegroundspvp.Etc.Menus.Player.SettingsMenu;
import com.battlegroundspvp.Etc.Menus.Punishment.PunishMenu;
import com.battlegroundspvp.Etc.Menus.Punishment.ReportMenu;
import com.battlegroundspvp.Etc.Menus.Punishment.WarnMenu;
import com.battlegroundspvp.Utils.Enums.Cosmetic;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Enums.Rarity;
import com.battlegroundspvp.Utils.FireworkUtils;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.KitManager;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Teams.TeamMessages;
import com.battlegroundspvp.Utils.Teams.TeamUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryClickListener implements Listener {

    private Battlegrounds plugin;

    public InventoryClickListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    private static int selectedInInventory(Inventory inventory) {
        int found = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getDurability() == 10)
                found += item.getAmount();
        }
        return found;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        final Player player = (Player) event.getWhoClicked();

        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (event.getSlotType() == null || event.getCurrentItem() == null
                    || event.getCurrentItem().getType() == null || event.getCurrentItem().getItemMeta() == null) {
                return;
            }

            if (KitManager.isPlayerInKit(player)) {
                event.setCancelled(true);
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

            if (inventory.getName().contains("Reporting:")) {
                ItemStack item = event.getCurrentItem();
                ReportMenu reportMenu = new ReportMenu(plugin);
                String targetName = inventory.getName().replace("Reporting: ", "");
                Player target = plugin.getServer().getPlayerExact(targetName);
                int slot = event.getSlot();
                String message = ReportCommand.getReportBuilders().get(player.getUniqueId());
                ArrayList<String> rm = ReportCommand.getReportArray().get(player.getUniqueId());

                if (item.getType().equals(Material.BOOK)) {
                    Battlegrounds.playSound(player, EventSound.CLICK);
                    rm.add(item.getItemMeta().getDisplayName().replace(ChatColor.RED + "", ""));
                    int size = ReportCommand.getReportArray().get(player.getUniqueId()).size();
                    for (int i = 0; i <= size - 1; i++) {
                        if (message != null) {
                            message = message + ", " + rm.get(i);
                        } else {
                            message = rm.get(i);
                        }
                    }
                    reportMenu.setWool(inventory, target, message);
                    inventory.setItem(slot, reportMenu.setSelected(item));
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
                    reportMenu.setWool(inventory, target, message);
                    inventory.setItem(slot, reportMenu.setUnSelected(item));
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
                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                            player.sendMessage(BoldColor.RED.getColor() + "Oops! " + ChatColor.GRAY + "You tried to report a player without selecting any report options!");
                        } else {
                            reportMenu.report(player, target, message);
                            player.closeInventory();
                        }
                    }
                    if (item.getDurability() == 14) {
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Report cancelled.");
                        Battlegrounds.playSound(player, EventSound.CLICK);
                    }
                }
                event.setCancelled(true);
            }

            if (inventory.getName().contains("Options")) {
                Player target = plugin.getServer().getPlayerExact(inventory.getName().substring(12));
                if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                    ReportMenu reportMenu = new ReportMenu(plugin);
                    if (target == player) {
                        player.sendMessage(ChatColor.RED + "You can't report yourself! Unless you have something to tell us.... *gives suspicious look*");
                        player.closeInventory();
                        return;
                    }
                    ReportCommand.getReportBuilders().put(player.getUniqueId(), null);
                    ReportCommand.getReportArray().put(player.getUniqueId(), new ArrayList<>());
                    reportMenu.openInventory(player, target);
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
                    TeamUtils.createPendingRequest(target, player);
                }

                event.setCancelled(true);
            }

            if (inventory.getName().equals("Settings")) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                SettingsMenu settingsMenu = new SettingsMenu(plugin);

                if (item.getItemMeta().getDisplayName().contains("Stealthy")) {
                    if (!playerData.isStealthyJoin()) {
                        playerData.setStealthyJoin(true);
                        settingsMenu.openInventory(player);
                        Battlegrounds.playSound(player, EventSound.CLICK);
                    } else {
                        playerData.setStealthyJoin(false);
                        Battlegrounds.playSound(player, EventSound.CLICK);
                        settingsMenu.openInventory(player);
                    }
                }
                if (item.getItemMeta().getDisplayName().contains("Command")) {
                    if (!Battlegrounds.getCmdspies().contains(player.getUniqueId())) {
                        Battlegrounds.getCmdspies().add(player.getUniqueId());
                        settingsMenu.openInventory(player);
                        Battlegrounds.playSound(player, EventSound.CLICK);
                    } else {
                        Battlegrounds.getCmdspies().remove(player.getUniqueId());
                        Battlegrounds.playSound(player, EventSound.CLICK);
                        settingsMenu.openInventory(player);
                    }
                }
                if (item.getItemMeta().getDisplayName().contains("Messaging")) {
                    if (!playerData.isPrivateMessaging()) {
                        playerData.setPrivateMessaging(true);
                        Battlegrounds.playSound(player, EventSound.CLICK);
                        settingsMenu.openInventory(player);
                    } else {
                        playerData.setPrivateMessaging(false);
                        Battlegrounds.playSound(player, EventSound.CLICK);
                        settingsMenu.openInventory(player);
                    }
                }
                if (item.getItemMeta().getDisplayName().contains("Requests")) {
                    if (!playerData.isTeamRequests()) {
                        playerData.setTeamRequests(true);
                        Battlegrounds.playSound(player, EventSound.CLICK);
                        settingsMenu.openInventory(player);
                    } else {
                        playerData.setTeamRequests(false);
                        Battlegrounds.playSound(player, EventSound.CLICK);
                        settingsMenu.openInventory(player);
                    }
                }
                if (item.getItemMeta().getDisplayName().contains("Particle")) {
                        /*if (playerData.getParticleQuality().equals(ParticleQuality.LOW)) {
                            playerData.setParticleQuality(ParticleQuality.MEDIUM);
                            Battlegrounds.playSound(player, EventSound.CLICK);
                            settingsMenu.openInventory(player);
                        } else if (playerData.getParticleQuality().equals(ParticleQuality.MEDIUM)) {
                            playerData.setParticleQuality(ParticleQuality.HIGH);
                            Battlegrounds.playSound(player, EventSound.CLICK);
                            settingsMenu.openInventory(player);
                        } else {
                            playerData.setParticleQuality(ParticleQuality.LOW);
                            Battlegrounds.playSound(player, EventSound.CLICK);
                            settingsMenu.openInventory(player);
                        }*/
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }
            }

            if (inventory.getName().equals("Profile")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                if (item.getType().equals(Material.BLAZE_POWDER)) {
                    EssenceMenu essenceMenu = new EssenceMenu(plugin);
                    if (plugin.getTotalEssenceAmount(player) == 0) {
                        Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    } else {
                        essenceMenu.openInventory(player);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                }

                if (item.getType().equals(Material.MAGMA_CREAM)) {
                    TrailMenu trailMenu = new TrailMenu(plugin);
                    trailMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }

                if (item.getType().equals(Material.EMERALD)) {
                    AchievementMenu achievementMenu = new AchievementMenu(plugin);
                    achievementMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }

                if (item.getType().equals(Material.DIAMOND)) {
                    // TODO: Open Challenge Menu
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }

                if (item.getType().equals(Material.GLOWSTONE_DUST)) {
                    GoreMenu goreMenu = new GoreMenu(plugin);
                    goreMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }

                if (item.getType().equals(Material.DIAMOND_SWORD)) {
                    WarcryMenu warcryMenu = new WarcryMenu(plugin);
                    warcryMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
            }

            if (inventory.getName().equals("Achievement Selection")) {
                AchievementMenu achievementMenu = new AchievementMenu(plugin);
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                if (item.getType().equals(Material.GOLD_SWORD)) {
                    achievementMenu.openCombatAchievements(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.DIAMOND)) {
                    // TODO: Open Challenge Achievement Menu
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }
                if (item.getType().equals(Material.BUCKET)) {
                    achievementMenu.openCollectionAchievements(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.SKULL_ITEM)) {
                    // TODO: Open Recruitment Achievement Menu
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }
                if (item.getType().equals(Material.NAME_TAG)) {
                    // TODO: Open ??? Achievement Menu
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }
                if (item.getType().equals(Material.DIAMOND_BOOTS)) {
                    // TODO: Open Miscellaneous Achievement Menu
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }
                if (item.getType().equals(Material.BLAZE_POWDER)) {
                    // TODO: Open Donation Achievement Menu
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }
                if (item.getType().equals(Material.END_CRYSTAL)) {
                    PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                    ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
                    playerData.setTitle("TRAIL_NONE");
                    scoreboardListener.reloadScoreboardTeams(player, player.getScoreboard());
                    Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "Still being worked on!");
                    player.sendMessage(ChatColor.GRAY + "You removed your Mastery Title");
                }
                if (item.getType().equals(Material.ARROW)) {
                    ProfileMenu profileMenu = new ProfileMenu(plugin);
                    profileMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                }
            }

            if (inventory.getName().contains(" Achievements")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                if (item.getType().equals(Material.COAL_BLOCK)) {
                    player.sendMessage(ChatColor.RED + "You haven't unlocked this Mastery Title yet!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return;
                }
                if (item.getType().equals(Material.EMERALD_BLOCK)) {
                    Achievement.Type achievement = Achievement.getTypeFromName(item.getItemMeta().getDisplayName().substring(4, item.getItemMeta().getDisplayName().length()));
                    PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                    if (achievement != null) {
                        if (playerData.hasRank(Rank.WARRIOR)) {
                            playerData.setTitle(achievement.toString());
                            ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
                            scoreboardListener.reloadScoreboardTeams(player, player.getScoreboard());
                            player.closeInventory();
                            Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                            player.sendMessage(ChatColor.GRAY + "Mastery Title set to " + BoldColor.GOLD.getColor() + "[" + achievement.getTitle() + "]");
                        }
                    }
                }
                if (item.getType().equals(Material.ARROW)) {
                    AchievementMenu achievementMenu = new AchievementMenu(plugin);
                    achievementMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                }
            }

            if (inventory.getName().equals("Battle Essences")) {
                Essence essence = new Essence(plugin);
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);

                if (item.getType().equals(Material.BLAZE_POWDER)) {
                    if (plugin.getConfig().getBoolean("essenceActive")) {
                        if (plugin.getConfig().get("essenceOwner").equals(player.getName())) {
                            player.closeInventory();
                            player.sendMessage(ChatColor.RED + "You already have a Battle Essence active!");
                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                        } else {
                            player.closeInventory();
                            player.sendMessage(ChatColor.RED + "Someone already has a Battle Essence active!");
                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
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

                if (item.getType().equals(Material.ARROW)) {
                    ProfileMenu profileMenu = new ProfileMenu(plugin);
                    profileMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                }
            }

            if (inventory.getName().equals("Particle Packs")) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);

                if (playerData.getTrail().getItem() == item) {
                    return;
                }

                if (item.getType().equals(Material.ARROW)) {
                    ProfileMenu profileMenu = new ProfileMenu(plugin);
                    profileMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                } else {
                    Cosmetic.Item trail = Cosmetic.typeFromName(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()), Cosmetic.PARTICLE_PACK);
                    if (trail == null) {
                        trail = Cosmetic.typeFromName(item.getItemMeta().getDisplayName().substring(4, item.getItemMeta().getDisplayName().length()), Cosmetic.PARTICLE_PACK);
                        if (trail == null) {
                            return;
                        }
                    }
                    playerData.setTrail(trail);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GRAY + (trail.getRarity().equals(Rarity.COMMON)
                            ? "You removed your active Particle Pack" : "You set your Particle Pack to " + trail.getRarity().getColor() + trail.getName()));
                    Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                }
            }

            if (inventory.getName().equals("Warcries")) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);

                if (playerData.getWarcry().getItem() == item) {
                    return;
                }

                if (item.getType().equals(Material.ARROW)) {
                    ProfileMenu profileMenu = new ProfileMenu(plugin);
                    profileMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                } else {
                    Cosmetic.Item warcry = Cosmetic.typeFromName(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()), Cosmetic.KILL_SOUND);
                    if (warcry == null) {
                        warcry = Cosmetic.typeFromName(item.getItemMeta().getDisplayName().substring(4, item.getItemMeta().getDisplayName().length()), Cosmetic.KILL_SOUND);
                        if (warcry == null) {
                            return;
                        }
                    }
                    playerData.setWarcry(warcry);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GRAY + (warcry.getRarity().equals(Rarity.COMMON)
                            ? "You removed your active Warcry" : "You set your Warcry to " + warcry.getRarity().getColor() + warcry.getName()));
                    Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                }
            }

            if (inventory.getName().equals("Gores")) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);

                if (playerData.getGore().getItem() == item) {
                    return;
                }

                if (item.getType().equals(Material.ARROW)) {
                    ProfileMenu profileMenu = new ProfileMenu(plugin);
                    profileMenu.openInventory(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                } else {
                    Cosmetic.Item gore = Cosmetic.typeFromName(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()), Cosmetic.KILL_EFFECT);
                    if (gore == null) {
                        gore = Cosmetic.typeFromName(item.getItemMeta().getDisplayName().substring(4, item.getItemMeta().getDisplayName().length()), Cosmetic.KILL_EFFECT);
                        if (gore == null) {
                            return;
                        }
                    }
                    playerData.setGore(gore);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GRAY + (gore.getRarity().equals(Rarity.COMMON)
                            ? "You removed your active Gore" : "You set your Gore to " + gore.getRarity().getColor() + gore.getName()));
                    Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                }
            }

            if (inventory.getName().contains("Punish Menu")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                PunishMenu punishMenu = new PunishMenu(plugin);
                if (item.getType().equals(Material.SKULL_ITEM)) {
                    String targetName = item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length());
                    PlayerData targetData = plugin.getPlayerData(targetName);
                    if (targetData == null) {
                        return;
                    }
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
                    punishMenu.openInventory(player, target);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.APPLE)) {
                    if (item.getItemMeta().getDisplayName().contains("A-Z")) {
                        punishMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_AZ, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains("Z-A")) {
                        punishMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_ZA, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                }
                if (item.getType().equals(Material.EXP_BOTTLE)) {
                    if (item.getItemMeta().getDisplayName().contains("High-Low")) {
                        punishMenu.openPlayersMenu(player, PunishMenu.SortType.RANK_HIGH_LOW, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains("Low-High")) {
                        punishMenu.openPlayersMenu(player, PunishMenu.SortType.RANK_LOW_HIGH, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                }
                if (item.getType().equals(Material.GOLDEN_CARROT)) {
                    punishMenu.openPlayersMenu(player, PunishMenu.SortType.ONLINE_ONLY, null, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                }
                if (item.getType().equals(Material.SIGN)) {
                    punishMenu.openSearch(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
            }

            if (inventory.getName().contains("Warn Menu")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                WarnMenu warnMenu = new WarnMenu(plugin);
                if (item.getType().equals(Material.SKULL_ITEM)) {
                    String targetName = item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length());
                    PlayerData targetData = plugin.getPlayerData(targetName);
                    if (targetData == null) {
                        return;
                    }
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
                    warnMenu.openInventory(player, target, null);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.APPLE)) {
                    if (item.getItemMeta().getDisplayName().contains("A-Z")) {
                        warnMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_AZ, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains("Z-A")) {
                        warnMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_ZA, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                }
                if (item.getType().equals(Material.EXP_BOTTLE)) {
                    if (item.getItemMeta().getDisplayName().contains("High-Low")) {
                        warnMenu.openPlayersMenu(player, PunishMenu.SortType.RANK_HIGH_LOW, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains("Low-High")) {
                        warnMenu.openPlayersMenu(player, PunishMenu.SortType.RANK_LOW_HIGH, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                    }
                }
                if (item.getType().equals(Material.GOLDEN_CARROT)) {
                    warnMenu.openPlayersMenu(player, PunishMenu.SortType.ONLINE_ONLY, null, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                }
                if (item.getType().equals(Material.SIGN)) {
                    warnMenu.openSearch(player);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
            }

            if (inventory.getName().contains("Punishing:")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                PunishMenu punishMenu = new PunishMenu(plugin);
                String targetName = inventory.getName().replace("Punishing: ", "");
                PlayerData targetData = plugin.getPlayerData(targetName);
                if (targetData == null) {
                    return;
                }
                OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
                if (item.getType().equals(Material.BOOK)) {
                    punishMenu.openMuteMenu(player, target, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.BARRIER)) {
                    punishMenu.openBanMenu(player, target, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.SULPHUR)) {
                    punishMenu.openTempBanMenu(player, target, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.GOLD_BOOTS)) {
                    punishMenu.openKickMenu(player, target, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.ARROW)) {
                    punishMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_AZ, null, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                }
            }

            if (inventory.getName().contains(" History")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                PunishMenu punishMenu = new PunishMenu(plugin);
                int page = 0;
                if (inventory.getName().contains("(")) {
                    page = Integer.parseInt(inventory.getName().substring(inventory.getName().length() - 2, inventory.getName().length() - 1));
                }
                String[] split = inventory.getName().split("'");
                PlayerData targetData = plugin.getPlayerData(split[0]);
                OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetData.getUuid());
                if (item.getType().equals(Material.ARROW)) {
                    if (item.getItemMeta().getDisplayName().contains("Next")) {
                        if (inventory.getName().contains(" Ban")) {
                            punishMenu.openBanMenu(player, target, (page + 1));
                        }
                        if (inventory.getName().contains(" Mute")) {
                            punishMenu.openMuteMenu(player, target, (page + 1));
                        }
                        if (inventory.getName().contains(" Temp-Ban")) {
                            punishMenu.openTempBanMenu(player, target, (page + 1));
                        }
                        if (inventory.getName().contains(" Kick")) {
                            punishMenu.openKickMenu(player, target, (page + 1));
                        }
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains("Back")) {
                        if (inventory.getName().contains(" Ban")) {
                            if (page == 0) {
                                punishMenu.openInventory(player, target);
                            } else {
                                punishMenu.openBanMenu(player, target, (page - 2));
                            }
                        }
                        if (inventory.getName().contains(" Mute")) {
                            if (page == 0) {
                                punishMenu.openInventory(player, target);
                            } else {
                                punishMenu.openMuteMenu(player, target, (page - 2));
                            }
                        }
                        if (inventory.getName().contains(" Temp-Ban")) {
                            if (page == 0) {
                                punishMenu.openInventory(player, target);
                            } else {
                                punishMenu.openTempBanMenu(player, target, (page - 2));
                            }
                        }
                        if (inventory.getName().contains(" Kick")) {
                            if (page == 0) {
                                punishMenu.openInventory(player, target);
                            } else {
                                punishMenu.openKickMenu(player, target, (page - 2));
                            }
                        }
                        Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                    }
                }
                if (item.getType().equals(Material.BOOK_AND_QUILL)) {
                    if (item.getItemMeta().getDisplayName().contains("Mute")) {
                        punishMenu.openPunishMenu(player, target, Punishment.Type.MUTE, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains(" Ban")) {
                        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                        if (!playerData.hasRank(Rank.ADMIN)) {
                            player.sendMessage(ChatColor.RED + "You must be an Admin or higher to issue a ban!");
                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                            return;
                        }
                        punishMenu.openPunishMenu(player, target, Punishment.Type.BAN, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains("Temp-Ban")) {
                        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                        if (!playerData.hasRank(Rank.MODERATOR)) {
                            player.sendMessage(ChatColor.RED + "You must be a Moderator or higher to issue a temporary ban!");
                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                            return;
                        }
                        punishMenu.openPunishMenu(player, target, Punishment.Type.TEMP_BAN, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                    if (item.getItemMeta().getDisplayName().contains("Kick")) {
                        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                        if (!playerData.hasRank(Rank.MODERATOR)) {
                            player.sendMessage(ChatColor.RED + "You must be a Moderator or higher to kick players!");
                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                            return;
                        }
                        punishMenu.openPunishMenu(player, target, Punishment.Type.KICK, null, 0);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                }
                if (item.getType().equals(Material.MAP)) {
                    if (item.getItemMeta().getLore().contains(ChatColor.LIGHT_PURPLE + "Click to Pardon!")) {
                        if (inventory.getName().contains("Mute")) {
                            String playerName = inventory.getName().substring(0, inventory.getName().length() - 15);
                            player.performCommand("unmute " + playerName);
                            player.closeInventory();
                            return;
                        }
                        if (inventory.getName().contains("Temp-Ban")) {
                            String playerName = inventory.getName().substring(0, inventory.getName().length() - 19);
                            player.performCommand("unban " + playerName);
                            player.closeInventory();
                            return;
                        }
                        if (inventory.getName().contains(" Ban")) {
                            String playerName = inventory.getName().substring(0, inventory.getName().length() - 14);
                            player.performCommand("unban " + playerName);
                            player.closeInventory();
                            return;
                        }
                    }
                }
            }

            if (inventory.getName().contains("Punishment Creation:")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                PunishMenu punishMenu = new PunishMenu(plugin);
                ItemStack wool = inventory.getItem(32);
                Punishment.Type type = Punishment.getTypeFromName(inventory.getName().replace("Punishment Creation: ", ""));
                if (item.getType().equals(Material.BOOK)) {
                    Punishment.Reason reason = Punishment.getReasonFromName(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()));
                    PlayerData playerData = plugin.getPlayerData(wool.getItemMeta().getLore().get(0).substring(15, wool.getItemMeta().getLore().get(0).length()));
                    Player targetOnline = plugin.getServer().getPlayer(playerData.getUuid());
                    if (reason != null) {
                        if (targetOnline == null) {
                            OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerData.getUuid());
                            punishMenu.openPunishMenu(player, target, type, reason, reason.getLength());
                        } else {
                            punishMenu.openPunishMenu(player, targetOnline, type, reason, reason.getLength());
                        }
                    }
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    HashMap<Punishment.Reason, Integer> create = new HashMap<>();
                    create.put(reason, reason.getLength());
                    Battlegrounds.punishmentCreation.put(player, create);
                }
                if (item.getType().equals(Material.WATCH)) {
                    if (item.getItemMeta().getDisplayName().contains("(Select a reason first)")) {
                        return;
                    } else {
                        if (Battlegrounds.punishmentCreation.containsKey(player)) {
                            Punishment.Reason reason = null;
                            for (Punishment.Reason reasons : Punishment.Reason.values()) {
                                if (Battlegrounds.punishmentCreation.get(player).containsKey(reasons)) {
                                    reason = reasons;
                                }
                            }
                            if (reason != null) {
                                int time = Battlegrounds.punishmentCreation.get(player).get(reason);
                                if (event.getClick().isLeftClick()) {
                                    if (event.getClick().isShiftClick()) {
                                        if (time - 900 <= 0) {
                                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                                            return;
                                        } else {
                                            time = time - 900;
                                        }
                                    } else {
                                        if (time - 300 <= 0) {
                                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                                            return;
                                        } else {
                                            time = time - 300;
                                        }
                                    }
                                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                                } else if (event.isRightClick()) {
                                    if (event.getClick().isShiftClick()) {
                                        if (time + 900 >= 604800) {
                                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                                            return;
                                        } else {
                                            time = time + 900;
                                        }
                                    } else {
                                        if (time + 300 >= 604800) {
                                            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                                            return;
                                        } else {
                                            time = time + 300;
                                        }
                                    }
                                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                                }
                                PlayerData playerData = plugin.getPlayerData(wool.getItemMeta().getLore().get(0).substring(15, wool.getItemMeta().getLore().get(0).length()));
                                Player targetOnline = plugin.getServer().getPlayer(playerData.getUuid());
                                if (targetOnline == null) {
                                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerData.getUuid());
                                    punishMenu.openPunishMenu(player, target, type, reason, time);
                                } else {
                                    punishMenu.openPunishMenu(player, targetOnline, type, reason, time);
                                }
                                HashMap<Punishment.Reason, Integer> create = new HashMap<>();
                                create.put(reason, time);
                                Battlegrounds.punishmentCreation.put(player, create);
                            }
                        }
                    }
                }
                if (item.getType().equals(Material.ARROW)) {
                    PlayerData playerData = plugin.getPlayerData(wool.getItemMeta().getLore().get(0).substring(15, wool.getItemMeta().getLore().get(0).length()));
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerData.getUuid());
                    if (inventory.getName().contains(" Mute")) {
                        punishMenu.openMuteMenu(player, target, 0);
                    }
                    if (inventory.getName().contains(" Ban")) {
                        punishMenu.openBanMenu(player, target, 0);
                    }
                    if (inventory.getName().contains(" Temp-Ban")) {
                        punishMenu.openTempBanMenu(player, target, 0);
                    }
                    if (inventory.getName().contains(" Kick")) {
                        punishMenu.openKickMenu(player, target, 0);
                    }
                    if (Battlegrounds.punishmentCreation.containsKey(player)) {
                        Battlegrounds.punishmentCreation.remove(player);
                    }
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                }
                if (item.getType().equals(Material.WOOL)) {
                    PlayerData playerData = plugin.getPlayerData(wool.getItemMeta().getLore().get(0).substring(15, wool.getItemMeta().getLore().get(0).length()));
                    OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerData.getUuid());
                    if (!Battlegrounds.punishmentCreation.containsKey(player)) {
                        Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                        return;
                    }
                    if (inventory.getName().contains(" Ban")) {
                        BanCommand.banPlayer(target.getUniqueId(), player, Battlegrounds.punishmentCreation.get(player));
                    }
                    if (inventory.getName().contains(" Mute")) {
                        MuteCommand.mutePlayer(target.getUniqueId(), player, Battlegrounds.punishmentCreation.get(player));
                    }
                    if (inventory.getName().contains(" Temp-Ban")) {
                        TempBanCommand.tempbanPlayer(target.getUniqueId(), player, Battlegrounds.punishmentCreation.get(player));
                    }
                    if (inventory.getName().contains(" Kick")) {
                        KickCommand.kickPlayer(target.getUniqueId(), player, Battlegrounds.punishmentCreation.get(player));
                    }
                }
            }

            if (inventory.getName().contains("Warning:")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                WarnMenu warnMenu = new WarnMenu(plugin);
                ItemStack wool = inventory.getItem(32);
                if (item.getType().equals(Material.BOOK)) {
                    Punishment.Reason reason = Punishment.getReasonFromName(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()));
                    PlayerData playerData = plugin.getPlayerData(wool.getItemMeta().getLore().get(0).substring(13, wool.getItemMeta().getLore().get(0).length()));
                    Player targetOnline = plugin.getServer().getPlayer(playerData.getUuid());
                    if (targetOnline == null) {
                        OfflinePlayer target = plugin.getServer().getOfflinePlayer(playerData.getUuid());
                        warnMenu.openInventory(player, target, reason);
                    } else {
                        warnMenu.openInventory(player, targetOnline, reason);
                    }
                    Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                }
                if (item.getType().equals(Material.ARROW)) {
                    warnMenu.openPlayersMenu(player, PunishMenu.SortType.NAME_AZ, null, 0);
                    Battlegrounds.playSound(player, EventSound.INVENTORY_GO_BACK);
                }
                if (item.getType().equals(Material.WOOL)) {
                    if (item.getItemMeta().getLore().contains(ChatColor.RED + "Nothing (Select a book)")) {
                        return;
                    }
                    PlayerData playerData = plugin.getPlayerData(wool.getItemMeta().getLore().get(0).substring(13, wool.getItemMeta().getLore().get(0).length()));
                    Punishment.Reason reason = Punishment.getReasonFromName(wool.getItemMeta().getLore().get(1).substring(9, wool.getItemMeta().getLore().get(1).length()));
                    plugin.warnPlayer(player, playerData, reason);
                }
            }

            if (inventory.getName().equals("\"K-Slots\" Machine")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                if (item.getType().equals(Material.INK_SACK)) {
                    if (item.getDurability() == 8) {
                        if (event.getSlot() == 12) {
                            if (!playerData.hasRank(Rank.WARRIOR)) {
                                return;
                            }
                        }
                        if (event.getSlot() == 14) {
                            if (!playerData.hasRank(Rank.GLADIATOR)) {
                                return;
                            }
                        }
                        if (event.getSlot() == 16) {
                            if (!playerData.hasRank(Rank.CONQUEROR)) {
                                return;
                            }
                        }
                        inventory.setItem(event.getSlot(), new I(Material.INK_SACK).name(BoldColor.GREEN.getColor() + "ACTIVE")
                                .lore(ChatColor.GRAY + "This slot will be rolled").lore(" ")
                                .lore(ChatColor.YELLOW + "Click to Disable!")
                                .durability(10));
                        inventory.setItem(31, new I(Material.WOOL).name((playerData.getSouls() >= 400 * selectedInInventory(inventory)
                                ? BoldColor.GREEN.getColor() + "Click to Roll!" : BoldColor.RED.getColor() + "Need More Souls!"))
                                .lore(ChatColor.GRAY + "Cost: " + ChatColor.AQUA + 400 * selectedInInventory(inventory) + " Souls")
                                .durability(5));
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                        player.openInventory(inventory);
                    }
                    if (item.getDurability() == 10) {
                        if (event.getSlot() != 10) {
                            inventory.setItem(event.getSlot(), new I(Material.INK_SACK).name(BoldColor.RED.getColor() + "DISABLED")
                                    .lore(ChatColor.GRAY + "This slot will not be rolled").lore(" ")
                                    .lore(ChatColor.YELLOW + "Click to Activate!")
                                    .durability(8));
                            inventory.setItem(31, new I(Material.WOOL).name((playerData.getSouls() >= 400 * selectedInInventory(inventory)
                                    ? BoldColor.GREEN.getColor() + "Click to Roll!" : BoldColor.RED.getColor() + "Need More Souls!"))
                                    .lore(ChatColor.GRAY + "Cost: " + ChatColor.AQUA + 400 * selectedInInventory(inventory) + " Souls")
                                    .durability(5));
                            Battlegrounds.playSound(player, EventSound.CLICK);
                            player.openInventory(inventory);
                        }
                    }
                }

                if (item.getType().equals(Material.WOOL)) {
                    if (playerData.getSouls() < 400 * selectedInInventory(inventory)) {
                        Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    } else {
                        KSlotsMenu kSlotsMenu = new KSlotsMenu(plugin);
                        kSlotsMenu.beginSlots(player, selectedInInventory(inventory));
                    }
                }
                if (item.getType().equals(Material.ARROW) && item.getItemMeta().getDisplayName().equals(BoldColor.YELLOW.getColor() + "Reset Slots")) {
                    if (playerData.getSouls() < 400 * selectedInInventory(inventory)) {
                        Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "You don't have enough Souls to use the machine again!");
                    } else {
                        KSlotsMenu kSlotsMenu = new KSlotsMenu(plugin);
                        kSlotsMenu.openInventory(player);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                }
            }

            if (inventory.getName().equals("Cosmeticrates")) {
                ItemStack item = event.getCurrentItem();
                event.setCancelled(true);
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                if (item.getType().equals(Material.CHEST)) {
                    if (playerData.getCoins() < 500) {
                        Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    } else {
                        CosmeticrateMenu cosmeticrateMenu = new CosmeticrateMenu(plugin);
                        cosmeticrateMenu.beginCrates(player);
                    }
                }

                if (item.getType().equals(Material.ARROW) && item.getItemMeta().getDisplayName().equals(BoldColor.YELLOW.getColor() + "Reset Slots")) {
                    if (playerData.getSouls() < 500) {
                        Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "You don't have enough Battle Coins to open another Cosmeticrate");
                    } else {
                        CosmeticrateMenu cosmeticrateMenu = new CosmeticrateMenu(plugin);
                        cosmeticrateMenu.openInventory(player);
                        Battlegrounds.playSound(player, EventSound.INVENTORY_OPEN_SUBMENU);
                    }
                }
            }

        } else

        {
            event.setCancelled(false);
        }
    }


}
