package com.battlegroundspvp.Utils.Messages;/* Created by GamerBah on 8/15/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@AllArgsConstructor
@Getter
public enum BoldColor {

    RED(ChatColor.RED + "" + ChatColor.BOLD + ""),
    BLUE(ChatColor.BLUE + "" + ChatColor.BOLD + ""),
    YELLOW(ChatColor.YELLOW + "" + ChatColor.BOLD + ""),
    GOLD(ChatColor.GOLD + "" + ChatColor.BOLD + ""),
    AQUA(ChatColor.AQUA + "" + ChatColor.BOLD + ""),
    GREEN(ChatColor.GREEN + "" + ChatColor.BOLD + ""),
    PINK(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + ""),
    PURPLE(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + ""),
    DARK_AQUA(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + ""),
    DARK_RED(ChatColor.DARK_RED + "" + ChatColor.BOLD + ""),
    DARK_BLUE(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + ""),
    GRAY(ChatColor.GRAY + "" + ChatColor.BOLD + ""),
    DARK_GRAY(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + ""),
    WHITE(ChatColor.WHITE + "" + ChatColor.BOLD + ""),
    DARK_GREEN(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "");

    private String color;
}
