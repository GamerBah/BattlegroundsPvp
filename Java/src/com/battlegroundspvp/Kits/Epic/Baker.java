package com.battlegroundspvp.Kits.Epic;
/* Created by GamerBah on 9/7/2016 */


import com.battlegroundspvp.Utils.Enums.Rarity;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.Kit;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Baker extends Kit {

    public Baker() {
        super(40, "Baker", new I(Material.BREAD)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● Leather Helmet")
                .lore("§7   ● Leather Chestplate")
                .lore("§7   ● Leather Leggings")
                .lore("§7   ● Leather Boots")
                .lore("§7   ● §3Stick §7(Sharpness VI)")
                .lore("§7   ● §3Bread §7(Knockback II)")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.EPIC);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.LEATHER_HELMET).name(BoldColor.GOLD.getColor() + "Baker's Hat").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.LEATHER_CHESTPLATE).name(BoldColor.GOLD.getColor() + "Baker's Shirt").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.LEATHER_LEGGINGS).name(BoldColor.GOLD.getColor() + "Baker's Pants").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.LEATHER_BOOTS).name(BoldColor.GOLD.getColor() + "Baker's Shoes").color(Color.WHITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack stick = new ItemStack(new I(Material.STICK).name(BoldColor.GOLD.getColor() + "Rolling Pin").enchantment(Enchantment.DAMAGE_ALL, 6).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        ItemStack bread = new ItemStack(new I(Material.BREAD).name(BoldColor.GOLD.getColor() + "Knockbaguette").enchantment(Enchantment.KNOCKBACK, 2).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(stick);
        player.getInventory().addItem(bread);
    }
}
