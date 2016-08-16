package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFoodLevelChange implements Listener {

    private Battlegrounds plugin;

    public PlayerFoodLevelChange(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        event.setCancelled(true);
        player.setFoodLevel(20);
    }
}
