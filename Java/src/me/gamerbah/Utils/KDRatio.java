package me.gamerbah.Utils;
/* Created by GamerBah on 8/15/2016 */

import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class KDRatio {

    private Battlegrounds plugin;

    public KDRatio(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public ChatColor getRatioColor(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        ChatColor ratioColor = ChatColor.GRAY;
        double ratio = ((double) playerData.getKills() / (double) playerData.getDeaths());
        ratio = Math.round(ratio * 100.00D) / 100.00D;
        if (playerData.getDeaths() == 0) {
            ratio = playerData.getKills();
        }
        if (ratio < 0.25D) {
            ratioColor = ChatColor.DARK_RED;
        } else if (ratio >= 0.25D && ratio < 0.50D) {
            ratioColor = ChatColor.RED;
        } else if (ratio >= 0.50D && ratio < 0.75D) {
            ratioColor = ChatColor.GOLD;
        } else if (ratio >= 0.75D && ratio < 1.00D) {
            ratioColor = ChatColor.YELLOW;
        } else if (ratio >= 1.00D && ratio < 1.50D) {
            ratioColor = ChatColor.GREEN;
        } else if (ratio >= 1.50D && ratio < 2.00D) {
            ratioColor = ChatColor.DARK_GREEN;
        } else if (ratio >= 2.00D && ratio < 3.00D) {
            ratioColor = ChatColor.AQUA;
        } else if (ratio >= 3.00D) {
            ratioColor = ChatColor.LIGHT_PURPLE;
        }
        return ratioColor;
    }

    public Double getRatio(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        double ratio = ((double) playerData.getKills() / (playerData.getDeaths() > 0 ? (double) playerData.getDeaths() : 1));
        return Math.round(ratio * 100.00D) / 100.00D;
    }
}
