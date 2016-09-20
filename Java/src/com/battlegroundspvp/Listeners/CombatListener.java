package com.battlegroundspvp.Listeners;

import com.battlegroundspvp.Administration.Runnables.AutoUpdate;
import com.battlegroundspvp.Administration.Utils.HackPrevention.HackPreventionTools;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Kits.Epic.DarkRider;
import com.battlegroundspvp.PlayerEvents.PlayerMove;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class CombatListener implements Listener {
    @Getter
    private static HashMap<UUID, Integer> tagged = new HashMap<>();
    @Getter
    private static HashMap<Player, Player> horseDamaged = new HashMap<>();
    private Battlegrounds plugin;
    private HashMap<UUID, Long> logged = new HashMap<>();

    public CombatListener(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();

            if (HackPreventionTools.getTargetPlayer(damager, 4) == null) {
                event.setCancelled(true);
                return;
            }

            if (Battlegrounds.currentTeams.containsKey(damaged.getName())) {
                if (Battlegrounds.currentTeams.get(damaged.getName()).equals(damager.getName())) {
                    if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                        event.setCancelled(true);
                        return;
                    }
                } else {
                    event.setCancelled(false);
                }
            }

            if (Battlegrounds.currentTeams.containsKey(damager.getName())) {
                if (Battlegrounds.currentTeams.get(damager.getName()).equals(damaged.getName())) {
                    if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                        event.setCancelled(true);
                        return;
                    }
                } else {
                    event.setCancelled(false);
                }
            }

            if (damaged.getVehicle() != null && damaged.getVehicle().getType() == EntityType.HORSE) {
                horseDamaged.put(damaged, damager);
            }
            if (!tagged.containsKey(damaged.getUniqueId())) {
                damaged.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damager.getName());
                tagged.put(damaged.getUniqueId(),
                        new BukkitRunnable() {
                            public void run() {
                                tagged.remove(damaged.getUniqueId());
                                damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                            }
                        }.runTaskLater(plugin, 240).getTaskId());
            } else {
                plugin.getServer().getScheduler().cancelTask(tagged.get(damaged.getUniqueId()));
                tagged.put(damaged.getUniqueId(),
                        new BukkitRunnable() {
                            public void run() {
                                tagged.remove(damaged.getUniqueId());
                                damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                            }
                        }.runTaskLater(plugin, 240).getTaskId());
            }

            if (!tagged.containsKey(damager.getUniqueId())) {
                damager.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damaged.getName());
                tagged.put(damager.getUniqueId(),
                        new BukkitRunnable() {
                            public void run() {
                                tagged.remove(damager.getUniqueId());
                                damager.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                            }
                        }.runTaskLater(plugin, 240).getTaskId());
            } else {
                plugin.getServer().getScheduler().cancelTask(tagged.get(damager.getUniqueId()));
                tagged.put(damager.getUniqueId(),
                        new BukkitRunnable() {
                            public void run() {
                                tagged.remove(damager.getUniqueId());
                                damager.sendMessage(ChatColor.GRAY + "You're no longer in combat");
                            }
                        }.runTaskLater(plugin, 240).getTaskId());
            }

            if (damager.getInventory().getItemInMainHand().getType().equals(Material.POISONOUS_POTATO)) {
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1, false, true));
            }
        }

        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                ProjectileSource shooter = arrow.getShooter();
                Player damager = (Player) shooter;
                Player damaged = (Player) event.getEntity();

                if (damaged.getLocation().distance(damaged.getWorld().getSpawnLocation()) <= 12) {
                    event.setCancelled(true);
                    return;
                }

                if (Battlegrounds.currentTeams.containsKey(damaged.getName())) {
                    if (Battlegrounds.currentTeams.get(damaged.getName()).equals(damager.getName())) {
                        if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                            event.setCancelled(true);
                            return;
                        } else {
                            event.setCancelled(false);
                            return;
                        }
                    } else {
                        event.setCancelled(false);
                        return;
                    }
                }

                if (Battlegrounds.currentTeams.containsKey(damager.getName())) {
                    if (Battlegrounds.currentTeams.get(damager.getName()).equals(damaged.getName())) {
                        if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                            event.setCancelled(true);
                            return;
                        } else {
                            event.setCancelled(false);
                            return;
                        }
                    } else {
                        event.setCancelled(false);
                        return;
                    }
                }

                if (damager.getName().equals(damaged.getName())) {
                    event.setCancelled(true);
                    return;
                }

                if (damaged.getVehicle() != null && damaged.getVehicle().getType() == EntityType.HORSE) {
                    horseDamaged.put(damaged, damager);
                }
                if (!tagged.containsKey(damaged.getUniqueId())) {
                    damaged.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damager.getName());
                    tagged.put(damaged.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damaged.getUniqueId());
                                    damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                } else {
                    plugin.getServer().getScheduler().cancelTask(tagged.get(damaged.getUniqueId()));
                    tagged.put(damaged.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damaged.getUniqueId());
                                    damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                }

                if (!tagged.containsKey(damager.getUniqueId())) {
                    damager.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damaged.getName());
                    tagged.put(damager.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damager.getUniqueId());
                                    damager.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                } else {
                    plugin.getServer().getScheduler().cancelTask(tagged.get(damager.getUniqueId()));
                    tagged.put(damager.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damager.getUniqueId());
                                    damager.sendMessage(ChatColor.GRAY + "You're no longer in combat");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                }
            }
        }

        if (event.getDamager() instanceof SmallFireball && event.getEntity() instanceof Player) {
            SmallFireball smallFireball = (SmallFireball) event.getDamager();
            if (smallFireball.getShooter() instanceof Player) {
                ProjectileSource shooter = smallFireball.getShooter();
                Player damager = (Player) shooter;
                Player damaged = (Player) event.getEntity();

                if (damaged.getLocation().distance(damaged.getWorld().getSpawnLocation()) <= 12) {
                    event.setCancelled(true);
                    return;
                }

                if (Battlegrounds.currentTeams.containsKey(damaged.getName())) {
                    if (Battlegrounds.currentTeams.get(damaged.getName()).equals(damager.getName())) {
                        if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                            event.setCancelled(true);
                            return;
                        } else {
                            event.setCancelled(false);
                            return;
                        }
                    } else {
                        event.setCancelled(false);
                        return;
                    }
                }

                if (Battlegrounds.currentTeams.containsKey(damager.getName())) {
                    if (Battlegrounds.currentTeams.get(damager.getName()).equals(damaged.getName())) {
                        if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                            event.setCancelled(true);
                            return;
                        } else {
                            event.setCancelled(false);
                            return;
                        }
                    } else {
                        event.setCancelled(false);
                        return;
                    }
                }

                if (damager.getName().equals(damaged.getName())) {
                    event.setCancelled(true);
                    return;
                }

                if (damaged.getVehicle() != null && damaged.getVehicle().getType() == EntityType.HORSE) {
                    horseDamaged.put(damaged, damager);
                }
                if (!tagged.containsKey(damaged.getUniqueId())) {
                    damaged.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damager.getName());
                    tagged.put(damaged.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damaged.getUniqueId());
                                    damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                } else {
                    plugin.getServer().getScheduler().cancelTask(tagged.get(damaged.getUniqueId()));
                    tagged.put(damaged.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damaged.getUniqueId());
                                    damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                }

                if (!tagged.containsKey(damager.getUniqueId())) {
                    damager.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damaged.getName());
                    tagged.put(damager.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damager.getUniqueId());
                                    damager.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                } else {
                    plugin.getServer().getScheduler().cancelTask(tagged.get(damager.getUniqueId()));
                    tagged.put(damager.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damager.getUniqueId());
                                    damager.sendMessage(ChatColor.GRAY + "You're no longer in combat");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                }
            }
        }

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Horse) {
            Player damager = (Player) event.getDamager();
            Horse horse = (Horse) event.getEntity();
            Player damaged = (Player) horse.getOwner();
            event.setCancelled(true);

            if (HackPreventionTools.getTargetPlayer(damager, 6) == null) {
                return;
            }

            if (Battlegrounds.currentTeams.containsKey(damaged.getName())) {
                if (Battlegrounds.currentTeams.get(damaged.getName()).equals(damager.getName())) {
                    if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                        event.setCancelled(true);
                        return;
                    }
                } else {
                    event.setCancelled(false);
                }
            }

            if (Battlegrounds.currentTeams.containsKey(damager.getName())) {
                if (Battlegrounds.currentTeams.get(damager.getName()).equals(damaged.getName())) {
                    if (plugin.getServer().getOnlinePlayers().size() >= 1) {
                        event.setCancelled(true);
                        return;
                    }
                } else {
                    event.setCancelled(false);
                }
            }

            if (DarkRider.getRiding().contains(damaged)) {
                damaged.damage(event.getFinalDamage());
                if (!tagged.containsKey(damaged.getUniqueId())) {
                    damaged.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damager.getName());
                    tagged.put(damaged.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damaged.getUniqueId());
                                    damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                } else {
                    plugin.getServer().getScheduler().cancelTask(tagged.get(damaged.getUniqueId()));
                    tagged.put(damaged.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damaged.getUniqueId());
                                    horseDamaged.remove(damaged);
                                    damaged.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                }

                if (!tagged.containsKey(damager.getUniqueId())) {
                    damager.sendMessage(ChatColor.GRAY + "You're now in combat with " + ChatColor.RED + damaged.getName());
                    tagged.put(damager.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damager.getUniqueId());
                                    damager.sendMessage(ChatColor.GRAY + "You're no longer in combat.");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                } else {
                    plugin.getServer().getScheduler().cancelTask(tagged.get(damager.getUniqueId()));
                    tagged.put(damager.getUniqueId(),
                            new BukkitRunnable() {
                                public void run() {
                                    tagged.remove(damager.getUniqueId());
                                    damager.sendMessage(ChatColor.GRAY + "You're no longer in combat");
                                }
                            }.runTaskLater(plugin, 240).getTaskId());
                }

                if (damager.getInventory().getItemInMainHand().getType().equals(Material.POISONOUS_POTATO)) {
                    damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5, 1, false, false));
                }
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (tagged.containsKey(player.getUniqueId())) {
            tagged.remove(player.getUniqueId());
            plugin.getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + player.getName() + ChatColor.RED + " has logged out while in combat!");
            logged.put(player.getUniqueId(), System.currentTimeMillis() + 120000);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (tagged.containsKey(player.getUniqueId()) && (event.getMessage().toLowerCase().startsWith("/spawn"))) {
            player.sendMessage(ChatColor.RED + "You cannot use that command during combat!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (tagged.containsKey(player.getUniqueId())) {
            plugin.getServer().getScheduler().cancelTask(tagged.get(player.getUniqueId()));
            tagged.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        if (logged.containsKey(uuid)) {
            if (System.currentTimeMillis() < logged.get(uuid)) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.RED + "\nYou are banned for combat logging.\nYour ban will expire 2 minutes from when you were banned.");
            } else {
                logged.remove(uuid);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                if (PlayerMove.getLaunched().contains(player)) {
                    event.setCancelled(true);
                    player.setFallDistance(0);
                    PlayerMove.getLaunched().remove(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1, 1);
                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.85F, 0.875F);
                }
                if (AutoUpdate.updating) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
