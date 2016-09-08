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

public class HonorGuard extends Kit {

    public HonorGuard() {
        super(20, "Honor Guard", new I(Material.SHIELD)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● §fIron Helmet")
                .lore("§7   ● §fIron Chestplate §7(Protection I)")
                .lore("§7   ● §fIron Leggings")
                .lore("§7   ● §fIron Boots")
                .lore("§7   ● §bDiamond Sword")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.RARE);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false));

        player.getInventory().setHelmet(new I(Material.IRON_HELMET).name(Rarity.RARE.getColor() + "Honor Guard Helmet").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.IRON_CHESTPLATE).name(Rarity.RARE.getColor() + "Honor Guard Chestplate").enchantment(Enchantment.PROTECTION_ENVIRONMENTAL).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.IRON_LEGGINGS).name(Rarity.RARE.getColor() + "Honor Guard Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.IRON_BOOTS).name(Rarity.RARE.getColor() + "Honor Guard Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.DIAMOND_SWORD).name(Rarity.RARE.getColor() + "Honor Guard Sword").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
    }
}
