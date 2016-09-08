package me.gamerbah.Kits.Rare;
/* Created by GamerBah on 8/27/2016 */


import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Rarity;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Breaker extends Kit {

    public Breaker() {
        super(19, "Breaker", new I(Material.ARROW)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● §fIron Helmet §7(Projectile Prot. II)")
                .lore("§7   ● §fIron Chestplate")
                .lore("§7   ● §fIron Leggings")
                .lore("§7   ● §fIron Boots §7(Projectile Prot. I)")
                .lore("§7   ● §fIron Sword §7(Sharpness I)")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.RARE);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.IRON_HELMET).name(Rarity.RARE.getColor() + "Breaker Helmet").enchantment(Enchantment.PROTECTION_PROJECTILE, 2).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.IRON_CHESTPLATE).name(Rarity.RARE.getColor() + "Breaker Chestplate").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.IRON_LEGGINGS).name(Rarity.RARE.getColor() + "Breaker Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.IRON_BOOTS).name(Rarity.RARE.getColor() + "Breaker Boots").enchantment(Enchantment.PROTECTION_PROJECTILE, 1).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.IRON_SWORD).name(Rarity.RARE.getColor() + "Breaker Sword").enchantment(Enchantment.DAMAGE_ALL, 1).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
    }
}
