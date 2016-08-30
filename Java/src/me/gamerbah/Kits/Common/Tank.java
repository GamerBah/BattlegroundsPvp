package me.gamerbah.Kits.Common;
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

public class Tank extends Kit {

    public Tank() {
        super(3, "Tank", new I(Material.DIAMOND_CHESTPLATE)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● §bDiamond Helmet")
                .lore("§7   ● §bDiamond Chestplate")
                .lore("§7   ● §bDiamond Leggings")
                .lore("§7   ● §bDiamond Boots")
                .lore("§7   ● Wooden Sword (Sharpness I)")
                .lore(" ")
                .lore("§c§lPotion Effects:")
                .lore("§7   ● §cSlowness I")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.COMMON);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));

        player.getInventory().setHelmet(new I(Material.DIAMOND_HELMET).name(Rarity.COMMON.getColor() + "Tank Chestplate").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.DIAMOND_CHESTPLATE).name(Rarity.COMMON.getColor() + "Tank Chestplate").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.DIAMOND_LEGGINGS).name(Rarity.COMMON.getColor() + "Tank Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.DIAMOND_BOOTS).name(Rarity.COMMON.getColor() + "Tank Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.WOOD_SWORD).name(Rarity.COMMON.getColor() + "Tank Sword").enchantment(Enchantment.DAMAGE_ALL, 1).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
    }
}
