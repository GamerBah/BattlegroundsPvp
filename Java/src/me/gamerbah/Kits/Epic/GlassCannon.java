package me.gamerbah.Kits.Epic;
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

public class GlassCannon extends Kit {

    public GlassCannon() {
        super(37, "Glass Cannon", new I(Material.STAINED_GLASS)
                .lore(" ")
                .lore("§a§lKit Contents:")
                .lore("§7   ● Leather Helmet")
                .lore("§7   ● Leather Chestplate")
                .lore("§7   ● Leather Leggings")
                .lore("§7   ● Leather Boots")
                .lore("§7   ● Wooden Sword (Sharpness IV)")
                .flag(ItemFlag.HIDE_ATTRIBUTES), Rarity.EPIC);
    }

    protected void wear(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false));

        player.getInventory().setHelmet(new I(Material.LEATHER_HELMET).name(Rarity.EPIC.getColor() + "Glass Cannon Helmet").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setChestplate(new I(Material.LEATHER_CHESTPLATE).name(Rarity.EPIC.getColor() + "Glass Cannon Chestplate").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setLeggings(new I(Material.LEATHER_LEGGINGS).name(Rarity.EPIC.getColor() + "Glass Cannon Leggings").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));
        player.getInventory().setBoots(new I(Material.LEATHER_BOOTS).name(Rarity.EPIC.getColor() + "Glass Cannon Boots").unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        ItemStack sword = new ItemStack(new I(Material.WOOD_SWORD).name(Rarity.EPIC.getColor() + "Glass Cannon Sword").enchantment(Enchantment.DAMAGE_ALL, 4).unbreakable().flag(ItemFlag.HIDE_ATTRIBUTES).flag(ItemFlag.HIDE_UNBREAKABLE));

        player.getInventory().addItem(sword);
    }
}
