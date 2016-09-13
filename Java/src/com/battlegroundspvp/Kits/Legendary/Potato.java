package com.battlegroundspvp.Kits.Legendary;
/* Created by GamerBah on 9/7/2016 */


import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.Kit;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Rarity;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Potato extends Kit {

    public Potato() {
        super(47, "Potato", new I(Material.POISONOUS_POTATO)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● §3Potato Helmet §7(Protection V)")
                .lore("§7   ● §3Potato §7(Sharpness V)")
                .lore("§7   ● §3Poisonous Potato §7(Poison II)")
                .flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_ENCHANTS), Rarity.LEGENDARY);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.POTATO_ITEM).name(BoldColor.PINK.getColor() + "Potato Helmet").enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5));

        ItemStack potato = new ItemStack(new I(Material.POTATO_ITEM).enchantment(Enchantment.DAMAGE_ALL, 5)
                .name(BoldColor.PINK.getColor() + "Potato"));
        ItemStack poisonPotato = new ItemStack(new I(Material.POISONOUS_POTATO).enchantment(Enchantment.ARROW_INFINITE).flag(ItemFlag.HIDE_ENCHANTS)
                .name(BoldColor.PINK.getColor() + "Poisonous Potato")
                .lore(ChatColor.GRAY + "Poison II").lore(" ")
                .lore(ChatColor.GRAY + "Inflicts those you").lore(ChatColor.GRAY + "hit with Poison II"));

        player.getInventory().addItem(potato);
        player.getInventory().addItem(poisonPotato);
    }
}
