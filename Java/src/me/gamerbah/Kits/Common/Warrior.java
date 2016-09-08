package me.gamerbah.Kits.Common;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Rarity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Warrior extends Kit {

    public Warrior() {
        super(1, "Warrior", new I(Material.IRON_SWORD)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● §fIron Helmet")
                .lore("§7   ● §fIron Chestplate")
                .lore("§7   ● §fIron Leggings")
                .lore("§7   ● §fIron Boots")
                .lore("§7   ● §fIron Sword")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.COMMON);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.IRON_HELMET).name(Rarity.COMMON.getColor() + "Warrior Helmet").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.IRON_CHESTPLATE).name(Rarity.COMMON.getColor() + "Warrior Chestplate").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.IRON_LEGGINGS).name(Rarity.COMMON.getColor() + "Warrior Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.IRON_BOOTS).name(Rarity.COMMON.getColor() + "Warrior Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.IRON_SWORD).name(Rarity.COMMON.getColor() + "Warrior Sword").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
    }
}
