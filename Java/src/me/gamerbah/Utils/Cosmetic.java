package me.gamerbah.Utils;
/* Created by GamerBah on 9/8/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;


public enum Cosmetic {
    PARTICLE_PACK, KILL_SOUND, KILL_EFFECT;

    public static Item typeFromName(String name, Cosmetic group) {
        for (Item item : Item.values()) {
            if (item.getName().equals(name) && item.getGroup().equals(group)) {
                return item;
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    public enum Item {
        /**
         * PARTICLE PACKS
         */
        // Rare


        // Epic
        TRAIL_RAIN_STORM(PARTICLE_PACK, "Rain Storm", new I(Material.WATER_BUCKET).name(BoldColor.GOLD.getColor() + "Rain Storm")
                .lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A rain cloud over your head")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Water splashes and rain drops"), Rarity.EPIC),
        TRAIL_LAVA_RAIN(PARTICLE_PACK, "Lava Rain", new I(Material.LAVA_BUCKET).name(BoldColor.GOLD.getColor() + "Lava Rain")
                .lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A smoke cloud raining lava over your head")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Lava bubbles and lava drops"), Rarity.EPIC),

        // Legendary
        TRAIL_FLAME_WARRIOR(PARTICLE_PACK, "Flame Warrior", new I(Material.FLINT_AND_STEEL).name(BoldColor.PINK.getColor() + "Flame Warrior")
                .lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A flame helix rotating around you")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Flames and smoke"), Rarity.LEGENDARY),

        TRAIL_NONE(PARTICLE_PACK, "None", new I(Material.BARRIER).name(ChatColor.RED + "None").lore(ChatColor.GRAY + "Removes your trail"), Rarity.COMMON);

        private Cosmetic group;
        private String name;
        private I item;
        private Rarity rarity;
    }
}
