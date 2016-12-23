package com.battlegroundspvp.Administration.Runnables;
/* Created by GamerBah on 8/19/2016 */


import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.Time;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;

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
            if (!plugin.getConfig().getBoolean("developmentMode")) {
                plugin.getConfig().set("essenceTimeRemaining", timeRemaining - 1);
                plugin.saveConfig();
                long milliseconds = timeRemaining * 1000;
                float completion = Float.parseFloat(Double.toString(((double) milliseconds / (plugin.getConfig().getInt("essenceTime") * 60 * 60 * 1000))));
                Essence.Type essenceType = Essence.Type.ONE_HOUR_50_PERCENT;
                for (Essence.Type type : Essence.Type.values()) {
                    if (plugin.getConfig().getInt("essenceTime") == type.getTime() && plugin.getConfig().getInt("essenceIncrease") == type.getIncrease()) {
                        essenceType = type;
                    }
                }
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    BossBarAPI.removeAllBars(player);
                    BossBarAPI.addBar(player, new TextComponent(ChatColor.RED + Time.toString(milliseconds, true) + ChatColor.GRAY + " remaining in "
                                    + ChatColor.RED + plugin.getConfig().getString("essenceOwner") + ChatColor.GRAY + "'s Battle Essence (" + essenceType.getIncrease() + "%)"),
                            essenceType.getBarColor(), BossBarAPI.Style.PROGRESS, completion);
                }
            }
        } else {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                BossBarAPI.removeAllBars(player);
                player.sendMessage(BoldColor.GOLD.getColor() + "" + (plugin.getConfig().get("essenceOwner").equals(player.getName())
                        ? "Your" : plugin.getConfig().get("essenceOwner") + "'s") + " Battle Essence has ended!");
                if (!player.getName().equals(plugin.getConfig().get("essenceOwner"))) {
                    player.sendMessage(ChatColor.GREEN + "Buy your own! " + ChatColor.YELLOW + "battlegroundspvp.com/store");
                }
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.3F, 0.9F);
            }
            essence.removeActiveEssence();
        }
    }

}
