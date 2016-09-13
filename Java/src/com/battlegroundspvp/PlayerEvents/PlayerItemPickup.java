package com.battlegroundspvp.PlayerEvents;

import com.battlegroundspvp.Battlegrounds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerItemPickup implements Listener {
    private Battlegrounds plugin;

    public PlayerItemPickup(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }
}
