package me.gamerbah.Listeners;
/* Created by GamerBah on 8/16/2016 */

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListPingListener implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        event.setMotd("           §7\u00AB  §f\u00AB  §7\u00AB   §6§lBATTLEGROUNDS   §7\u00BB  §f\u00BB  §7\u00BB\n" +
                "         §b\u00BB    §e§lCLOSED ALPHA §c§lCOMING SOON!    §b\u00AB");
    }
}
