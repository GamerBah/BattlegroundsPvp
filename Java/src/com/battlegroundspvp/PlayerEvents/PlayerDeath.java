package com.battlegroundspvp.PlayerEvents;
/* Created by GamerBah on 8/9/2016 */


import com.battlegroundspvp.Administration.Commands.ChatCommands;
import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Etc.Achievements.Achievement;
import com.battlegroundspvp.Listeners.CombatListener;
import com.battlegroundspvp.Listeners.ScoreboardListener;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Messages.TextComponentMessages;
import com.battlegroundspvp.Utils.Packets.Particles.ParticleEffect;
import com.connorlinfoot.titleapi.TitleAPI;
import de.Herbystar.TTA.TTA_Methods;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerDeath implements Listener {

    private Battlegrounds plugin;

    public PlayerDeath(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        Location deathLoc = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        ParticleEffect.LAVA.display(0, 0.2F, 0, 1, 20, player.getLocation(), 100);
        player.setHealth(20);
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            plugin.respawn(player);
            CombatListener.getTagged().remove(player.getUniqueId());
        });
        player.getInventory().setHeldItemSlot(4);

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        scoreboardListener.updateScoreboardDeaths(player, 1);

        for (Achievement.Type achievement : Achievement.Type.values()) {
            if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Deathknell")) {
                if (playerData.getKitPvpData().getDeaths() == achievement.getRequirement()) {
                    Achievement.sendUnlockMessage(player, achievement);
                }
            }
        }

        if (killer == null) {
            if (plugin.getServer().getOnlinePlayers().size() >= 15 || ChatCommands.chatSilenced) {
                event.setDeathMessage(null);
            } else {
                if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " lost a fight with gravity");
                } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " tried to swim in lava");
                } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " forgot to come up for air");
                } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                        || player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " didn't stop, drop, and roll");
                } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " fell into the unknown");
                } else {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " died");
                }
            }
            int i = ThreadLocalRandom.current().nextInt(1, 3 + 1);

            if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Try not to fall so hard next time");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "" + ChatColor.ITALIC + "'You believed you could fly...'");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Flying is for airplanes, not people.");
                }
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Reminder: Lava will burn you...");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Yea, that orange stuff? IT BURNS!");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "That probably gave you 5th-degree burns");
                }
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Did you really think you were a fish?");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "" + ChatColor.ITALIC + "Just keep swimming, just keep swimming...");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "There's this neat thing called air. You needed it.");
                }
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                    || player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Tip: Stop, Drop, and Roll");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "One day, Minecraft will have fire extinguishers");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Perfect for roasting marshmallows!");
                }
            } else if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "How, exactly, did you get down there?");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Did you just fall into nothingness?");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Welcome... to SPACE!");
                }
            } else {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Looks like you need life insurance!");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "That could've been done differently...");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "The point of the game is to" + ChatColor.ITALIC + "stay alive");
                }
            }
            plugin.getGlobalStats().addSuicide();
            return;
        }

        PlayerData killerData = plugin.getPlayerData(killer.getUniqueId());
        scoreboardListener.updateScoreboardKills(killer, 1);
        player.getWorld().playSound(deathLoc, killerData.getWarcry().getKillSound(), killerData.getWarcry().getVolume(), killerData.getWarcry().getPitch());

        for (Achievement.Type achievement : Achievement.Type.values()) {
            if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Brutality")) {
                if (killerData.getKitPvpData().getKills() == achievement.getRequirement()) {
                    Achievement.sendUnlockMessage(player, achievement);
                }
            }
        }

        playerData.getKitPvpData().setLastKilledBy(killer);

        TextComponentMessages tcm = new TextComponentMessages(plugin);
        TextComponent killerTCM = new TextComponent(killer.getName());
        killerTCM.setColor(ChatColor.GOLD);
        killerTCM.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tcm.playerStats(killer)));
        killerTCM.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/options " + killer.getName()));

        TextComponent killedTCM = new TextComponent(player.getName());
        killedTCM.setColor(ChatColor.RED);
        killedTCM.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tcm.playerStats(player)));
        killedTCM.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/options " + player.getName()));

        TextComponent wkb = new TextComponent(" killed ");
        wkb.setColor(ChatColor.GRAY);
        wkb.setHoverEvent(null);

        BaseComponent baseComponent = new TextComponent("");
        baseComponent.addExtra(killerTCM);
        baseComponent.addExtra(wkb);
        baseComponent.addExtra(killedTCM);

        if (plugin.getServer().getOnlinePlayers().size() >= 15 || ChatCommands.chatSilenced) {
            event.setDeathMessage(null);
        } else {
            event.setDeathMessage(null);
            plugin.getServer().spigot().broadcast(baseComponent);
        }

        String health;
        if (killer.getHealth() % 2 == 0) {
            health = ChatColor.GRAY + "They had " + ChatColor.RED + (((int) killer.getHealth()) / 2) + " hearts" + ChatColor.GRAY + " left";
        } else {
            health = ChatColor.GRAY + "They had " + ChatColor.RED + (((int) killer.getHealth()) / 2) + ".5 hearts" + ChatColor.GRAY + " left";
        }

        TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "Killed by " + BoldColor.GOLD.getColor() + killer.getName(), health);

        if (killer.getName().equals(player.getName())) {
            return;
        }

        if (killer.getLocation().distance(player.getWorld().getSpawnLocation()) <= 100) {
            killer.setHealth(20);
        }

        int souls = ThreadLocalRandom.current().nextInt(4, 9 + 1);
        int coins = 0;
        if (ThreadLocalRandom.current().nextInt(1, 8 + 1) == 1) {
            coins = ThreadLocalRandom.current().nextInt(1, 4 + 1);
        }
        String eOwner = "";
        boolean essence = plugin.getConfig().getBoolean("essenceActive");
        if (essence) {
            switch (plugin.getConfig().getInt("essenceIncrease")) {
                case 50:
                    souls = (int) Math.ceil(souls * 1.5);
                    coins = (int) Math.ceil(coins * 1.5);
                case 100:
                    souls = (int) Math.ceil(souls * 2);
                    coins = (int) Math.ceil(coins * 2);
                case 150:
                    souls = (int) Math.ceil(souls * 2.5);
                    coins = (int) Math.ceil(coins * 2.5);
            }
            eOwner = plugin.getConfig().getString("essenceOwner");
        }

        if (Battlegrounds.killStreak.containsKey(killer.getUniqueId())) {
            Battlegrounds.killStreak.put(killer.getUniqueId(), Battlegrounds.killStreak.get(killer.getUniqueId()) + 1);
            int killstreak = Battlegrounds.killStreak.get(killer.getUniqueId());
            if (killstreak % 5 == 0) {
                plugin.getServer().broadcastMessage(ChatColor.GOLD + killer.getName() + ChatColor.GRAY + " is on a " + BoldColor.RED.getColor() + killstreak + " killstreak!");
                scoreboardListener.updateScoreboardSouls(killer, (souls * (killstreak / 5)));
                scoreboardListener.updateScoreboardCoins(killer, coins);

                TTA_Methods.sendActionBar(killer, ChatColor.AQUA + "[+" + souls * ((killstreak / 5) + 1) + " Souls]" + ChatColor.LIGHT_PURPLE
                        + (coins != 0 ? " [+" + coins + (coins == 1 ? " Battle Coin]" : " Battle Coins]") : "")
                        + (essence ? ChatColor.YELLOW + " [" + eOwner + "'s Essence]" : ""));
                TitleAPI.sendTitle(killer, 1, 30, 10, BoldColor.GREEN.getColor() + killstreak + " Killstreak!", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
                if (killerData.getKitPvpData().getLastKilledBy().equals(player.getUniqueId().toString())) {
                    killerData.getKitPvpData().addRevengeKill();
                    killerData.getKitPvpData().setLastKilledBy("NONE");
                    for (Achievement.Type achievement : Achievement.Type.values()) {
                        if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Vengeful")) {
                            if (killerData.getKitPvpData().getRevengeKills() == achievement.getRequirement()) {
                                Achievement.sendUnlockMessage(killer, achievement);
                            }
                        }
                    }
                }
            } else {
                scoreboardListener.updateScoreboardSouls(killer, souls);
                scoreboardListener.updateScoreboardCoins(killer, coins);

                TTA_Methods.sendActionBar(killer, ChatColor.AQUA + "[+" + souls + " Souls]" + ChatColor.LIGHT_PURPLE
                        + (coins != 0 ? " [+" + coins + (coins == 1 ? " Battle Coin]" : " Battle Coins]") : "")
                        + (essence ? ChatColor.YELLOW + " [" + eOwner + "'s Essence]" : ""));
                if (killerData.getKitPvpData().getLastKilledBy().equals(player.getUniqueId().toString())) {
                    TitleAPI.sendTitle(killer, 1, 40, 10, BoldColor.YELLOW.getColor() + "REVENGE!", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                    killerData.getKitPvpData().addRevengeKill();
                    killerData.getKitPvpData().setLastKilledBy("NONE");
                    for (Achievement.Type achievement : Achievement.Type.values()) {
                        if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Vengeful")) {
                            if (killerData.getKitPvpData().getRevengeKills() == achievement.getRequirement()) {
                                Achievement.sendUnlockMessage(killer, achievement);
                            }
                        }
                    }
                } else {
                    TitleAPI.sendTitle(killer, 1, 30, 10, " ", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                }
            }
            if (Battlegrounds.killStreak.get(killer.getUniqueId()) > killerData.getKitPvpData().getHighestKillstreak()) {
                killerData.getKitPvpData().setHighestKillstreak(Battlegrounds.killStreak.get(killer.getUniqueId()));
                for (Achievement.Type achievement : Achievement.Type.values()) {
                    if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Sadist")) {
                        if (killerData.getKitPvpData().getHighestKillstreak() == achievement.getRequirement()) {
                            Achievement.sendUnlockMessage(killer, achievement);
                        }
                    }
                }
            }
        } else {
            Battlegrounds.killStreak.put(killer.getUniqueId(), 1);
            scoreboardListener.updateScoreboardSouls(killer, souls);
            scoreboardListener.updateScoreboardCoins(killer, coins);

            TTA_Methods.sendActionBar(killer, ChatColor.AQUA + "[+" + souls + " Souls]"
                    + ChatColor.LIGHT_PURPLE + (coins != 0 ? " [+" + coins + (coins == 1 ? " Battle Coin]" : " Battle Coins]") : "")
                    + (essence ? ChatColor.YELLOW + " [" + eOwner + "'s Essence]" : ""));
            if (killerData.getKitPvpData().getLastKilledBy().equals(player.getUniqueId().toString())) {
                TitleAPI.sendTitle(killer, 1, 40, 10, BoldColor.YELLOW.getColor() + "REVENGE!", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                killerData.getKitPvpData().addRevengeKill();
                killerData.getKitPvpData().setLastKilledBy("NONE");
                for (Achievement.Type achievement : Achievement.Type.values()) {
                    if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Vengeful")) {
                        if (killerData.getKitPvpData().getRevengeKills() == achievement.getRequirement()) {
                            Achievement.sendUnlockMessage(killer, achievement);
                        }
                    }
                }
            } else {
                TitleAPI.sendTitle(killer, 1, 30, 10, " ", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
            }
        }

        if (Battlegrounds.killStreak.containsKey(player.getUniqueId())) {
            if (Battlegrounds.killStreak.get(player.getUniqueId()) >= 5) {
                plugin.getServer().broadcastMessage(ChatColor.GOLD + killer.getName()
                        + ChatColor.GRAY + " ended " + ChatColor.RED + player.getName()
                        + "'s " + ChatColor.GRAY + "killstreak of " + BoldColor.RED.getColor() + Battlegrounds.killStreak.get(player.getUniqueId()) + "!");
                killerData.getKitPvpData().addKillstreakEnded();
                for (Achievement.Type achievement : Achievement.Type.values()) {
                    if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Buzzkill")) {
                        if (killerData.getKitPvpData().getKillstreaksEnded() == achievement.getRequirement()) {
                            Achievement.sendUnlockMessage(killer, achievement);
                        }
                    }
                }
                TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "Killed by " + BoldColor.GOLD.getColor() + killer.getName(),
                        ChatColor.YELLOW + "You reached a " + BoldColor.GOLD.getColor() + Battlegrounds.killStreak.get(player.getUniqueId()) + ChatColor.YELLOW + " killstreak! Nice!");

                plugin.getGlobalStats().addKillstreakEnded();
            }
            Battlegrounds.killStreak.remove(player.getUniqueId());
        }
    }
}
