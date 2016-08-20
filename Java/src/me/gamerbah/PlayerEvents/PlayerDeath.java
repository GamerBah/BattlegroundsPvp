package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/9/2016 */


import me.gamerbah.Administration.Commands.ChatCommands;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.KDRatio;
import me.gamerbah.Utils.TextComponentMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
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
        plugin.getServer().getScheduler().runTask(plugin, () ->
                plugin.respawn(player));

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        KDRatio kdRatio = new KDRatio(plugin);
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        scoreboardListener.getDeaths().put(player.getUniqueId(), playerData.getDeaths());
        scoreboardListener.getKds().put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));
        playerData.setDeaths(playerData.getDeaths() + 1);
        scoreboardListener.updateScoreboardDeaths(player);
        scoreboardListener.updateScoreboardRatio(player);

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
            return;
        }

        PlayerData killerData = plugin.getPlayerData(killer.getUniqueId());
        scoreboardListener.getKills().put(killer.getUniqueId(), killerData.getKills());
        scoreboardListener.getKds().put(killer.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(killer));
        killerData.setKills(killerData.getKills() + 1);
        scoreboardListener.updateScoreboardKills(killer);
        scoreboardListener.updateScoreboardRatio(killer);

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

        TextComponent tc = new TextComponent("");

        BaseComponent baseComponent = tc;
        baseComponent.addExtra(killerTCM);
        baseComponent.addExtra(wkb);
        baseComponent.addExtra(killedTCM);

        if (plugin.getServer().getOnlinePlayers().size() >= 15 || ChatCommands.chatSilenced) {
            event.setDeathMessage(null);
        } else {
            event.setDeathMessage(null);
            plugin.getServer().spigot().broadcast(baseComponent);
        }

        if (killer.getHealth() % 2 == 0) {
            player.sendMessage("" + ChatColor.RED + killer.getName() + ChatColor.GRAY + " had " + ChatColor.RED + (((int) killer.getHealth()) / 2) + " hearts" + ChatColor.GRAY + " left");
        } else {
            player.sendMessage("" + ChatColor.RED + killer.getName() + ChatColor.GRAY + " had " + ChatColor.RED + (((int) killer.getHealth()) / 2) + ".5 hearts" + ChatColor.GRAY + " left");
        }

        if (killer.getName().equals(player.getName())) {
            return;
        }

        /*if(playerData.getKills() == 1) {
            playerData.addAchievement(Achievement.FIRST_KILL);
            killer.sendMessage("You got the achievement: Fresh from the Pile");
        }*/

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

        int souls = ThreadLocalRandom.current().nextInt(2, 6 + 1);
        int coins = 0;
        if (ThreadLocalRandom.current().nextInt(1, 7 + 1) == 1) {
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

                scoreboardListener.getSouls().put(killer.getUniqueId(), killerData.getSouls());
                killerData.setSouls(killerData.getSouls() + (souls * (killstreak / 5)));
                scoreboardListener.updateScoreboardSouls(killer);
                scoreboardListener.getCoins().put(killer.getUniqueId(), killerData.getCoins());
                killerData.setCoins(killerData.getCoins() + coins);
                scoreboardListener.updateScoreboardCoins(killer);

                killer.sendMessage(ChatColor.DARK_AQUA + "[ +" + BoldColor.AQUA.getColor() + souls * (killstreak / 5) + " souls" + ChatColor.DARK_AQUA + " ] "
                        + ChatColor.DARK_PURPLE + (coins != 0 ? "[ +" + BoldColor.PINK.getColor() + coins + " Coins" + ChatColor.DARK_PURPLE + " ]" : "")
                        + (essence ? ChatColor.BLUE + "[" + eOwner + "'s Essence]" : ""));
            } else {
                scoreboardListener.getSouls().put(killer.getUniqueId(), killerData.getSouls());
                killerData.setSouls(killerData.getSouls() + souls);
                scoreboardListener.updateScoreboardSouls(killer);
                scoreboardListener.getCoins().put(killer.getUniqueId(), killerData.getCoins());
                killerData.setCoins(killerData.getCoins() + coins);
                scoreboardListener.updateScoreboardCoins(killer);

                killer.sendMessage(ChatColor.DARK_AQUA + "[+" + BoldColor.AQUA.getColor() + souls + " souls" + ChatColor.DARK_AQUA + "] "
                        + ChatColor.DARK_PURPLE + (coins != 0 ? "[+" + BoldColor.PINK.getColor() + coins + " Coins" + ChatColor.DARK_PURPLE + "]" : "")
                        + (essence ? ChatColor.BLUE + "[" + eOwner + "'s Essence]" : ""));
            }
        } else {
            Battlegrounds.killStreak.put(killer.getUniqueId(), 1);

            scoreboardListener.getSouls().put(killer.getUniqueId(), killerData.getSouls());
            killerData.setSouls(killerData.getSouls() + souls);
            scoreboardListener.updateScoreboardSouls(killer);
            scoreboardListener.getCoins().put(killer.getUniqueId(), killerData.getCoins());
            killerData.setCoins(killerData.getCoins() + coins);
            scoreboardListener.updateScoreboardCoins(killer);

            killer.sendMessage(ChatColor.DARK_AQUA + "[+" + BoldColor.AQUA.getColor() + souls + " souls" + ChatColor.DARK_AQUA + "] "
                    + ChatColor.DARK_PURPLE + (coins != 0 ? "[+" + BoldColor.PINK.getColor() + coins + " Coins" + ChatColor.DARK_PURPLE + "]" : "")
                    + (essence ? ChatColor.BLUE + "[" + eOwner + "'s Essence]" : ""));
        }

        if (Battlegrounds.killStreak.containsKey(player.getUniqueId())) {
            if (Battlegrounds.killStreak.get(player.getUniqueId()) >= 10) {
                plugin.getServer().broadcastMessage(ChatColor.GOLD + killer.getName()
                        + ChatColor.GRAY + " ended " + ChatColor.RED + player.getName()
                        + "'s " + ChatColor.GRAY + "killstreak of " + BoldColor.RED.getColor() + Battlegrounds.killStreak.get(player.getUniqueId()) + "!");
            }
            Battlegrounds.killStreak.remove(player.getUniqueId());
        }
    }
}
