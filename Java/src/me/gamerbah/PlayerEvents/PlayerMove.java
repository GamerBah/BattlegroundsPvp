package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/14/2016 */


import lombok.Getter;
import me.gamerbah.Battlegrounds;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

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
            player.setVelocity(player.getLocation().getDirection().multiply(3));
            player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
            if (!launched.contains(player)) {
                launched.add(player);
            }
        }
    }
}
