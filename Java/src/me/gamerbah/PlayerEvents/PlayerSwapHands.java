package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/27/2016 */


import me.gamerbah.Battlegrounds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHands implements Listener {

    private Battlegrounds plugin;

    public PlayerSwapHands(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }
}
