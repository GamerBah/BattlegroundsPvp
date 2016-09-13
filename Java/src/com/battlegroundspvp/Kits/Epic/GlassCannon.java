package com.battlegroundspvp.Kits.Epic;
/* Created by GamerBah on 8/27/2016 */


import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.Kit;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Rarity;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlassCannon extends Kit {

    public GlassCannon() {
        super(37, "Glass Cannon", new I(Material.STAINED_GLASS)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● Leather Helmet")
                .lore("§7   ● Leather Chestplate (Protection I)")
                .lore("§7   ● Leather Leggings")
                .lore("§7   ● Leather Boots")
                .lore("§7   ● Wooden Sword (Sharpness V)")
                .lore(" ")
                .lore("§c§lPotion Effects:")
                .lore("§7   ● §aStrength II")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.EPIC);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));

        player.getInventory().setHelmet(new I(Material.LEATHER_HELMET).name(BoldColor.GOLD.getColor() + "Glass Cannon Helmet").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.LEATHER_CHESTPLATE).name(BoldColor.GOLD.getColor() + "Glass Cannon Chestplate").enchantment(Enchantment.PROTECTION_ENVIRONMENTAL).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.LEATHER_LEGGINGS).name(BoldColor.GOLD.getColor() + "Glass Cannon Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.LEATHER_BOOTS).name(BoldColor.GOLD.getColor() + "Glass Cannon Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.WOOD_SWORD).name(BoldColor.GOLD.getColor() + "Glass Cannon Sword").enchantment(Enchantment.DAMAGE_ALL, 5).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
    }
}
