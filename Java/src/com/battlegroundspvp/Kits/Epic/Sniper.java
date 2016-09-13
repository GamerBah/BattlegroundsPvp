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

public class Sniper extends Kit {

    public Sniper() {
        super(38, "Sniper", new I(Material.BOW).enchantment(Enchantment.ARROW_KNOCKBACK)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● Chain Helmet")
                .lore("§7   ● Chaim Chestplate")
                .lore("§7   ● Chain Leggings")
                .lore("§7   ● Chain Boots")
                .lore("§7   ● Bow (Power II)")
                .lore("§7   ● Stone Sword")
                .flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_ENCHANTS), Rarity.EPIC);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.CHAINMAIL_HELMET).name(BoldColor.GOLD.getColor() + "Sniper Helmet").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.CHAINMAIL_CHESTPLATE).name(BoldColor.GOLD.getColor() + "Sniper Chainmail").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.CHAINMAIL_LEGGINGS).name(BoldColor.GOLD.getColor() + "Sniper Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.CHAINMAIL_BOOTS).name(BoldColor.GOLD.getColor() + "Sniper Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack bow = new ItemStack(new I(Material.BOW).name(BoldColor.GOLD.getColor() + "Sniper Bow").enchantment(Enchantment.ARROW_DAMAGE, 2).enchantment(Enchantment.ARROW_INFINITE).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        ItemStack sword = new ItemStack(new I(Material.STONE_SWORD).name(BoldColor.GOLD.getColor() + "Sniper Sword").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(bow);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(new ItemStack(Material.ARROW));
    }
}
