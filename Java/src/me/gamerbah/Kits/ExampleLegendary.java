package me.gamerbah.Kits;
/* Created by GamerBah on 8/19/2016 */


import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ExampleLegendary extends Kit {

    public ExampleLegendary() {
        super("Example Legendary", new ItemStack(Material.ENCHANTED_BOOK), ChatColor.GRAY + "An Example of a Legendary Kit", Rarity.LEGENDARY);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        player.getInventory().setChestplate(new I(Material.LEATHER_CHESTPLATE).name(Rarity.LEGENDARY.getColor() + "Example Legendary Chestplate").color(Color.RED).unbreakable());
        player.getInventory().setLeggings(new I(Material.IRON_LEGGINGS).name(Rarity.LEGENDARY.getColor() + "Example Legendary Leggings").unbreakable());
        player.getInventory().setBoots(new I(Material.DIAMOND_BOOTS).name(Rarity.LEGENDARY.getColor() + "Example Legendary Boots").unbreakable());
        ItemStack sword = new ItemStack(new I(Material.WOOD_SWORD).name(Rarity.LEGENDARY.getColor() + "Example Legendary Sword").enchantment(Enchantment.DAMAGE_ALL, 3)
                .enchantment(Enchantment.KNOCKBACK, 2).unbreakable());
        ItemStack book = new ItemStack(new I(Material.BOOK).name(Rarity.LEGENDARY.getColor() + "Example Legendary Spell").enchantment(Enchantment.MENDING));
        player.getInventory().addItem(sword);
        player.getInventory().addItem(book);
    }
}
