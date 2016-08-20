package me.gamerbah.Utils.Trails;
/* Created by GamerBah on 8/20/2016 */


import lombok.Getter;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Kits.Kit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrailManager {
    @Getter
    private static List<Trail> trails = new ArrayList<>();
    @Getter
    private static Map<Player, Trail> playerTrails = new HashMap<>();
    private Battlegrounds plugin;

    public TrailManager(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public static boolean hasTrailEquipped(Player player) {
        return playerTrails.containsKey(player);
    }

    public static boolean hasSpecificTrailEquipped(Player player, Kit kit) {
        return playerTrails.containsKey(player) && playerTrails.get(player).equals(kit);
    }

    public void equipTrail(Player player, Trail trail) {
        PlayerData playerData = Battlegrounds.getInstance().getPlayerData(player.getUniqueId());
        TrailManager.getPlayerTrails().put(player, trail);
    }

}
