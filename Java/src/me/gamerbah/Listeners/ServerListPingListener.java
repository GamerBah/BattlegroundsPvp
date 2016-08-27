package me.gamerbah.Listeners;
/* Created by GamerBah on 8/16/2016 */

import me.gamerbah.Battlegrounds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {

    private Battlegrounds plugin;

    public ServerListPingListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        if (!plugin.getConfig().getBoolean("developmentMode")) {
            event.setMotd("           §7\u00AB  §f\u00AB  §7\u00AB   §6§lBATTLEGROUNDS   §7\u00BB  §f\u00BB  §7\u00BB\n" +
                    "               §e§lCLOSED ALPHA §c§lCOMING SOON!");
        } else {
            event.setMotd("           §7\u00AB  §4\u00AB  §7\u00AB   §6§lBATTLEGROUNDS   §7\u00BB  §4\u00BB  §7\u00BB\n" +
                    "                   §c§lIN DEVELOPMENT MODE");
        }
    }
}
