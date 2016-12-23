package com.battlegroundspvp.PlayerEvents;
/* Created by GamerBah on 8/7/2016 */


import com.battlegroundspvp.Administration.Commands.FreezeCommand;
import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Donations.DonationMessages;
import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Runnables.AutoUpdate;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Listeners.ScoreboardListener;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.KDRatio;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Time;
import com.connorlinfoot.titleapi.TitleAPI;
import de.Herbystar.TTA.TTA_Methods;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PlayerJoin implements Listener {

    private Battlegrounds plugin;

    public PlayerJoin(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (plugin.getPlayerData(event.getUniqueId()) == null) {
            plugin.createPlayerData(event.getUniqueId(), event.getName());
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                plugin.getPlayerData(event.getUniqueId()).setDailyRewardLast(LocalDateTime.now());
                plugin.getPlayerData(event.getUniqueId()).setLastKilledBy(event.getUniqueId().toString());
            }, 2L);
        }

        ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(event.getUniqueId());
        if (punishments != null) {
            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (punishment.getType().equals(Punishment.Type.BAN)) {
                    if (!punishment.isPardoned()) {
                        PlayerData playerData = plugin.getPlayerData(punishment.getEnforcer());
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You were banned by " + ChatColor.GOLD + playerData.getName()
                                + ChatColor.RED + " for " + ChatColor.GOLD + punishment.getReason().getName() + "\n" + ChatColor.AQUA
                                + punishment.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a '(CST)'")) + "\n\n" + ChatColor.YELLOW
                                + punishment.getReason().getMessage() + "\n\n" + ChatColor.GRAY + "Appeal your ban on the forums: battlegroundspvp.com/forums");
                        return;
                    }
                }
                if (punishment.getType().equals(Punishment.Type.TEMP_BAN)) {
                    if (!punishment.isPardoned()) {
                        PlayerData playerData = plugin.getPlayerData(punishment.getEnforcer());
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You were temporarily banned by " + ChatColor.GOLD + playerData.getName()
                                + ChatColor.RED + " for " + ChatColor.GOLD + punishment.getReason().getName() + "\n" + ChatColor.AQUA
                                + punishment.getDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a '(CST)'")) + "\n\n"
                                + ChatColor.GRAY + "Time Remaining in Ban: " + ChatColor.RED + Time.toString(Time.punishmentTimeRemaining(punishment.getExpiration()), true) + "\n" + ChatColor.YELLOW
                                + punishment.getReason().getMessage() + "\n\n" + ChatColor.GRAY + "You can appeal your ban on the forums: battlegroundspvp.com/forums");
                        return;
                    }
                }
            }
        }

        if (plugin.getConfig().getBoolean("developmentMode")) {
            if (plugin.getPlayerData(event.getUniqueId()) == null || !plugin.getPlayerData(event.getUniqueId()).hasRank(Rank.HELPER)) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "You were not able to join the server because it is in\n" + BoldColor.GOLD.getColor()
                        + "MAINTENANCE MODE\n\n" + ChatColor.AQUA + "This means that we are fixing bugs, or found another issue we needed to take care of\n\n"
                        + ChatColor.GRAY + "We put the server into Maintenance Mode in order to reduce the risk of\n§7corrupting player data, etc. The server should be open shortly!");
                return;
            }
        }

        if (AutoUpdate.updating) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, BoldColor.YELLOW.getColor() + "SERVER IS UPDATING!\n"
                    + ChatColor.RED + "To prevent your data from being corrupted, you didn't connect\n\n" + ChatColor.GRAY + "You should be able to join in a few seconds! Hang in there!");
            return;
        }

        PlayerData playerData = plugin.getPlayerData(event.getUniqueId());
        if (playerData != null) {
            if (LocalDateTime.now().minusSeconds(86400).isAfter(playerData.getDailyRewardLast())) {
                playerData.setDailyReward(false);
            }
        }
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        KDRatio kdRatio = new KDRatio(plugin);
        scoreboardListener.getRanks().put(plugin.getPlayerData(event.getUniqueId()).getUuid(), plugin.getPlayerData(event.getUniqueId()).getRank().getColor()
                + "" + ChatColor.BOLD + plugin.getPlayerData(event.getUniqueId()).getRank().getName().toUpperCase());
        scoreboardListener.getKills().put(plugin.getPlayerData(event.getUniqueId()).getUuid(), plugin.getPlayerData(event.getUniqueId()).getKills());
        scoreboardListener.getDeaths().put(plugin.getPlayerData(event.getUniqueId()).getUuid(), plugin.getPlayerData(event.getUniqueId()).getDeaths());
        scoreboardListener.getKds().put(plugin.getPlayerData(event.getUniqueId()).getUuid(), ChatColor.GRAY + "" + kdRatio.getRatio(plugin.getPlayerData(event.getUniqueId())));
        scoreboardListener.getSouls().put(plugin.getPlayerData(event.getUniqueId()).getUuid(), plugin.getPlayerData(event.getUniqueId()).getSouls());
        scoreboardListener.getCoins().put(plugin.getPlayerData(event.getUniqueId()).getUuid(), plugin.getPlayerData(event.getUniqueId()).getCoins());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!plugin.getOne50Essence().containsKey(player.getUniqueId()))
            plugin.getOne50Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_50_PERCENT));
        if (!plugin.getOne100Essence().containsKey(player.getUniqueId()))
            plugin.getOne100Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_100_PERCENT));
        if (!plugin.getOne150Essence().containsKey(player.getUniqueId()))
            plugin.getOne150Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_150_PERCENT));
        if (!plugin.getThree50Essence().containsKey(player.getUniqueId()))
            plugin.getThree50Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_50_PERCENT));
        if (!plugin.getThree100Essence().containsKey(player.getUniqueId()))
            plugin.getThree100Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_100_PERCENT));
        if (!plugin.getThree150Essence().containsKey(player.getUniqueId()))
            plugin.getThree150Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_150_PERCENT));
        if (!plugin.getSix50Essence().containsKey(player.getUniqueId()))
            plugin.getSix50Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_50_PERCENT));
        if (!plugin.getSix100Essence().containsKey(player.getUniqueId()))
            plugin.getSix100Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_100_PERCENT));
        if (!plugin.getSix150Essence().containsKey(player.getUniqueId()))
            plugin.getSix150Essence().put(player.getUniqueId(), Battlegrounds.getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_150_PERCENT));

        if (!player.hasPlayedBefore()) {
            event.setJoinMessage(BoldColor.GOLD.getColor() + "New! " + BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                    + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName());
            TitleAPI.sendTitle(player, 5, 60, 20, BoldColor.YELLOW.getColor() + "Welcome to" + BoldColor.GOLD.getColor() + "Battlegrounds!",
                    ChatColor.AQUA + "Right-Click the Nether Star to get started!");
        } else {
            if (playerData.isStealthyJoin()) {
                event.setJoinMessage(null);
                plugin.getServer().getOnlinePlayers().stream().filter(staff ->
                        plugin.getPlayerData(staff.getUniqueId()).hasRank(Rank.ADMIN)).forEach(staff ->
                        staff.sendMessage(BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                                + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " (Stealth Join)"));
            } else {
                if (!player.getName().equals(playerData.getName())) {
                    String oldName = playerData.getName();
                    playerData.setName(player.getName());
                    event.setJoinMessage(BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                            + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName() + ChatColor.GRAY + " (" + oldName + ")");
                } else {
                    event.setJoinMessage(BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                            + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName());
                }
            }
            TitleAPI.sendTitle(player, 5, 60, 15, BoldColor.GREEN.getColor() + "Welcome Back!", ChatColor.GRAY
                    + (plugin.getConfig().getBoolean("essenceActive") ? plugin.getConfig().getString("essenceOwner") + " has a " + ChatColor.AQUA + plugin.getConfig().getInt("essenceIncrease")
                    + "% Battle Essence " + ChatColor.GRAY + "active!" : ""));

            BaseComponent baseComponent = new TextComponent(BoldColor.YELLOW.getColor() + "Click here to claim your " + BoldColor.AQUA.getColor() + "Daily Reward");
            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.RED + "/dailyreward").create()));
            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dailyreward"));
            if (!playerData.isDailyReward()) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    player.spigot().sendMessage(baseComponent);
                    Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                }, 2L);
            }
        }

        if ((FreezeCommand.frozen && !playerData.hasRank(Rank.MODERATOR)) || FreezeCommand.frozenPlayers.contains(player) || FreezeCommand.reloadFreeze) {
            player.setWalkSpeed(0F);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -50, true, false));
            player.setFoodLevel(6);
            player.setSaturation(0);
        } else {
            player.setWalkSpeed(0.2F);
        }

        player.setPlayerListName((playerData.hasRank(Rank.WARRIOR) ? playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : ChatColor.GRAY) + player.getName());

        TTA_Methods.sendTablist(player, ChatColor.AQUA + "You're playing on " + BoldColor.GOLD.getColor() + "BATTLEGROUNDS",
                ChatColor.RED + "Visit our store! " + ChatColor.YELLOW + "store.battlegroundspvp.com");

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(20);
        plugin.respawn(player);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getConfig().getBoolean("essenceActive")) {
                DonationMessages donationMessages = new DonationMessages(plugin);
                if (!plugin.getConfig().getStringList("essenceThanks").contains(player.getName())) {
                    donationMessages.sendActiveEssenceMessage(player);
                }
            }
        }, 2L);
    }
}
