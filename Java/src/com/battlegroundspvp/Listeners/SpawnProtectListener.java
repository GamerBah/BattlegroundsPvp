package com.battlegroundspvp.Listeners;

import com.battlegroundspvp.Battlegrounds;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class SpawnProtectListener implements Listener {
    private Battlegrounds plugin;

    public SpawnProtectListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Location damagerLocation = event.getDamager().getLocation();
        Location damagedLocation = event.getEntity().getLocation();

        if ((damagerLocation.distance(event.getEntity().getWorld().getSpawnLocation()) <= 12
                || (damagedLocation.distance(event.getDamager().getWorld().getSpawnLocation()) <= 12))) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Location location = player.getLocation();

        if (location.distance(location.getWorld().getSpawnLocation()) <= 12) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();

        if (entity instanceof Arrow) plugin.getGlobalStats().addArrowFired();
        if (entity instanceof EnderPearl) plugin.getGlobalStats().addEnderpearlThrown();
        if (location.distance(location.getWorld().getSpawnLocation()) <= 12) {
            event.setCancelled(true);
            entity.remove();
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();

        if (location.distance(location.getWorld().getSpawnLocation()) <= 12) {
            entity.remove();
        }
    }
}
