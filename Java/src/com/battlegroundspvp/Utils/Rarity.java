package com.battlegroundspvp.Utils;/* Created by GamerBah on 8/7/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@AllArgsConstructor
@Getter
public enum Rarity {

    COMMON("Common", ChatColor.GRAY, 4, 4),
    RARE("Rare", ChatColor.BLUE, 1, 5),
    EPIC("Epic", ChatColor.GOLD, 1, 15),
    LEGENDARY("Legendary", ChatColor.LIGHT_PURPLE, 1, 45);

    private String name;
    private ChatColor color;
    private int chanceBottom;
    private int chanceTop;

}