package com.battlegroundspvp.Utils;
/* Created by GamerBah on 9/8/2016 */

import com.battlegroundspvp.Utils.Messages.BoldColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;


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
        TRAIL_RAIN_STORM(20, PARTICLE_PACK, "Rain Storm", new I(Material.WATER_BUCKET)
                .name(BoldColor.GOLD.getColor() + "Rain Storm")
                .lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A rain cloud over your head")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Water splashes and rain drops"),
                Rarity.EPIC, null, 0, 0),
        TRAIL_LAVA_RAIN(21, PARTICLE_PACK, "Lava Rain", new I(Material.LAVA_BUCKET)
                .name(BoldColor.GOLD.getColor() + "Lava Rain")
                .lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A smoke cloud raining lava over your head")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Lava bubbles and lava drops"),
                Rarity.EPIC, null, 0, 0),

        // Legendary
        TRAIL_FLAME_WARRIOR(40, PARTICLE_PACK, "Flame Warrior", new I(Material.FLINT_AND_STEEL)
                .name(BoldColor.PINK.getColor() + "Flame Warrior")
                .lore(ChatColor.YELLOW + "Idle Effect: " + ChatColor.GRAY + "A flame helix rotating around you")
                .lore(ChatColor.YELLOW + "Moving Effect: " + ChatColor.GRAY + "Flames and smoke"),
                Rarity.LEGENDARY, null, 0, 0),


        /**
         * KILL SOUNDS
         */
        // Rare
        SOUND_PIG_DEATH(100, KILL_SOUND, "Pig Death", new I(Material.PORK)
                .name(ChatColor.BLUE + "Pig Death")
                .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Gruesome yet satisfying!"),
                Rarity.RARE, Sound.ENTITY_PIG_DEATH, 1, 1),
        SOUND_LEVEL_UP(101, KILL_SOUND, "Level Up", new I(Material.EXP_BOTTLE)
                .name(ChatColor.BLUE + "Level Up")
                .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Something doesn't quite add up..."),
                Rarity.RARE, Sound.ENTITY_PLAYER_LEVELUP, 1.1F, 1),
        SOUND_ANVIL(102, KILL_SOUND, "Anvil", new I(Material.ANVIL)
                .name(ChatColor.BLUE + "Anvil")
                .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Crushed by pure iron."),
                Rarity.RARE, Sound.BLOCK_ANVIL_FALL, 1, 0.9F),

        // Epic
        SOUND_EXPLODE(130, KILL_SOUND, "Explosion", new I(Material.TNT)
                .name(BoldColor.GOLD.getColor() + "Explosion")
                .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "Kaboom!"),
                Rarity.EPIC, Sound.ENTITY_GENERIC_EXPLODE, 1, 1),

        //Legendary
        SOUND_CAT_MEOW(160, KILL_SOUND, "Meow", new I(Material.DIAMOND)
                .name(BoldColor.PINK.getColor() + "Meow")
                .lore(ChatColor.GRAY + "" + ChatColor.ITALIC + "A cute and cuddly death!"),
                Rarity.LEGENDARY, Sound.ENTITY_CAT_AMBIENT, 1.3F, 1),


        /**
         * NONE
         */
        TRAIL_NONE(1000, PARTICLE_PACK, "None", new I(Material.BARRIER).name(ChatColor.RED + "None").lore(ChatColor.GRAY + "Removes your trail"),
                Rarity.COMMON, null, 0, 0);

        private int id;
        private Cosmetic group;
        private String name;
        private I item;
        private Rarity rarity;
        private Sound killSound;
        private float volume;
        private float pitch;
    }
}
