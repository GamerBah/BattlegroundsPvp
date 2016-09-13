package com.battlegroundspvp.Listeners;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Battlegrounds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemSpawnListener implements Listener {
    private Battlegrounds plugin;

    public ItemSpawnListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemSpawn(final ItemSpawnEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getEntity().remove();
            }
        }.runTaskLater(plugin, 40);
    }

}
