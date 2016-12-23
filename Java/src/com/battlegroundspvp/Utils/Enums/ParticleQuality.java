package com.battlegroundspvp.Utils.Enums;/* Created by GamerBah on 12/23/2016 */

import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public enum ParticleQuality {

    LOW("Low", new I(Material.CLAY_BRICK)
            .name(ChatColor.AQUA + "Particle Quality")
            .lore(BoldColor.GREEN.getColor() + "Low").lore(" ")
            .lore(ChatColor.GRAY + "Changes the amount of")
            .lore(ChatColor.GRAY + "particles that display")
            .lore(ChatColor.GRAY + "in some effects").lore(" ")
            .lore(ChatColor.YELLOW + "Click to change!")),
    MEDIUM("Medium", new I(Material.IRON_INGOT)
            .name(ChatColor.AQUA + "Particle Quality")
            .lore(BoldColor.GOLD.getColor() + "Medium").lore(" ")
            .lore(ChatColor.GRAY + "Changes the amount of")
            .lore(ChatColor.GRAY + "particles that display")
            .lore(ChatColor.GRAY + "in some effects").lore(" ")
            .lore(ChatColor.YELLOW + "Click to change!")),
    HIGH("High", new I(Material.GOLD_INGOT)
            .name(ChatColor.AQUA + "Particle Quality")
            .lore(BoldColor.RED.getColor() + "High " + ChatColor.GRAY + "(Default)").lore(" ")
            .lore(ChatColor.GRAY + "Changes the amount of")
            .lore(ChatColor.GRAY + "particles that display")
            .lore(ChatColor.GRAY + "in some effects").lore(" ")
            .lore(ChatColor.YELLOW + "Click to change!"));

    @Getter
    private String name;
    @Getter
    private I item;

    ParticleQuality(String name, I item) {
        this.name = name;
        this.item = item;
    }
}
