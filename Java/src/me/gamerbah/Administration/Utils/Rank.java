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
    WARLORD("Warlord", ChatColor.AQUA, 10),
    MASTER("Master", ChatColor.YELLOW, 8),
    CHAMPION("Champion", ChatColor.LIGHT_PURPLE, 6),
    ELITE("Elite", ChatColor.DARK_PURPLE, 4),
    DEFAULT("Default", ChatColor.GRAY, 0);


    private String name;
    private ChatColor color;
    private int level;
}
