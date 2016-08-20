package me.gamerbah.Kits;
/* Created by GamerBah on 8/19/2016 */


import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bowman extends Kit {

    public Bowman() {
        super("Bowman", new ItemStack(Material.BOW), ChatColor.GRAY + "A kit for long range fights", Rarity.COMMON);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        player.getInventory().setHelmet(new I(Material.LEATHER_HELMET).name(ChatColor.GRAY + "Bowman Helmet").unbreakable());
        player.getInventory().setChestplate(new I(Material.LEATHER_CHESTPLATE).name(ChatColor.GRAY + "Bowman Chestplate").unbreakable());
        player.getInventory().setLeggings(new I(Material.LEATHER_LEGGINGS).name(ChatColor.GRAY + "Bowman Leggings").unbreakable());
        player.getInventory().setBoots(new I(Material.LEATHER_BOOTS).name(ChatColor.GRAY + "Bowman Boots").unbreakable());
        ItemStack sword = new ItemStack(new I(Material.STONE_SWORD).name(ChatColor.GRAY + "Bowman Sword").enchantment(Enchantment.DAMAGE_ALL, 1)
                .enchantment(Enchantment.KNOCKBACK, 5).unbreakable());
        ItemStack bow = new ItemStack(new I(Material.BOW).name(ChatColor.GRAY + "Trusty Bow").enchantment(Enchantment.ARROW_DAMAGE, 2)
                .enchantment(Enchantment.ARROW_INFINITE).enchantment(Enchantment.ARROW_KNOCKBACK, 1).unbreakable());
        player.getInventory().addItem(bow);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(new ItemStack(Material.ARROW));
    }
}
