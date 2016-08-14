package me.gamerbah.Events;
/* Created by GamerBah on 8/14/2016 */


import lombok.Getter;
import me.gamerbah.Battlegrounds;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PlayerMove implements Listener {

    private Battlegrounds plugin;
    @Getter
    private static List<Player> launched = new ArrayList<>();

    public PlayerMove(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo().getBlock().getType().equals(Material.GOLD_PLATE)) {
            player.setVelocity(player.getLocation().getDirection().multiply(3));
            player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
            if (!launched.contains(player)) {
                launched.add(player);
            }
        }
    }

}
