package me.gamerbah.Utils.Donations;
/* Created by GamerBah on 8/19/2016 */


import me.gamerbah.Battlegrounds;
import net.md_5.bungee.api.ChatColor;

public class DonationUpdater implements Runnable {
    private Battlegrounds plugin;

    public DonationUpdater(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Essence essence = new Essence(plugin);
        if (!plugin.getConfig().getBoolean("essenceActive")) {
            return;
        }
        int timeRemaining = plugin.getConfig().getInt("essenceTimeRemaining");

        if (timeRemaining > 0) {
            plugin.getConfig().set("essenceTimeRemaining", timeRemaining - 360);
        } else {
            plugin.getServer().broadcastMessage(ChatColor.GOLD + "" + plugin.getConfig().get("essenceOwner") + "'s Battle Essence has expired!");
            essence.removeActiveEssence();
        }
    }

}
