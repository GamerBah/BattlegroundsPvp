package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/15/2016 */


import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerBalanceChange extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private OfflinePlayer player;

    public PlayerBalanceChange(UUID uuid) {
        this.player = Bukkit.getOfflinePlayer(uuid);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
