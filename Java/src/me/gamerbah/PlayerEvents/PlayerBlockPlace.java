package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBlockPlace implements Listener {

    private Battlegrounds plugin;

    public PlayerBlockPlace(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }
}
