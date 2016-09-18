package com.battlegroundspvp.Kits.Epic;
/* Created by GamerBah on 9/14/2016 */


import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.Kit;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Rarity;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class DarkRider extends Kit {

    @Getter
    private static ArrayList<Player> riding = new ArrayList<>();

    public DarkRider() {
        super(41, "Dark Rider", new I(Material.SADDLE)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● §fIron Helmet")
                .lore("§7   ● §fIron Chestplate")
                .lore("§7   ● Chain Leggings")
                .lore("§7   ● §fIron Boots")
                .lore("§7   ● §6Gold Sword §7(Knockback I)")
                .lore(" ")
                .lore("§7§oRides a Skeletal Horse")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.EPIC);
    }

    protected void wear(Player player) {
        riding.add(player);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.LEATHER_HELMET).name(BoldColor.GOLD.getColor() + "Dark Rider's Helmet").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.LEATHER_CHESTPLATE).name(BoldColor.GOLD.getColor() + "Dark Rider's Chestplate").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.LEATHER_LEGGINGS).name(BoldColor.GOLD.getColor() + "Dark Rider's Leggins").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.LEATHER_BOOTS).name(BoldColor.GOLD.getColor() + "Dark Rider's Boots").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.GOLD_SWORD).name(BoldColor.GOLD.getColor() + "Dark Rider's Sword").enchantment(Enchantment.KNOCKBACK, 1).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);

        Horse horse = (Horse) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        horse.setAdult();
        horse.getInventory().setSaddle(new I(Material.SADDLE).name(BoldColor.GOLD.getColor() + "Saddle"));
        horse.setVariant(Horse.Variant.SKELETON_HORSE);
        horse.setCollidable(false);
        horse.setJumpStrength(0.84);
        horse.setOwner(player);
        horse.setPassenger(player);
        horse.setTamed(true);
    }
}
