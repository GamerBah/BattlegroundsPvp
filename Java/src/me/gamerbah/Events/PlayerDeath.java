package me.gamerbah.Events;
/* Created by GamerBah on 8/9/2016 */


import me.gamerbah.Administration.Commands.ChatCommands;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.TextComponentMessages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;
import org.inventivetalent.particle.ParticleEffect;

import java.util.Random;

public class PlayerDeath {

    private Battlegrounds plugin;

    public PlayerDeath(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        Location location = player.getLocation();

        BukkitTask task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            ParticleEffect.FLAME.send(plugin.getServer().getOnlinePlayers(), player.getLocation(), 0, 1.5, 0, 0, 10, 100);
        }, 10L, 0L);

        plugin.getServer().getScheduler().runTaskLater(plugin, task::cancel, 10);

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            plugin.respawn(player, player.getWorld().getSpawnLocation());
        });

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        playerData.setDeaths(playerData.getDeaths() + 1);

        if (killer == null) {
            if (plugin.getServer().getOnlinePlayers().size() >= 15) {
                event.setDeathMessage(null);
            } else {
                event.setDeathMessage("" + ChatColor.RED + player.getName() + ChatColor.GRAY + " died");
            }
            return;
        }

        TextComponentMessages tcm = new TextComponentMessages(plugin);
        TextComponent killerTCM = new TextComponent(killer.getName());
        killerTCM.setColor(ChatColor.GOLD);
        killerTCM.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tcm.playerStats(killer)));
        killerTCM.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/options " + killer.getName()));

        TextComponent killedTCM = new TextComponent(player.getName());
        killedTCM.setColor(ChatColor.RED);
        killedTCM.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tcm.playerStats(player)));
        killedTCM.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/options " + player.getName()));

        TextComponent wkb = new TextComponent(" was killed by ");
        wkb.setColor(ChatColor.GRAY);
        wkb.setHoverEvent(null);

        TextComponent tc = new TextComponent("");

        BaseComponent baseComponent = tc;
        baseComponent.addExtra(killedTCM);
        baseComponent.addExtra(wkb);
        baseComponent.addExtra(killerTCM);

        if (ChatCommands.chatSilenced) {
            event.setDeathMessage(null);
        } else {
            if (plugin.getServer().getOnlinePlayers().size() >= 15) {
                event.setDeathMessage(null);
            } else {
                plugin.getServer().spigot().broadcast(baseComponent);
            }
        }

        if (killer.getHealth() % 2 == 0) {
            player.sendMessage("" + ChatColor.RED + killer.getName() + ChatColor.YELLOW + " had " + ChatColor.RED + (((int) killer.getHealth()) / 2) + " hearts" + ChatColor.YELLOW + " left");
        } else {
            player.sendMessage("" + ChatColor.RED + killer.getName() + ChatColor.YELLOW + " had " + ChatColor.RED + (((int) killer.getHealth()) / 2) + ".5 hearts" + ChatColor.YELLOW + " left");
        }

        if (killer.getName().equals(player.getName())) {
            return;
        }

        PlayerData killerData = plugin.getPlayerData(killer.getUniqueId());
        killerData.setKills(playerData.getKills() + 1);
        killer.sendMessage(ChatColor.GRAY + "You killed " + ChatColor.RED + player.getName());

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

        Random random = new Random();
        int souls = random.nextInt(10 - 4 + 1) + 4;

        if (Battlegrounds.killStreak.containsKey(killer.getUniqueId())) {
            Battlegrounds.killStreak.put(killer.getUniqueId(), Battlegrounds.killStreak.get(killer.getUniqueId()) + 1);
            int killstreak = Battlegrounds.killStreak.get(killer.getUniqueId());
            if (killstreak % 5 == 0) {
                plugin.getServer().broadcastMessage(ChatColor.GOLD + killer.getName() + ChatColor.GRAY + " is on a " + BoldColor.RED.getColor() + killstreak + " killstreak!");
                killerData.setSouls(killerData.getSouls() + (souls * (killstreak / 5)));
                killer.sendMessage(ChatColor.GRAY + "You gained " + BoldColor.AQUA.getColor() + souls * (killstreak / 5) + " souls");
            } else {
                killerData.setSouls(killerData.getSouls() + souls);
                killer.sendMessage(ChatColor.GRAY + "You gained " + BoldColor.AQUA.getColor() + souls + " souls");
            }
        } else {
            Battlegrounds.killStreak.put(killer.getUniqueId(), 1);
            killerData.setSouls(killerData.getSouls() + souls);
            killer.sendMessage(ChatColor.GRAY + "You gained " + BoldColor.AQUA.getColor() + souls + " souls");
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
