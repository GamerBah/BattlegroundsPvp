package com.battlegroundspvp.Listeners;
/* Created by GamerBah on 8/16/2016 */

import com.battlegroundspvp.Administration.Runnables.AutoUpdate;
import com.battlegroundspvp.Battlegrounds;
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
        if (AutoUpdate.updating) {
            event.setMotd("           §7\u00AB  §f\u00AB  §7\u00AB   §6§lBATTLEGROUNDS   §7\u00BB  §f\u00BB  §7\u00BB\n" +
                    "               §f§lUPDATING... HANG IN THERE!");
        } else if (plugin.getConfig().getBoolean("developmentMode")) {
            event.setMotd("           §7\u00AB  §4\u00AB  §7\u00AB   §6§lBATTLEGROUNDS   §7\u00BB  §4\u00BB  §7\u00BB\n" +
                    "                   §c§lIN DEVELOPMENT MODE");
        } else {
            event.setMotd("           §7\u00AB  §f\u00AB  §7\u00AB   §6§lBATTLEGROUNDS   §7\u00BB  §f\u00BB  §7\u00BB\n" +
                    "              §e§lCLOSED ALPHA §a§lIN PROGRESS!");
        }
    }
}
