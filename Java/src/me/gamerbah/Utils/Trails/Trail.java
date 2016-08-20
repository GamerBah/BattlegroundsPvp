package me.gamerbah.Utils.Trails;
/* Created by GamerBah on 8/20/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Rarity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public class Trail {

    public static Trail.Type typeFromName(String name) {
        for (Trail.Type type : Type.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    public enum Type {

        RAIN_STORM("Rain Storm", new I(Material.WATER_BUCKET).name(Rarity.EPIC.getColor() + "Rain Storm").lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A rain cloud over your head")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Water splashes and rain drops"), Rarity.EPIC),
        LAVA_RAIN("Lava Rain", new I(Material.LAVA_BUCKET).name(Rarity.EPIC.getColor() + "Lava Rain").lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A smoke cloud raining lava over your head")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Lava bubbles and lava drops"), Rarity.EPIC),

        NONE("None", new I(Material.BARRIER).name(ChatColor.RED + "None").lore(ChatColor.GRAY + "Removes your trail"), Rarity.COMMON);

        private String name;
        private I item;
        private Rarity rarity;
    }
}
