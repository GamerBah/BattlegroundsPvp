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

public class Enderknight extends Kit {

    public Enderknight() {
        super(39, "Enderknight", new I(Material.ENDER_PEARL)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● Chain Helmet")
                .lore("§7   ● Chain Chestplate")
                .lore("§7   ● Chain Leggings")
                .lore("§7   ● Chain Boots")
                .lore("§7   ● §fIron Sword §7(Knockback I)")
                .lore("§7   ● §5Enderpearl §7x5")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.EPIC);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.CHAINMAIL_HELMET).name(BoldColor.GOLD.getColor() + "Enderknight Helmet").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.CHAINMAIL_CHESTPLATE).name(BoldColor.GOLD.getColor() + "Enderknight Chestplate").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.CHAINMAIL_LEGGINGS).name(BoldColor.GOLD.getColor() + "Enderknight Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.CHAINMAIL_BOOTS).name(BoldColor.GOLD.getColor() + "Enderknight Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.IRON_SWORD).name(BoldColor.GOLD.getColor() + "Enderknight Sword").enchantment(Enchantment.KNOCKBACK, 1).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
        player.getInventory().addItem(new I(Material.ENDER_PEARL).name(BoldColor.GOLD.getColor() + "Enderpearl").amount(5));
    }
}
