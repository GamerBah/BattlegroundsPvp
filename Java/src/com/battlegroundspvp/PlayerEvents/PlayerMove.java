package com.battlegroundspvp.PlayerEvents;
/* Created by GamerBah on 8/14/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Kits.Epic.DarkRider;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
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
                BukkitTask task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                    if (launched.contains(player)) {
                        ParticleEffect.FIREWORKS_SPARK.send(Bukkit.getOnlinePlayers(), player.getLocation(), 0, 0.5, 0, 0.05D, 5, 30);
                    }
                }, 0L, 1L);
                plugin.getServer().getScheduler().runTaskLater(plugin, task::cancel, 20);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            if (event.getTo().distance(player.getWorld().getSpawnLocation()) <= 15 || event.getTo().distance(player.getWorld().getSpawnLocation()) >= 60) {
                event.setCancelled(true);
                player.getInventory().addItem(new I(Material.ENDER_PEARL).name(BoldColor.GOLD.getColor() + "Enderpearl"));
            }
        }
    }

    @EventHandler
    public void onDismount(VehicleExitEvent event) {
        Player player = (Player) event.getExited();
        if (DarkRider.getRiding().contains(player)) {
            event.getVehicle().setPassenger(player);
        } else {
            event.getVehicle().setPassenger(null);
        }
    }
}
