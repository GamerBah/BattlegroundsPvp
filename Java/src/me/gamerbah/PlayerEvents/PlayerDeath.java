package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/9/2016 */


import com.connorlinfoot.titleapi.TitleAPI;
import me.gamerbah.Administration.Commands.ChatCommands;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Achievements.Achievement;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.KDRatio;
import me.gamerbah.Utils.Messages.BoldColor;
import me.gamerbah.Utils.Messages.TextComponentMessages;
import me.gamerbah.Utils.Messages.Titles;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.inventivetalent.particle.ParticleEffect;

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

        ParticleEffect.LAVA.send(Bukkit.getOnlinePlayers(), player.getLocation(), 0, 0.2, 0, 1, 20, 100);

        player.setHealth(20);
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            plugin.respawn(player);
            player.getInventory().setHeldItemSlot(4);
        });

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        KDRatio kdRatio = new KDRatio(plugin);
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        scoreboardListener.updateScoreboardDeaths(player, 1);

        for (Achievement.Type achievement : Achievement.Type.values()) {
            if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Deathknell")) {
                if (playerData.getDeaths() == achievement.getRequirement()) {
                    Achievement.sendUnlockMessage(player, achievement);
                }
            }
        }

        if (killer == null) {
            if (plugin.getServer().getOnlinePlayers().size() >= 15 || ChatCommands.chatSilenced) {
                event.setDeathMessage(null);
            } else {
                if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " lost a fight with gravity");
                } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " tried to swim in lava");
                } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " forgot to come up for air");
                } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                        || event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " didn't stop, drop, and roll");
                } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " fell into the unknown");
                } else {
                    event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " died");
                }
            }
            int i = ThreadLocalRandom.current().nextInt(1, 3 + 1);

            if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Try not to fall so hard next time");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "" + ChatColor.ITALIC + "'You believed you could fly...'");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Flying is for airplanes, not people.");
                }
            } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Reminder: Lava will burn you...");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Yea, that orange stuff? IT BURNS!");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "That probably gave you 5th-degree burns");
                }
            } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Did you really think you were a fish?");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "" + ChatColor.ITALIC + "Just keep swimming, just keep swimming...");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "There's this neat thing called air. You needed it.");
                }
            } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                    || event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE) {
                if (i == 1) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Tip: Stop, Drop, and Roll");
                } else if (i == 2) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "One day, Minecraft will have fire extinguishers");
                } else if (i == 3) {
                    TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "You died!", ChatColor.GRAY + "Perfect for roasting marshmallows!");
                }
            } else if (event.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
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
            return;
        }

        PlayerData killerData = plugin.getPlayerData(killer.getUniqueId());
        scoreboardListener.updateScoreboardKills(killer, 1);

        for (Achievement.Type achievement : Achievement.Type.values()) {
            if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Brutality")) {
                if (killerData.getKills() == achievement.getRequirement()) {
                    Achievement.sendUnlockMessage(player, achievement);
                }
            }
        }

        playerData.setLastKilledBy(killer);

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

        /*for (Challenge challenge : Challenge.values()) {
            if (plugin.getChallengesFiles().challengeIsStarted(killer, challenge)) {
                plugin.getChallengesFiles().addChallengeKill(killer, challenge);
                if (plugin.getChallengesFiles().getChallengeKills(killer, challenge) >= challenge.getKillRequirement()) {
                    plugin.getChallengesFiles().setCompleted(killer, challenge);
                    killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1, 1);
                    killer.sendMessage(ChatColor.BOLD + "-------------------------------------------");
                    killer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Challenge Complete: " + ChatColor.AQUA + challenge.getName());
                    killer.sendMessage(ChatColor.DARK_AQUA + "You've earned " + ChatColor.GREEN + "$" + challenge.getRewardMoney());
                    killer.sendMessage(ChatColor.DARK_AQUA + " for completing the challenge!");
                    killer.sendMessage(ChatColor.BOLD + "-------------------------------------------");
                    killerData.depositBalance(challenge.getRewardMoney());
                }
            }
        }*/

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

                Titles.sendActionBar(killer, ChatColor.AQUA + "[+" + souls * ((killstreak / 5) + 1) + " Souls]" + ChatColor.LIGHT_PURPLE
                        + (coins != 0 ? " [+" + coins + (coins == 1 ? " Battle Coin]" : " Battle Coins]") : "")
                        + (essence ? ChatColor.YELLOW + " [" + eOwner + "'s Essence]" : ""));
                TitleAPI.sendTitle(killer, 1, 30, 10, BoldColor.GREEN.getColor() + killstreak + " Killstreak!", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1);
                if (killerData.getLastKilledBy().equals(player.getUniqueId().toString())) {
                    killerData.setRevengeKills(killerData.getRevengeKills() + 1);
                    killerData.setLastKilledBy("NONE");
                    for (Achievement.Type achievement : Achievement.Type.values()) {
                        if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Vengeful")) {
                            if (killerData.getRevengeKills() == achievement.getRequirement()) {
                                Achievement.sendUnlockMessage(player, achievement);
                            }
                        }
                    }
                }
            } else {
                scoreboardListener.updateScoreboardSouls(killer, souls);
                scoreboardListener.updateScoreboardCoins(killer, coins);

                Titles.sendActionBar(killer, ChatColor.AQUA + "[+" + souls + " Souls]" + ChatColor.LIGHT_PURPLE
                        + (coins != 0 ? " [+" + coins + (coins == 1 ? " Battle Coin]" : " Battle Coins]") : "")
                        + (essence ? ChatColor.YELLOW + " [" + eOwner + "'s Essence]" : ""));
                if (killerData.getLastKilledBy().equals(player.getUniqueId().toString())) {
                    TitleAPI.sendTitle(killer, 1, 40, 10, BoldColor.YELLOW.getColor() + "REVENGE!", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                    killerData.setRevengeKills(killerData.getRevengeKills() + 1);
                    killerData.setLastKilledBy("NONE");
                    for (Achievement.Type achievement : Achievement.Type.values()) {
                        if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Vengeful")) {
                            if (killerData.getRevengeKills() == achievement.getRequirement()) {
                                Achievement.sendUnlockMessage(player, achievement);
                            }
                        }
                    }
                } else {
                    TitleAPI.sendTitle(killer, 1, 30, 10, " ", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                }
            }
        } else {
            Battlegrounds.killStreak.put(killer.getUniqueId(), 1);
            scoreboardListener.updateScoreboardSouls(killer, souls);
            scoreboardListener.updateScoreboardCoins(killer, coins);

            Titles.sendActionBar(killer, ChatColor.AQUA + "[+" + souls + " Souls]"
                    + ChatColor.LIGHT_PURPLE + (coins != 0 ? " [+" + coins + (coins == 1 ? " Battle Coin]" : " Battle Coins]") : "")
                    + (essence ? ChatColor.YELLOW + " [" + eOwner + "'s Essence]" : ""));
            if (killerData.getLastKilledBy().equals(player.getUniqueId().toString())) {
                TitleAPI.sendTitle(killer, 1, 40, 10, BoldColor.YELLOW.getColor() + "REVENGE!", ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());
                killerData.setRevengeKills(killerData.getRevengeKills() + 1);
                killerData.setLastKilledBy("NONE");
                for (Achievement.Type achievement : Achievement.Type.values()) {
                    if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Vengeful")) {
                        if (killerData.getRevengeKills() == achievement.getRequirement()) {
                            Achievement.sendUnlockMessage(player, achievement);
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
                killerData.setKillstreaksEnded(killerData.getKillstreaksEnded() + 1);
                for (Achievement.Type achievement : Achievement.Type.values()) {
                    if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Buzzkill")) {
                        if (killerData.getKillstreaksEnded() == achievement.getRequirement()) {
                            Achievement.sendUnlockMessage(player, achievement);
                        }
                    }
                }
                TitleAPI.sendTitle(player, 5, 35, 20, BoldColor.RED.getColor() + "Killed by " + BoldColor.GOLD.getColor() + killer.getName(),
                        ChatColor.YELLOW + "You reached a " + BoldColor.GOLD.getColor() + Battlegrounds.killStreak.get(player.getUniqueId()) + ChatColor.YELLOW + " killstreak! Nice!");
            }
            if (Battlegrounds.killStreak.get(player.getUniqueId()) > playerData.getHighestKillstreak()) {
                playerData.setHighestKillstreak(Battlegrounds.killStreak.get(player.getUniqueId()));
                for (Achievement.Type achievement : Achievement.Type.values()) {
                    if (achievement.getGroup().equals(Achievement.COMBAT) && achievement.getName().contains("Sadist")) {
                        if (killerData.getHighestKillstreak() == achievement.getRequirement()) {
                            Achievement.sendUnlockMessage(player, achievement);
                        }
                    }
                }
            }
            Battlegrounds.killStreak.remove(player.getUniqueId());
        }
    }
}
