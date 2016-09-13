package com.battlegroundspvp.Utils.Kits;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Kits.Common.Bowman;
import com.battlegroundspvp.Kits.Common.Scout;
import com.battlegroundspvp.Kits.Common.Tank;
import com.battlegroundspvp.Kits.Common.Warrior;
import com.battlegroundspvp.Kits.Epic.Baker;
import com.battlegroundspvp.Kits.Epic.Enderknight;
import com.battlegroundspvp.Kits.Epic.GlassCannon;
import com.battlegroundspvp.Kits.Epic.Sniper;
import com.battlegroundspvp.Kits.Legendary.Potato;
import com.battlegroundspvp.Kits.Legendary.UltraTank;
import com.battlegroundspvp.Kits.Rare.Breaker;
import com.battlegroundspvp.Kits.Rare.HonorGuard;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class KitManager implements Listener {
    @Getter
    private static List<Kit> kits = new ArrayList<>();
    @Getter
    private static Map<UUID, Kit> playersInKits = new HashMap<>();
    @Getter
    private static Map<UUID, Kit> previousKit = new HashMap<>();

    public KitManager(Battlegrounds plugin) {
        // Common Kits
        kits.add(new Warrior());
        kits.add(new Bowman());
        kits.add(new Tank());
        kits.add(new Scout());

        // Rare Kits
        kits.add(new Breaker());
        kits.add(new HonorGuard());

        // Epic Kits
        kits.add(new GlassCannon());
        kits.add(new Sniper());
        kits.add(new Enderknight());
        kits.add(new Baker());

        // Legendary Kits
        kits.add(new UltraTank());
        kits.add(new Potato());

        for (Kit kit : kits) {
            plugin.getServer().getPluginManager().registerEvents(kit, plugin);
            getCommandMap().register(plugin.getConfig().getName(), getCommand(kit.getName().replaceAll("\\s+", ""), plugin));
            plugin.getCommand(kit.getName().replaceAll("\\s+", "")).setExecutor(kit);
        }
    }

    public static boolean isPlayerInKit(Player player) {
        return playersInKits.containsKey(player.getUniqueId());
    }

    public static boolean isPlayerInKit(Player player, Kit kit) {
        return playersInKits.containsKey(player.getUniqueId()) && playersInKits.get(player.getUniqueId()).equals(kit);
    }

    public static boolean isPlayerInKit(Player player, Class<? extends Kit> kit) {
        return playersInKits.containsKey(player.getUniqueId()) && playersInKits.get(player.getUniqueId()).getClass().equals(kit);
    }

    @EventHandler
    public final void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (isPlayerInKit(player)) {
            previousKit.put(player.getUniqueId(), playersInKits.get(player.getUniqueId()));
            playersInKits.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public final void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (isPlayerInKit(player)) {
            previousKit.put(player.getUniqueId(), playersInKits.get(player.getUniqueId()));
            playersInKits.remove(player.getUniqueId());
        }
    }

    public final void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (isPlayerInKit(player)) {
            playersInKits.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public final void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (isPlayerInKit(player)) {
            playersInKits.remove(player.getUniqueId());
        }
    }

    private PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return command;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;

        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return commandMap;
    }

}
