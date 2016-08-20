package me.gamerbah.Listeners;

import me.gamerbah.Battlegrounds;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpawnProtectListener implements Listener {
    private Battlegrounds plugin;

    public SpawnProtectListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        Location damagerLocation = damager.getLocation();
        Location damagedLocation = damaged.getLocation();

        if ((damagerLocation.distance(damager.getWorld().getSpawnLocation()) <= 10
                || (damagedLocation.distance(damaged.getWorld().getSpawnLocation()) <= 10))) {
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

        if (location.distance(location.getWorld().getSpawnLocation()) <= 10) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (Battlegrounds.getAfk().contains(player.getUniqueId())) {
            if (location.distance(location.getWorld().getSpawnLocation()) >= 9) {
                plugin.respawn(player);
                player.sendMessage(ChatColor.RED + "You are AFK, so you are not able to leave the spawn!");
                player.sendMessage(ChatColor.YELLOW + "If you wish to leave, use " + ChatColor.GREEN + "/afk");
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();

        if (location.distance(location.getWorld().getSpawnLocation()) <= 12) {
            event.setCancelled(true);
            entity.remove();
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        Location location = entity.getLocation();

        if (location.distance(location.getWorld().getSpawnLocation()) <= 11) {
            entity.remove();
        }
    }
}
