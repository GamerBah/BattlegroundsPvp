package me.gamerbah.Administration.Donations;
/* Created by GamerBah on 8/19/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
            plugin.saveConfig();
        } else {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.sendMessage(BoldColor.GOLD.getColor() + "" + (plugin.getConfig().get("essenceOwner").equals(player.getName())
                        ? "Your" : plugin.getConfig().get("essenceOwner") + "'s") + " Battle Essence has ended!");
                if (!player.getName().equals(plugin.getConfig().get("essenceOwner"))) {
                    player.sendMessage(ChatColor.GREEN + "Buy your own! " + ChatColor.YELLOW + "battlegroundspvp.enjin.com/store");
                }
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.3F, 0.9F);
            }
            essence.removeActiveEssence();
        }
    }

}
