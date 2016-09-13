package com.battlegroundspvp.PlayerEvents;

import com.battlegroundspvp.Battlegrounds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerItemDrop implements Listener {
    private Battlegrounds plugin;

    public PlayerItemDrop(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}