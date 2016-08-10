package me.gamerbah.Administration.Utils;/* Created by GamerBah on 8/7/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
@AllArgsConstructor
public enum Rank {

    OWNER("Owner", ChatColor.RED, 100),
    DEVELOPER("Dev", ChatColor.GOLD, 75),
    ADMIN("Admin", ChatColor.RED, 50),
    MODERATOR("Mod", ChatColor.DARK_AQUA, 25),
    HELPER("Helper", ChatColor.GREEN, 15),
    WARLORD("Warlord", ChatColor.YELLOW, 10),
    CHAMPION("Champion", ChatColor.LIGHT_PURPLE, 8),
    ELITE("Elite", ChatColor.DARK_PURPLE, 6),
    MASTER("Master", ChatColor.AQUA, 4),
    DEFAULT(null, null, 0);


    private String name;
    private ChatColor color;
    private int level;
}
