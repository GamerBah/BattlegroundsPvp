package com.battlegroundspvp.PlayerEvents;
/* Created by GamerBah on 8/9/2016 */


import com.battlegroundspvp.Administration.Commands.FreezeCommand;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.Rarity;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.Kit;
import com.battlegroundspvp.Utils.Kits.KitAbility;
import com.battlegroundspvp.Utils.Kits.KitManager;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerRespawn implements Listener {

    private Battlegrounds plugin;

    public PlayerRespawn(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
            event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation().add(0.5, 0.0, 0.5));
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(20F);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setExp(0);
        player.setLevel(0);
        player.setFlying(false);
        player.setAllowFlight(false);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        if (KitAbility.getPlayerStatus().containsKey(player.getName())) {
            KitAbility.getPlayerStatus().remove(player.getName());
            player.setExp(0);
            player.setLevel(0);
        }

        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.showPlayer(player);
        }

        if (FreezeCommand.frozenPlayers.contains(player) || FreezeCommand.frozen) {
            player.setWalkSpeed(0F);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -50, true, false));
            player.setFoodLevel(6);
            player.setSaturation(0);
        }

        player.getInventory().setItem(0, new I(Material.NETHER_STAR)
                .name(BoldColor.AQUA.getColor() + "Kit Selector" + ChatColor.GRAY + " (Right-Click)")
                .lore(ChatColor.GRAY + "Choose which kit you'll use!"));

        if (KitManager.getPreviousKit().containsKey(player.getUniqueId())) {
            Kit kit = KitManager.getPreviousKit().get(player.getUniqueId());
            player.getInventory().setItem(1, new I(Material.BOOK)
                    .name(BoldColor.GREEN.getColor() + "Previous Kit: " + kit.getRarity().getColor() + (kit.getRarity() == Rarity.EPIC || kit.getRarity() == Rarity.LEGENDARY ?
                            "" + ChatColor.BOLD : "") + kit.getName() + ChatColor.GRAY + " (Right-Click)")
                    .lore(ChatColor.GRAY + "Equips your previous kit"));
        }

        ItemStack head = new I(Material.SKULL_ITEM).durability(3).name(BoldColor.YELLOW.getColor() + "Player Profile" + ChatColor.GRAY + " (Right-Click)")
                .lore(ChatColor.GRAY + "View your unlocked cosmetics,").lore(ChatColor.GRAY + "achievements, and more!");
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(player.getName());
        head.setItemMeta(meta);

        player.getInventory().setItem(7, head);

        player.getInventory().setItem(8, new I(Material.REDSTONE_COMPARATOR)
                .name(BoldColor.RED.getColor() + "Settings" + ChatColor.GRAY + " (Right-Click)")
                .lore(ChatColor.GRAY + "Change your personal settings!"));

    }
}
