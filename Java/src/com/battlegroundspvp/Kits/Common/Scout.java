package com.battlegroundspvp.Kits.Common;
/* Created by GamerBah on 8/27/2016 */


import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.Kit;
import com.battlegroundspvp.Utils.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Scout extends Kit {

    public Scout() {
        super(4, "Scout", new I(Material.MAP)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● Leather Helmet")
                .lore("§7   ● §fIron Chestplate")
                .lore("§7   ● Chain Leggings")
                .lore("§7   ● Leather Boots")
                .lore("§7   ● §6Gold Sword")
                .lore(" ")
                .lore("§c§lPotion Effects:")
                .lore("§7   ● §aSpeed I")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.COMMON);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));

        player.getInventory().setHelmet(new I(Material.LEATHER_HELMET).name(Rarity.COMMON.getColor() + "Scout Helmet").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.IRON_CHESTPLATE).name(Rarity.COMMON.getColor() + "Scout Chestplate").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.CHAINMAIL_LEGGINGS).name(Rarity.COMMON.getColor() + "Scout Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.LEATHER_BOOTS).name(Rarity.COMMON.getColor() + "Scout Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.GOLD_SWORD).name(Rarity.COMMON.getColor() + "Scout Sword").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
    }
}
