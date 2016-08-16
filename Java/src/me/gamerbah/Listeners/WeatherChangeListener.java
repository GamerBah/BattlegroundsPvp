package me.gamerbah.Listeners;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    private Battlegrounds plugin;

    public WeatherChangeListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }
}
