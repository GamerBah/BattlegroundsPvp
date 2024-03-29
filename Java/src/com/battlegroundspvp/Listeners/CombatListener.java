package com.battlegroundspvp.Listeners;

import com.battlegroundspvp.Administration.Runnables.AutoUpdate;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Commands.SpectateCommand;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.connorlinfoot.titleapi.TitleAPI;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
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

            /*if (HackPreventionTools.getTargetPlayer(damager, 4) == null) {
                event.setCancelled(true);
                return;
            }*/

            if (SpectateCommand.getSpectating().contains(damager) || SpectateCommand.getSpectating().contains(damaged)) {
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
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1, true, true));
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
                String health;
                if (damaged.getHealth() % 2 == 0) {
                    health = (((int) damaged.getHealth()) / 2) + "";
                } else {
                    health = (((int) damaged.getHealth()) / 2) + ".5";
                }

                TitleAPI.sendTitle(damager, 5, 30, 5, " ", BoldColor.YELLOW.getColor() + damaged.getName()
                        + BoldColor.GRAY.getColor() + " \u00BB " + BoldColor.WHITE.getColor() + health + BoldColor.RED.getColor() + " \u2764");

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

        if (tagged.containsKey(player.getUniqueId())
                && (event.getMessage().toLowerCase().startsWith("/spawn"))
                && (event.getMessage().toLowerCase().startsWith("/spectate"))) {
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
                if (Battlegrounds.getFallDmg().contains(player)) {
                    event.setCancelled(true);
                    player.setFallDistance(0);
                    Battlegrounds.getFallDmg().remove(player);
                }
                if (AutoUpdate.updating) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
