package me.gamerbah.Kits;
/* Created by GamerBah on 8/15/2016 */


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

public class Standard extends Kit {

    public Standard() {
        super("Standard", new ItemStack(Material.DIAMOND_SWORD), ChatColor.GRAY + "The standard kit", Rarity.COMMON);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        player.getInventory().setHelmet(new I(Material.IRON_HELMET).name(ChatColor.GRAY + "Standard Helmet").unbreakable());
        player.getInventory().setChestplate(new I(Material.IRON_CHESTPLATE).name(ChatColor.GRAY + "Standard Chestplate").unbreakable());
        player.getInventory().setLeggings(new I(Material.IRON_LEGGINGS).name(ChatColor.GRAY + "Standard Leggings").unbreakable());
        player.getInventory().setBoots(new I(Material.IRON_BOOTS).name(ChatColor.GRAY + "Standard Boots").unbreakable());
        ItemStack sword = new ItemStack(new I(Material.DIAMOND_SWORD).name(ChatColor.GRAY + "Standard Sword").unbreakable());
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        player.getInventory().addItem(sword);
    }
}
