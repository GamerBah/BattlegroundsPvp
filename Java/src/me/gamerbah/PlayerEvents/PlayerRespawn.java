package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/9/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Kits.KitManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

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
        player.setMaxHealth(20F);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setFlying(false);
        player.setAllowFlight(false);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.getInventory().setItem(0, new I(Material.NETHER_STAR)
                .name(BoldColor.AQUA.getColor() + "Kit Selector")
                .lore(ChatColor.GRAY + "Choose which kit you'll use!"));

        if (KitManager.getPreviousKit().containsKey(player.getUniqueId())) {
            Kit kit = KitManager.getPreviousKit().get(player.getUniqueId());
            player.getInventory().setItem(1, new I(Material.BOOK)
                    .name(BoldColor.GREEN.getColor() + "Previous Kit: " + kit.getRarity().getColor() + kit.getName())
                    .lore(ChatColor.GRAY + "Equips your previous kit"));
        }

        player.getInventory().setItem(7, new I(Material.DIAMOND)
                .name(BoldColor.YELLOW.getColor() + "Challenges")
                .lore(ChatColor.GRAY + "View your challenges, start")
                .lore(ChatColor.GRAY + "new ones, and receive rewards!"));

        player.getInventory().setItem(8, new I(Material.REDSTONE_COMPARATOR)
                .name(BoldColor.RED.getColor() + "Settings")
                .lore(ChatColor.GRAY + "Change your personal settings!"));

    }
}
