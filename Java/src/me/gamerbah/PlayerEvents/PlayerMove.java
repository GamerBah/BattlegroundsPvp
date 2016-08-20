package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/14/2016 */


import lombok.Getter;
import me.gamerbah.Battlegrounds;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class PlayerMove implements Listener {

    @Getter
    private static List<Player> launched = new ArrayList<>();
    private Battlegrounds plugin;

    public PlayerMove(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfig().getBoolean("essenceActive")) {
            long duration = (plugin.getConfig().getInt("essenceTimeRemaining") / 360) * 20;
            BukkitTask task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () ->
                    //ParticleEffect.FIREWORKS_SPARK.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 1, 0), 0.5, 0.5, 0.5, 0, 1, 25), 0L, 15L);
                    ParticleEffect.REDSTONE.sendColor(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 1, 0), Color.TEAL), 0L, 15L);
            plugin.getServer().getScheduler().runTaskLater(plugin, task::cancel, duration);
        }

        if (event.getTo().getBlock().getType().equals(Material.GOLD_PLATE)) {
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                return;
            }
            if (Battlegrounds.getAfk().contains(player.getUniqueId())) {
                return;
            }
            player.setVelocity(player.getLocation().getDirection().multiply(3));
            player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 2, 0.3F);
            if (!launched.contains(player)) {
                launched.add(player);
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> launched.remove(player), 50L);
                BukkitTask task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () ->
                        ParticleEffect.FIREWORKS_SPARK.send(Bukkit.getOnlinePlayers(), player.getLocation(), 0, 0.5, 0, 0.05D, 5, 30), 0L, 1L);
                plugin.getServer().getScheduler().runTaskLater(plugin, task::cancel, 20);
            }
        }
    }
}
